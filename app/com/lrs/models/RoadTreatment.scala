package com.lrs.models

import com.lrs.daos.core.TemporalModel
import org.joda.time.DateTime
import reactivemongo.bson.BSONObjectID

/**
  * Created by vagrant on 10/18/17.
  */
object RoadTreatments{
    case class TreatmentDetail(layerNo: Int, material: String, materialDesign: String, thickness: Double)
    case class Treatment(desc: String, details: List[TreatmentDetail])

    class RoadTreatment(roadId: Long, dir: String, map: Map[Lane, Treatment],
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
    }
}
