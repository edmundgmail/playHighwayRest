package com.lrs.models

import play.api.libs.json.Json
import RoadTreatment._

/**
  * Created by eguo on 11/27/17.
  */
case class RoadTreatmentRecord(roadId: Long, dir: String, lanes: List[Lane], treatments: List[Treatment])

object RoadTreatmentRecord
{
  implicit def roadTreatmentRecordFormat = Json.format[RoadTreatmentRecord]
}
