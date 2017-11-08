package com.lrs.models

import play.api.libs.json.Json

/**
  * Created by vagrant on 11/8/17.
  */
case class SimpleRoad(roadId: Long, roadName: String)

object SimpleRoad{
  import reactivemongo.play.json.BSONFormats.BSONObjectIDFormat // This is required
  implicit val simpleRoadFormat = Json.format[SimpleRoad]
}