package com.lrs.highway

import javax.inject.{Inject, Singleton}

import com.lrs.models.DataRecords._
import com.lrs.models.{Road, SimpleRoad}
import play.api.libs.json.{JsObject, Json}
import reactivemongo.bson.BSONObjectID

import scala.concurrent.Future
import scala.util.Try
import scala.util.parsing.json.JSONObject
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class HighwayService @Inject()(repository: HighwayRepository) {


  def get(id: Long) : Future[Option[Road]] = {
    repository.findOne(Json.obj("roadId" -> id))
  }

  def getall : Future[List[SimpleRoad]] = {
    repository.find[SimpleRoad](projection = Json.obj("roadId"->1,"roadName"->1))
  }

  private def update(road: Road): Future[Try[Road]] = {
      repository.update(road._id.get.toString, road)
  }

  def handleHighwayRecord(entity:DataRecord) = {
    entity match {
      case record: AddRoadRecord => {
        val road = Road.fromJson(record)
        repository.insert(road)
      }

      case record: RemoveSegmentRecord => {
        this.get(record.roadId).flatMap{
          case Some(road)=> {
            update(road.removeSegment(record.dir, record.startPoint, record.endPoint))
          }
        }
      }

      case _ => throw new Exception("Unknown request type")
    }
  }
}
