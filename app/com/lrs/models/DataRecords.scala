package com.lrs.models

import com.lrs.models.RoadFeatureObj.RoadFeature
import play.api.libs.json._

/**
  * Created by vagrant on 8/18/17.
  */
import reactivemongo.play.json.BSONFormats.BSONObjectIDFormat // This is required

object DataRecords{

    class DataRecord(val action:String, val dateTime: String , val roadId: Long)

  /*for parsing the record*/
    case class RawDataRecord(val action:String, val dateTime: String , val roadId: Long)
    implicit def rawDataRecordFormat = Json.format[RawDataRecord]

    case class PointRecord(val rpName:String, val offset:Double)
    implicit def pointRecordFormat = Json.format[PointRecord]

    case class SegmentRecord(val start: PointRecord, val end: PointRecord)
    implicit def segmentRecordFormat = Json.format[SegmentRecord]

    case class DirectionRecord(val dir: String,val segments: Array[String])
    implicit def directionRecordFormat = Json.format[DirectionRecord]

    case class AddRoadRecord(override val action: String, override val dateTime: String, override val roadId: Long,
                             val roadName:String, val mainDir: String, val jurisdictionType:String, val ownerShip:String, val prefixCode:String,
                             val routeNumber:String, val modifierCode:String, val mainlineCode:String, val routeTypeCode:String, val routeOfficialName:String,
                             val routeFullName:String, val routeAlternateName:String, val beginPlace:String, val endPlace:String,
                             val directions: Array[DirectionRecord]) extends DataRecord(action, dateTime, roadId)
    implicit def addRoadRecordFormat = Json.format[AddRoadRecord]

    case class RemoveSegmentRecord(override val action: String, override val dateTime: String, override val roadId: Long, val dir:String, val startPoint: PointRecord, val endPoint:PointRecord)
      extends DataRecord(action, dateTime, roadId)

    implicit def removeSegmentRecordFormat = Json.format[RemoveSegmentRecord]

    case class AddSegmentRecord(override val action: String, override val dateTime: String, override val roadId: Long, val dir:String, val segment:String, val leftConnect : Boolean,
                                val afterRP: String, val rightConnect: Boolean, val beforeRP:String ) extends DataRecord(action, dateTime, roadId)

    implicit def addSegmentRecordFormat = Json.format[AddSegmentRecord]
    /*rp1,offset1, rp2,offset2, 1, in*/
    /*rp1,offset1, rp2,offset2, 2, out*/
    case class UpdateLaneRecord(override val action:String, override val dateTime: String, override val roadId: Long, val dir:String, val lane: String) extends DataRecord(action, dateTime, roadId)

    implicit def updateLaneRecordFormat = Json.format[UpdateLaneRecord]

    case class TransferSegmentRecord(override val action: String, override val dateTime: String, val fromRoadId: Long, val fromDir: String, val startPoint: PointRecord,
                                     val endPoint: PointRecord, val toRoadId: Long, val toDir: String, val afterRP: String, val beforeRP: String,
                                     leftConnect: Boolean, rightConnect: Boolean) extends DataRecord(action, dateTime, fromRoadId)

    implicit def transferSegmentRecordFormat = Json.format[TransferSegmentRecord]

    case class AddRoadFeaturesRecord(override val action: String, override val dateTime: String, override val roadId: Long, val dir: String,
                                    val segments: List[SegmentRecord], val roadFeature: RoadFeature) extends DataRecord(action, dateTime, roadId )

    implicit def addRoadFeatureRecordFormat = Json.format[AddRoadFeaturesRecord]

    case class RemoveRoadFeaturesRecord(override val action: String, override val dateTime: String, override val roadId: Long, val dir: String,
                                     val segment: SegmentRecord) extends DataRecord(action, dateTime, roadId )

    implicit def removeRoadFeaturesRecordFormat = Json.format[RemoveRoadFeaturesRecord]

}