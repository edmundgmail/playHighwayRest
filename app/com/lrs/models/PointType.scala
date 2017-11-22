package com.lrs.models

import play.api.libs.json._


sealed abstract class PointType(val value: String) extends Serializable
case object SystemRoad extends PointType("SystemRoad")
case object NonSystemRoad extends PointType("NonSystemRoad")
case object NonRoad extends PointType("NonRoad")

object PointType {

  def apply(value: String): PointType = value match {
    case SystemRoad.value => SystemRoad
    case NonSystemRoad.value => NonSystemRoad
    case NonRoad.value => NonRoad
    case _ => throw new Exception(s"Invalid Point type: $value")
  }

  def unapply(arg: PointType): Option[String] = Some(arg.value)

  import reactivemongo.play.json.BSONFormats.BSONObjectIDFormat // This is required
  implicit  def pointTypeFormat = new Format[PointType]{
    def reads(json: JsValue) = JsSuccess(PointType.apply(json.as[String])) // doesn't compile
    def writes(myEnum: PointType) = JsString(myEnum.toString)
  }
}