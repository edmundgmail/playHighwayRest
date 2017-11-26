package com.lrs.models

import com.lrs.daos.core.TemporalModel
import org.joda.time.DateTime
import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID

import scala.util.hashing.MurmurHash3

/**
  * Created by eguo on 11/17/17.
  */



case class Ramp(
               rampId:Long,
               rampName: String,
               length: Double,
               pavementType: String,
               metered: String,
               fromPoint: RampPoint,
               toPoint: RampPoint,
                 var _id: Option[BSONObjectID] = None,
                 var created: Option[DateTime] = None,
                 var updated: Option[DateTime] = None) extends TemporalModel {
  def ID = rampId match {
    case 0 => MurmurHash3.stringHash(rampName).toLong
    case _ => rampId
  }
}

object Ramp{

  private def ID(rampId: Long, rampName: String) =
    rampId match {
      case 0 => MurmurHash3.stringHash(rampName).toLong
      case _ => rampId
    }

  def apply(rampId:Long,
            rampName: String,
            length: Double,
            pavementType: String,
            metered: String,
            fromPoint: RampPoint,
            toPoint: RampPoint) = {


    new Ramp(ID(rampId, rampName), rampName, length, pavementType, metered, fromPoint, toPoint)
  }
  import reactivemongo.play.json.BSONFormats.BSONObjectIDFormat // This is required
  implicit def rampFormat = Json.format[Ramp]

}
