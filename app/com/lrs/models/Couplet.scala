package com.lrs.models

import com.lrs.daos.core.TemporalModel
import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID
import org.joda.time.DateTime


/**
  * Created by eguo on 11/17/17.
  */

case class CoupletSegment(roadId: Long, dir: String, startRpName: String, startOffset: Double, endRpName: String, endOffset: Double)

case class Couplet(
                  coupletName: String,
                  primary: CoupletSegment,
                  secondary: CoupletSegment,
                  coupletType: String,
                  medianType: String,
                  medianWidth: Double,
                  divisionType: String,
                  var _id: Option[BSONObjectID] = None,
                  var created: Option[DateTime] = None,
                  var updated: Option[DateTime] = None) extends TemporalModel

object Couplet{
  import reactivemongo.play.json.BSONFormats.BSONObjectIDFormat // This is required
  implicit def coupletSegmentFormat = Json.format[CoupletSegment]
  implicit def coupletFormat = Json.format[Couplet]

}