package com.lrs.models

import com.lrs.daos.core.TemporalModel
import com.lrs.models.DataRecords.PointRecord
import org.joda.time.DateTime
import play.api.libs.json.Json.JsValueWrapper
import play.api.libs.json._
import reactivemongo.bson.BSONObjectID
import RoadAttribute._

import reactivemongo.play.json.BSONFormats.BSONObjectIDFormat // This is required
import play.api.libs.functional.syntax._

/**
  * Created by vagrant on 10/18/17.
  */

object RoadFeatures{
  case class RoadFeatureDetailAdmin(functionalClass: RoadAttributeCode,
                                COG: String,
                                county: String,
                                engineerDistrict: String,
                                indianReservation: String,
                                legislatureDistrict: String,
                                naaqsArea: String,
                                nationalForest: String,
                                urbanArea: String,
                                cityOrTown: String,
                                nhs: String,
                                park: String,
                                privateLand: String,
                                bureauOfLand: String,
                                speedLimit: String,
                                noPassingZone: String,
                                typeOfSignal: String,
                                truckRoute: String)

  case class RoadFeatureDetailNonAdmin(
                                 wideningObstacles: RoadAttributeCode,
                                 curve: RoadAttributeCode,
                                 grade: RoadAttributeCode,
                                 terrain: RoadAttributeCode,
                                 climateZone: RoadAttributeCode,
                                 surfaceType: RoadAttributeCode,
                                 soilType: RoadAttributeCode,
                                 shoulder: RoadAttributeCode,
                                 shoulderToggle: String,
                                 medianType: RoadAttributeCode,
                               wideningPotential: String,
                               percentOfGreenTime: String,
                               percentOfPassSight: String,
                               cutAndGutter: String,
                               cutAndGutterLeftOrCard: String,
                               barriar: String,
                               barriarToggle: String,
                               Guardrail: String,
                               guardrailToggle: String,
                               sideShoulder: String,
                               division:String
                           )

  case class RoadFeatureDetail(admin: RoadFeatureDetailAdmin, nonAdmin: RoadFeatureDetailNonAdmin)

  case class RoadFeature(roadId: Long, dir: String, segmentFeatures: Map[List[Segment], RoadFeatureDetail],
                         var _id: Option[BSONObjectID] = None,
                         var created: Option[DateTime] = None,
                         var updated: Option[DateTime] = None) extends TemporalModel {
    def addFeature(rps: List[ReferencePoint], start: PointRecord, end: PointRecord, roadFeatures: List[RoadFeatureDetail]): RoadFeature = ???
    def removeFeature(rps: List[ReferencePoint],  start: PointRecord, end: PointRecord) : RoadFeature = ???
    def getFeatures(rps: List[ReferencePoint], start: PointRecord, end: PointRecord) : List[RoadFeatureDetail] = ???
  }

  implicit def roadFeatureDetail1Format = Json.format[RoadFeatureDetailAdmin]
  implicit def roadFeatureDetail2Format = Json.format[RoadFeatureDetailNonAdmin]
  implicit def roadFeatureDetailFormat = Json.format[RoadFeatureDetail]


  /*val fields1to21: Reads[(RoadAttributeCode, RoadAttributeCode, RoadAttributeCode, RoadAttributeCode, RoadAttributeCode, RoadAttributeCode, RoadAttributeCode, RoadAttributeCode, RoadAttributeCode, String, RoadAttributeCode, String,String,String,String,String,String,String,String,String,String,String)] = (
    (__ \ "functionalClass").read[RoadAttributeCode] and
      (__ \ "wideningObstacles").read[RoadAttributeCode] and
      (__ \ "curve").read[RoadAttributeCode] and
      (__ \ "grade").read[RoadAttributeCode] and
      (__ \ "terrain").read[RoadAttributeCode] and
      (__ \ "climateZone").read[RoadAttributeCode] and
      (__ \ "surfaceType").read[RoadAttributeCode] and
      (__ \ "soilType").read[RoadAttributeCode] and
      (__ \ "shoulder").read[RoadAttributeCode] and
      (__ \ "shoulderToggle").read[String] and
      (__ \ "medianType").read[RoadAttributeCode] and
      (__ \ "COG").read[String] and
      (__ \ "county").read[String] and
      (__ \ "engineerDistrict").read[String] and
      (__ \ "indianReservation").read[String] and
      (__ \ "legislatureDistrict").read[String] and
      (__ \ "naaqsArea").read[String] and
      (__ \ "nationalForest").read[String] and
      (__ \ "urbanArea").read[String] and
      (__ \ "cityOrTown").read[String] and
      (__ \ "nhs").read[String] and
      (__ \ "truckRoute").read[String]).tupled

  val field22toall: Reads[(String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String)] =
  (
      (__ \ "park").read[String] and
      (__ \ "privateLand").read[String] and
        (__ \ "bureauOfLand").read[String] and
        (__ \ "wideningPotential").read[String] and
        (__ \ "speedLimit").read[String] and
        (__ \ "noPassingZone").read[String] and
        (__ \ "typeOfSignal").read[String] and
        (__ \ "percentOfGreenTime").read[String] and
        (__ \ "percentOfPassSight").read[String] and
        (__ \ "cutAndGutter").read[String] and
        (__ \ "cutAndGutterLeftOrCard").read[String] and
        (__ \ "barriar").read[String] and
        (__ \ "barriarToggle").read[String] and
        (__ \ "Guardrail").read[String] and
        (__ \ "sideShoulder").read[String] and
        (__ \ "division").read[String] and
        (__ \ "guardrailToggle").read[String]).tupled

  implicit val roadFeatureFormat: Reads[RoadFeatureDetail] = (
    fields1to21 and field22toall
    ) {
    case ((a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17,a18,a19,a20,a21,a22),(b1,b2,b3,b4,b5,b6,b7,b8,b9,b10,b11,b12,b13,b14,b15,b16,b17)) =>
      RoadFeatureDetail(a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17,a18,a19,a20,a21,a22, b1,b2,b3,b4,b5,b6,b7,b8,b9,b10,b11,b12,b13,b14,b15,b16,b17)
  }
  */

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