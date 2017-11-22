package com.lrs.models

import play.api.libs.json.Json

/**
  * Created by eguo on 11/21/17.
  */
case class RampPoint(name: String, x:Double, y:Double, z: Double, pointType: PointType, roadId: Option[Long] = None, dir: Option[String] = None, segmentPoint: Option[SegmentPoint] = None) extends Point

object RampPoint{
  import reactivemongo.play.json.BSONFormats.BSONObjectIDFormat // This is required
  implicit def rampPointFormat = Json.format[RampPoint]
}