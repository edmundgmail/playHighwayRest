package com.lrs.models

import com.lrs.daos.core.TemporalModel
import org.joda.time.DateTime
import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID

/**
  * Created by eguo on 11/24/17.
  */

case class RoadAttributeCode(code: String, desc: String, source: String)
case class RoadAttribute(attributeName: String, categoryName: String, attributeId: String, categoryId: String, codes: List[RoadAttributeCode],
  var _id: Option[BSONObjectID] = None,
  var created: Option[DateTime] = None,
  var updated: Option[DateTime] = None) extends TemporalModel


object RoadAttribute{
  import reactivemongo.play.json.BSONFormats.BSONObjectIDFormat // This is required
  implicit val roadattributecodeFormat = Json.format[RoadAttributeCode]
  implicit val roadattributeFormat = Json.format[RoadAttribute]
}