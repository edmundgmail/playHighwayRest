package com.lrs.models

import com.lrs.daos.core.TemporalModel
import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID
import org.joda.time.DateTime


/**
  * Created by eguo on 11/17/17.
  */
case class Couplet(
                  coupletId: Long,
                    var _id: Option[BSONObjectID] = None,
                    var created: Option[DateTime] = None,
                    var updated: Option[DateTime] = None) extends TemporalModel

object Couplet{
  import reactivemongo.play.json.BSONFormats.BSONObjectIDFormat // This is required
  implicit def coupletFormat = Json.format[Couplet]

}