package com.lrs.models

import com.lrs.daos.core.TemporalModel
import org.joda.time.DateTime
import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID

/**
  * Created by eguo on 11/17/17.
  */
case class Ramp(
               rampId:Long,
               rampName: String,
               roadId: Option[Long],
               dir: Option[String],
               category: String,

                 var _id: Option[BSONObjectID] = None,
                 var created: Option[DateTime] = None,
                 var updated: Option[DateTime] = None) extends TemporalModel

object Ramp{
  import reactivemongo.play.json.BSONFormats.BSONObjectIDFormat // This is required
  implicit def rampFormat = Json.format[Ramp]

}
