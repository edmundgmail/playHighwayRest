package com.lrs.highway

import javax.inject.{Inject, Singleton}

import com.lrs.models.DataRecords._
import com.lrs.models.{Project, _}
import com.lrs.models.RoadFeatures.RoadFeature
import play.api.Logger
import play.api.libs.json.{JsObject, Json}
import reactivemongo.bson.BSONObjectID

import scala.concurrent.{Await, Future}
import scala.util.Try
import scala.util.parsing.json.JSONObject
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.io.Source

@Singleton
class HighwayService @Inject()(repository: RoadRepository, featureRepository: RoadFeatureRepository, projectRepository: ProjectRepository,
                              coupletRepository: CoupletRepository, rampRepository: RampRepository, roadAttributeRepository: RoadAttributeRepository) {

  def getRamps : Future[List[Ramp]] = {
    rampRepository.find[Ramp]()
  }

  def getRamp(id: Long) : Future[Option[Ramp]] = {
    rampRepository.findOne(Json.obj("rampId" -> id))
  }

  case class TestAttributeRecord(ATTRIBUTENAME: String, CATEGORYNAME: String, CAT_NO: String, ATT_NO:String, CODE: String, DESCRIPTION:String, CODESOURCE:String)
  private def toRoadAttribute(catname: String,  attrname: String, catno: String, attrno: String, stream: Stream[TestAttributeRecord]) : RoadAttribute = {
    RoadAttribute(catname, attrname, catno, attrno, stream.map(s=> RoadAttributeCode(s.CODE, s.DESCRIPTION, s.CODESOURCE)).toList)
  }

  def createAttributes = {

    val lines = Source.fromFile("csv/roadattributes.csv").getLines.drop(1)
    val records = lines.map(_.split(",")).map(r=>TestAttributeRecord(r(0), r(1), r(2), r(3), r(4), r(5), r(6))).toStream
    val entities = records.groupBy[(String, String, String, String)](r=> (r.CATEGORYNAME,  r.ATTRIBUTENAME, r.CAT_NO,r.ATT_NO)).map(r=>toRoadAttribute(r._1._1,  r._1._2, r._1._3, r._1._4, r._2))

    entities.foreach(roadAttributeRepository.insert(_))
  }

  def createRamp(entity: Ramp) = {
    this.getRamp(entity.rampId).flatMap{
      case Some(ramp) => {
        rampRepository.update(ramp._id.get.stringify, entity)
      }

      case _ => {
        rampRepository.insert(entity)
      }
    }
  }

  def getCouplets : Future[List[Couplet]] = {
    coupletRepository.find[Couplet]()
  }

  def getCouplet(name: String) : Future[Option[Couplet]] = {
    coupletRepository.findOne(Json.obj("coupletName" -> name))
  }

  def createCouplet(entity: Couplet) = {
    this.getCouplet(entity.coupletName).flatMap{
      case Some(couplet) => {
        coupletRepository.update(entity._id.get.stringify, entity)
      }

      case _ => {
        coupletRepository.insert(entity)
      }
    }
  }

  def getProjects : Future[List[Project]] = {
    projectRepository.find[Project]()
  }

  def getProject(name: String) : Future[Option[Project]] = {
    projectRepository.findOne(Json.obj("projectName" -> name))
  }

  def createProject(entity: Project) = {
    this.getProject(entity.projectName).flatMap{
      case Some(project) => {
        projectRepository.update(project._id.get.stringify, entity)
      }

      case _ => {
        projectRepository.insert(entity)
      }
    }
  }


  def get(id: Long) : Future[Option[Road]] = {
    repository.findOne(Json.obj("roadId" -> id))
  }

  def getSync(id: Long) : Option[Road] = {
    Await.result(get(id), Duration.Inf)
  }

  def getall : Future[List[SimpleRoad]] = {
    repository.find[SimpleRoad](projection = Json.obj("roadId"->1,"roadName"->1))
  }

  def getRPs(id: Long, dir:String) : Future[List[ReferencePoint]] = {
    this.get(id).flatMap {
      case Some(road) => Future(road.directions.filter(_.dir.equals(dir)).map(_.rps).flatten)
      case _ => Future(List.empty[ReferencePoint])
    }
  }

  def getSegmentStartRPs(id: Long, dir:String) : Future[List[ReferencePoint]] = {
    this.get(id).flatMap {
      case Some(road) => Future(road.directions.filter(_.dir.equals(dir)).map(_.getSegmentStartRPs).flatten)
      case _ => Future(List.empty[ReferencePoint])
    }
  }

  def getSegmentEndRPs(id: Long, dir:String) : Future[List[ReferencePoint]] = {
    this.get(id).flatMap {
      case Some(road) => Future(road.directions.filter(_.dir.equals(dir)).map(_.getSegmentEndRPs).flatten)
      case _ => Future(List.empty[ReferencePoint])
    }
  }

  private def update(road: Road): Future[Try[Road]] = {
      repository.update(road._id.get.stringify, road)
  }



  def handleHighwayRecord(entity:DataRecord) = {
    Logger.info(entity.toString)

    entity match {
      case record: AddRoadRecord => {
        val newRoad = Road.fromJson(record)

        this.get(record.roadId).flatMap{
          case Some(road) => {
              val updatedRoad = road.copy(directions = road.directions, _id = road._id)
              update(updatedRoad)
          }

          case _ => {
            repository.insert(newRoad)
          }
        }
      }

      case record: RemoveSegmentRecord => {
        this.get(record.roadId).flatMap{
          case Some(road)=> {
            update(road.removeSegment(record.dir, record.startPoint, record.endPoint))
          }
        }
      }

      case record: AddSegmentRecord => {
        this.get(record.roadId).flatMap{
          case Some(road)=> {
            Logger.info("got the road")
            update(road.addSegment(record.dir, record.segment, record.afterRP, record.leftConnect, record.beforeRP, record.rightConnect))
          }
        }
      }

      case record: UpdateLaneRecord => {
        this.get(record.roadId).flatMap{
          case Some(road)=> {
            update(road.updateLanes(record.dir, record.lanes))
          }
        }
      }

      case record: TransferSegmentRecord => {
        val fromRoad = getSync(record.roadId).get
        val toRoad = getSync(record.toRoadId).get

        val fromRps = fromRoad.getRps(record.fromDir)
        val segmentString = fromRoad.getSegmentString(record.fromDir, record.startPoint, record.endPoint)
        val fromRoadFeatures = RoadFeature.getRoadFeatures(record.roadId, record.fromDir)

        val newfromRoad =fromRoad.removeSegment(record.fromDir, record.startPoint, record.endPoint)
        val newToRoad = toRoad.addSegment(record.toDir, segmentString, record.afterRP, record.leftConnect, record.beforeRP, record.rightConnect)

        val toRoadFeatures = RoadFeature.getRoadFeatures(record.toRoadId, record.toDir)
        val transferedFeatures = fromRoadFeatures.getFeatures(fromRps, record.startPoint, record.endPoint)
        val newFromRoadFeatures = fromRoadFeatures.removeFeature(fromRps, record.startPoint, record.endPoint)
        val toRps = newToRoad.getRps(record.toDir)
        val newToRoadFeatures = toRoadFeatures.addFeature(toRps, record.startPoint, record.endPoint, transferedFeatures)

        update(newToRoad)
        update(newfromRoad)
        //MongoUtils.updateRoadFeatures(newFromRoadFeatures)
        //MongoUtils.updateRoadFeatures(newToRoadFeatures)
      }

      case _ => throw new Exception("Unknown request type")
    }
  }
}
