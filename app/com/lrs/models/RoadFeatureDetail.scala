package com.lrs.models
import play.api.libs.json._
import reactivemongo.bson.BSONObjectID
import RoadAttribute._

/**
  * Created by vagrant on 10/18/17.
  */

case class RoadFeatureDetailAdmin(functionalClass: RoadAttributeCode,
                                COG: String,
                                county: String,
                                engineeringDistrict: String,
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
                                speedLimit: Double,
                                noPassingZone: String,
                                typeOfSignal: String,
                                truckRoute: String)

case class RoadFeatureDetailNonAdmin(
                                      wideningObstacle: RoadAttributeCode,
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
                                      percentOfGreenTime: Double,
                                      percentOfPassSight: Double,
                                      curbAndGutter: String,
                                      curbAndGutterToggle: String,
                                      barriar: String,
                                      barriarToggle: String,
                                      guardrail: String,
                                      guardrailToggle: String,
                                      division:String
                           )

case class RoadFeatureDetail(admin: RoadFeatureDetailAdmin, nonAdmin: RoadFeatureDetailNonAdmin)

object RoadFeatureDetail{
  implicit def roadFeatureDetail1Format = Json.format[RoadFeatureDetailAdmin]
  implicit def roadFeatureDetail2Format = Json.format[RoadFeatureDetailNonAdmin]
  implicit def roadFeatureDetailFormat = Json.format[RoadFeatureDetail]
}