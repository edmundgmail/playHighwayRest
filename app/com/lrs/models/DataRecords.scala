package com.lrs.models

import play.api.libs.json._

/**
  * Created by vagrant on 8/18/17.
  */
import reactivemongo.play.json.BSONFormats.BSONObjectIDFormat // This is required

object DataRecords{

    class DataRecord(val action:String, val dateTime: String , val roadId: Long)
    //implicit  val dataRecordFormat = Json.format[DataRecord]

    case class PointRecord(val rpName:String, val offset:Double)
    implicit  val pointRecordFormat = Json.format[PointRecord]

    case class SegmentRecord(val start: PointRecord, val end: PointRecord)
    implicit val segmentRecordFormat = Json.format[SegmentRecord]

    case class DirectionRecord(val dir: String,val segments: Array[String])
    implicit val directionRecordFormat = Json.format[DirectionRecord]

    case class AddRoadRecord(override val action: String, override val dateTime: String, override val roadId: Long,
                             val roadName:String, val mainDir: String, val jurisDictionType:String, val ownerShip:String, val prefixCode:String,
                             val routeNumber:String, val modifierCode:String, val mainlineCode:String, val routeTypeCode:String, val routeOfficialName:String,
                             val routeFullName:String, val routeAlternateName:String, val beginPlace:String, val endPlace:String,
                             val directions: Array[DirectionRecord]) extends DataRecord(action, dateTime, roadId)
    implicit val addRoadRecordFormat = Json.format[AddRoadRecord]

    case class RemoveSegmentRecord(override val action: String, override val dateTime: String, override val roadId: Long, val dir:String, val startPoint: PointRecord, val endPoint:PointRecord)
      extends DataRecord(action, dateTime, roadId)

    case class AddSegmentRecord(override val action: String, override val dateTime: String, override val roadId: Long, val dir:String, val segment:String, val leftConnect : Boolean,
                                val afterRP: String, val rightConnect: Boolean, val beforeRP:String ) extends DataRecord(action, dateTime, roadId)

    /*rp1,offset1, rp2,offset2, 1, in*/
    /*rp1,offset1, rp2,offset2, 2, out*/
    case class UpdateLaneRecord(override val action:String, override val dateTime: String, override val roadId: Long, val dir:String, val lane: String) extends DataRecord(action, dateTime, roadId)

    case class TransferSegmentRecord(override val action: String, override val dateTime: String, val fromRoadId: Long, val fromDir: String, val startPoint: PointRecord,
                                     val endPoint: PointRecord, val toRoadId: Long, val toDir: String, val afterRP: String, val beforeRP: String,
                                     leftConnect: Boolean, rightConnect: Boolean) extends DataRecord(action, dateTime, fromRoadId)

    case class AddRoadFeaturesRecord(override val action: String, override val dateTime: String, override val roadId: Long, val dir: String,
                                    val segments: List[SegmentRecord], val roadFeature: RoadFeature) extends DataRecord(action, dateTime, roadId )


    case class RemoveRoadFeaturesRecord(override val action: String, override val dateTime: String, override val roadId: Long, val dir: String,
                                     val segment: SegmentRecord) extends DataRecord(action, dateTime, roadId )


}