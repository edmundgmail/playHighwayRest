package com.lrs.models

import com.lrs.daos.core.TemporalModel
import org.joda.time.DateTime
import play.api.libs.json.Json.JsValueWrapper
import play.api.libs.json._
import reactivemongo.bson.BSONObjectID

/**
  * Created by vagrant on 10/18/17.
  */
case class TreatmentDetail(layerNo: Int, material: String, materialDesign: String, thickness: Double)
case class Treatment(desc: String, details: List[TreatmentDetail])

case class RoadTreatment(roadId: Long, dir: String, map: Map[Lane, Treatment],
                    var _id: Option[BSONObjectID] = None,
                    var created: Option[DateTime] = None,
                    var updated: Option[DateTime] = None) extends TemporalModel{
  def addTreament(lane: Lane, treatment: Treatment): RoadTreatment = {
    RoadTreatment(this.roadId, this.dir, this.map + (lane-> treatment) )
  }

  def removeTreatment(lane: Lane) = {
    RoadTreatment(this.roadId, this.dir, this.map - lane)
  }
}

object RoadTreatment{
  def apply(roadId: Long, dir: String, map: Map[Lane, Treatment]): RoadTreatment = new RoadTreatment(roadId, dir, map)

  import reactivemongo.play.json.BSONFormats.BSONObjectIDFormat // This is required
  implicit def treatmentDetailFormat = Json.format[TreatmentDetail]
  implicit def treatmentFormat = Json.format[Treatment]

  implicit val mapReads: Reads[Map[Lane, Treatment]] = new Reads[Map[Lane, Treatment]] {
    def reads(jv: JsValue): JsResult[Map[Lane, Treatment]] =
      JsSuccess(jv.as[Map[String, Treatment]].map{case (k, v) =>
        Json.parse(k).as[Lane] -> v .asInstanceOf[Treatment]
      })
  }

  implicit val mapWrites: Writes[Map[Lane, Treatment]] = new Writes[Map[Lane, Treatment]] {
    def writes(map: Map[Lane, Treatment]): JsValue =
      Json.obj(map.map{case (s, o) =>
        val ret: (String, JsValueWrapper) = s.toString -> Json.toJson(o)
        ret
      }.toSeq:_*)
  }

  implicit val mapFormat: Format[Map[Lane, Treatment]] = Format(mapReads, mapWrites)

  implicit def roadTreatmentFormat = Json.format[RoadTreatment]

}
