package com.lrs.models

import com.lrs.models.DataRecords.PointRecord
import play.api.libs.json.Json.JsValueWrapper
import play.api.libs.json._
import reactivemongo.play.json.BSONFormats.BSONObjectIDFormat // This is required

/**
  * Created by vagrant on 10/18/17.
  */

object RoadFeatureObj{
  case class RoadFeature( COG: String,
                        county: String,
                        engineerDistrict: String,
                        cutAndGutter: String,
                        cutAndGutterLeftOrCard: Boolean)

  case class RoadFeatures(roadId: Long, dir: String, segmentFeatures: Map[List[Segment], RoadFeature]) {
    def addFeature(rps: List[ReferencePoint], start: PointRecord, end: PointRecord, roadFeatures: List[RoadFeature]): RoadFeatures = ???
    def removeFeature(rps: List[ReferencePoint],  start: PointRecord, end: PointRecord) : RoadFeatures = ???
    def getFeatures(rps: List[ReferencePoint], start: PointRecord, end: PointRecord) : List[RoadFeature] = ???
  }

  implicit def roadFeatureFormat = Json.format[RoadFeature]

  implicit val mapReads: Reads[Map[List[Segment], RoadFeature]] = new Reads[Map[List[Segment], RoadFeature]] {
    def reads(jv: JsValue): JsResult[Map[List[Segment], RoadFeature]] =
      JsSuccess(jv.as[Map[String, RoadFeature]].map{case (k, v) =>
        Json.parse(k).as[List[Segment]] -> v .asInstanceOf[RoadFeature]
      })
  }

  implicit val mapWrites: Writes[Map[List[Segment], RoadFeature]] = new Writes[Map[List[Segment], RoadFeature]] {
    def writes(map: Map[List[Segment], RoadFeature]): JsValue =
      Json.obj(map.map{case (s, o) =>
        val ret: (String, JsValueWrapper) = s.toString -> Json.toJson(o)
        ret
      }.toSeq:_*)
  }

  implicit val mapFormat: Format[Map[List[Segment], RoadFeature]] = Format(mapReads, mapWrites)
  implicit def roadFeaturesFormat = Json.format[RoadFeatures]

  object RoadFeatures{
    def getRoadFeatures(roadId: Long, dir: String) : RoadFeatures = ???
  }
}