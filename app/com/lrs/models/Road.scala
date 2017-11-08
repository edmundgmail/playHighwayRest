package com.lrs.models

import com.lrs.daos.core.TemporalModel
import com.lrs.logging.Logging
import com.lrs.models.DataRecords.{AddRoadRecord, PointRecord}
import org.joda.time.DateTime
import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID
import user.User

/**
  * Created by eguo on 8/26/17.
  */
case class Road(val name:String, val roadId: Long, val mainDir: String,
                val jurisDictionType:String, val ownerShip:String, val prefixCode:String,
                val routeNumber:String, val modifierCode:String, val mainlineCode:String, val routeTypeCode:String, val routeOfficialName:String,
                val routeFullName:String, val routeAlternateName:String, val beginPlace:String, val endPlace:String,
                val directions: List[Direction] = List.empty,
                var _id: Option[BSONObjectID] = None,
                var created: Option[DateTime] = None,
                var updated: Option[DateTime] = None) extends TemporalModel with Logging{
  def withUpdatedDirections(newDirections:List[Direction]) = Road(name , roadId, mainDir, jurisDictionType, ownerShip, prefixCode,
  routeNumber, modifierCode, mainlineCode, routeTypeCode, routeOfficialName,
  routeFullName, routeAlternateName, beginPlace, endPlace,newDirections)

  override def toString: String = {
    s"{RoadName: $name mainDir:$mainDir directions: $directions"
  }

  def getRps(dir: String) : List[ReferencePoint] = ???

  @throws(classOf[Exception])
  def removeSegment(dir:String, startPoint:PointRecord, endPoint:PointRecord) = {
      //logger.info("trying to removeSegment")
      val startRP = ReferencePoint(startPoint.rpName, name, dir,0,0)
      val endRP = ReferencePoint(endPoint.rpName, name, dir, 0, 0)
      val dirs = directions.filterNot(_.dir==dir) ++ directions.filter(_.dir==dir).map(d=>d.removeSegment(SegmentPoint("start", startRP.ID, startPoint.offset), SegmentPoint("end", endRP.ID, endPoint.offset)))
      Road(name, roadId, mainDir, jurisDictionType, ownerShip, prefixCode,
        routeNumber, modifierCode, mainlineCode, routeTypeCode, routeOfficialName,
        routeFullName, routeAlternateName, beginPlace, endPlace,dirs)
  }

  def addSegment(dir:String, segment:String, afterRPName:String, leftConnect:Boolean, beforeRPName:String, rightConnect:Boolean) = {
    val afterRP = ReferencePoint(afterRPName, name, dir,0,0)
    val beforeRP = ReferencePoint(beforeRPName, name, dir, 0, 0)
    val dirs = directions.filterNot(_.dir==dir) ++ directions.filter(_.dir==dir).map(
      d=>d.addSegmentString(name, segment, Some(afterRP), leftConnect, Some(beforeRP), rightConnect))
    Road(name, roadId, mainDir, jurisDictionType, ownerShip, prefixCode,
      routeNumber, modifierCode, mainlineCode, routeTypeCode, routeOfficialName,
      routeFullName, routeAlternateName, beginPlace, endPlace,dirs)
  }

  def getSegmentString(dir: String, start: PointRecord, end: PointRecord) : String = ???

 def updateLane(dir: String, lane: String)  = {
   val dirs = directions.filterNot(_.dir==dir) ++ directions.filter(_.dir==dir).map(
     d=>d.updateLane(lane))
   Road(name, roadId, mainDir, jurisDictionType, ownerShip, prefixCode,
     routeNumber, modifierCode, mainlineCode, routeTypeCode, routeOfficialName,
     routeFullName, routeAlternateName, beginPlace, endPlace,dirs)
 }

}

object Road{
  def fromJson(record: AddRoadRecord) : Road = {
    Road(record.roadName, record.roadId, record.mainDir, record.jurisDictionType, record.ownerShip, record.prefixCode,
      record.routeNumber, record.modifierCode, record.mainlineCode, record.routeTypeCode, record.routeOfficialName,
      record.routeFullName, record.routeAlternateName, record.beginPlace, record.endPlace,
      record.directions.map(d=>Direction.fromString(record.roadName, d.dir, d.segments.toList)).toList)
  }

  import reactivemongo.play.json.BSONFormats.BSONObjectIDFormat // This is required
  implicit val roadFormat = Json.format[Road]
}