package com.lrs.models

import play.api.libs.json.Json

/**
  * Created by eguo on 11/27/17.
  */
case class RoadFeatureRecord(roadId: Long, dir: String, segments: List[Segment], detail: RoadFeatureDetail)

object RoadFeatureRecord{
  implicit def roadFeatureRecordFormat = Json.format[RoadFeatureRecord]
}
