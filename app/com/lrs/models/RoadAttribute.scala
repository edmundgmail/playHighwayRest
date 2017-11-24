package com.lrs.models

import play.api.libs.json.Json

/**
  * Created by eguo on 11/24/17.
  */

case class RoadAttributeCode(code: String, desc: String, source: String)
case class RoadAttribute(name: String, category: String, codes: List[RoadAttributeCode])

object RoadAttribute{
  import reactivemongo.play.json.BSONFormats.BSONObjectIDFormat // This is required
  implicit val roadattributecodeFormat = Json.format[RoadAttributeCode]
  implicit val roadattributeFormat = Json.format[RoadAttribute]
}