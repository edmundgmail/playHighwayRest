package com.lrs.models

import com.lrs.daos.core.TemporalModel
import com.lrs.models.DataRecords.PointRecord
import org.joda.time.DateTime
import play.api.libs.json.Json.JsValueWrapper
import play.api.libs.json._
import reactivemongo.bson.BSONObjectID
import RoadAttribute._

import reactivemongo.play.json.BSONFormats.BSONObjectIDFormat // This is required

/**
  * Created by vagrant on 10/18/17.
  */

object RoadFeatures{
  case class RoadFeatureDetail(functionalClass: RoadAttributeCode,
                                COG: String,
                               county: String,
                               engineerDistrict: String,
                               cutAndGutter: String,
                               cutAndGutterLeftOrCard: Boolean)

  case class RoadFeature(roadId: Long, dir: String, segmentFeatures: Map[List[Segment], RoadFeatureDetail],
                         var _id: Option[BSONObjectID] = None,
                         var created: Option[DateTime] = None,
                         var updated: Option[DateTime] = None) extends TemporalModel {
    def addFeature(rps: List[ReferencePoint], start: PointRecord, end: PointRecord, roadFeatures: List[RoadFeatureDetail]): RoadFeature = ???
    def removeFeature(rps: List[ReferencePoint],  start: PointRecord, end: PointRecord) : RoadFeature = ???
    def getFeatures(rps: List[ReferencePoint], start: PointRecord, end: PointRecord) : List[RoadFeatureDetail] = ???
  }
  implicit def roadFeatureFormat = Json.format[RoadFeatureDetail]

  implicit val mapReads: Reads[Map[List[Segment], RoadFeatureDetail]] = new Reads[Map[List[Segment], RoadFeatureDetail]] {
    def reads(jv: JsValue): JsResult[Map[List[Segment], RoadFeatureDetail]] =
      JsSuccess(jv.as[Map[String, RoadFeatureDetail]].map{case (k, v) =>
        Json.parse(k).as[List[Segment]] -> v .asInstanceOf[RoadFeatureDetail]
      })
  }

  implicit val mapWrites: Writes[Map[List[Segment], RoadFeatureDetail]] = new Writes[Map[List[Segment], RoadFeatureDetail]] {
    def writes(map: Map[List[Segment], RoadFeatureDetail]): JsValue =
      Json.obj(map.map{case (s, o) =>
        val ret: (String, JsValueWrapper) = s.toString -> Json.toJson(o)
        ret
      }.toSeq:_*)
  }

  implicit val mapFormat: Format[Map[List[Segment], RoadFeatureDetail]] = Format(mapReads, mapWrites)
  implicit def roadFeaturesFormat = Json.format[RoadFeature]

  object RoadFeature{
    def getRoadFeatures(roadId: Long, dir: String) : RoadFeature = ???
  }
}