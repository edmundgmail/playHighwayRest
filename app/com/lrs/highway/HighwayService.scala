package com.lrs.highway

import javax.inject.{Inject, Singleton}

import com.lrs.models.DataRecords._
import com.lrs.models.Project
import com.lrs.models.RoadFeatures.RoadFeature
import com.lrs.models._
import play.api.Logger
import play.api.libs.json.{JsObject, Json}
import reactivemongo.bson.BSONObjectID

import scala.concurrent.{Await, Future}
import scala.util.Try
import scala.util.parsing.json.JSONObject
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

@Singleton
class HighwayService @Inject()(repository: RoadRepository, featureRepository: RoadFeatureRepository, projectRepository: ProjectRepository) {

  def getProjects : Future[List[Project]] = {
    projectRepository.find[Project]()
  }

  def getProject(id: Long) : Future[Option[Project]] = {
    projectRepository.findOne(Json.obj("projectId" -> id))
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


  def createProject(entity: Project) = {
    this.getProject(entity.projectId).flatMap{
      case Some(project) => {
        projectRepository.insert(entity)
      }

      case _ => {
        projectRepository.insert(entity)
      }
    }
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
            update(road.updateLane(record.dir, record.lane))
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
