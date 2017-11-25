package com.lrs.models

import com.lrs.daos.core.TemporalModel
import com.lrs.models.DataRecords.PointRecord
import org.joda.time.DateTime
import play.api.libs.json.Json.JsValueWrapper
import play.api.libs.json._
import reactivemongo.bson.BSONObjectID
import RoadAttribute._

/**
  * Created by vagrant on 10/18/17.
  */

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

object RoadFeatureDetail{
  implicit def roadFeatureDetail1Format = Json.format[RoadFeatureDetailAdmin]
  implicit def roadFeatureDetail2Format = Json.format[RoadFeatureDetailNonAdmin]
  implicit def roadFeatureDetailFormat = Json.format[RoadFeatureDetail]
}