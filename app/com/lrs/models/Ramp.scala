package com.lrs.models

import com.lrs.daos.core.TemporalModel
import org.joda.time.DateTime
import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID

/**
  * Created by eguo on 11/17/17.
  */

case class RampPoint(name: String, x:Double, y:Double, z: Double, pointType: String, roadId: Option[Long] = None, dir: Option[String] = None, segmentPoint: Option[SegmentPoint] = None) extends Point

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
                 var updated: Option[DateTime] = None) extends TemporalModel

object Ramp{
  import reactivemongo.play.json.BSONFormats.BSONObjectIDFormat // This is required
  implicit def rampPointFormat = Json.format[RampPoint]
  implicit def rampFormat = Json.format[Ramp]

}
