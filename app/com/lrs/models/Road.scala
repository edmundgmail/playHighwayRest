package com.lrs.models

import com.lrs.daos.core.TemporalModel
import com.lrs.models.DataRecords.{AddRoadRecord, PointRecord}
import org.joda.time.DateTime
import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID

import scala.util.hashing.MurmurHash3

/**
  * Created by eguo on 8/26/17.
  */
case class Road(val roadName:String, val roadId: Long, val mainDir: String,
                val jurisdictionType:String, val ownerShip:String, val prefixCode:String,
                val routeNumber:String, val modifierCode:String, val mainlineCode:String, val routeTypeCode:String, val routeOfficialName:String,
                val routeFullName:String, val routeAlternateName:String, val beginPlace:String, val endPlace:String,
                val directions: List[Direction] = List.empty,
                var _id: Option[BSONObjectID] = None,
                var created: Option[DateTime] = None,
                var updated: Option[DateTime] = None) extends TemporalModel{
  def withUpdatedDirections(newDirections:List[Direction]) = Road(roadName , roadId, mainDir, jurisdictionType, ownerShip, prefixCode,
  routeNumber, modifierCode, mainlineCode, routeTypeCode, routeOfficialName,
  routeFullName, routeAlternateName, beginPlace, endPlace,newDirections)

  override def toString: String = {
    s"{RoadName: $roadName mainDir:$mainDir directions: $directions"
  }

  def getRps(dir: String) : List[ReferencePoint] = ???

  @throws(classOf[Exception])
  def removeSegment(dir:String, startPoint:PointRecord, endPoint:PointRecord) = {
      //logger.info("trying to removeSegment")
      val startRP = ReferencePoint(startPoint.rpName, roadName, dir,0,0)
      val endRP = ReferencePoint(endPoint.rpName, roadName, dir, 0, 0)
      val dirs = directions.filterNot(_.dir==dir) ++ directions.filter(_.dir==dir).map(
        d=>d.removeSegment(SegmentPoint(SegmentPoint.generateName(startRP.name, startPoint.offset), startRP.ID, startPoint.offset),
          SegmentPoint(SegmentPoint.generateName(endRP.name, endPoint.offset), endRP.ID, endPoint.offset)))
      Road(roadName, roadId, mainDir, jurisdictionType, ownerShip, prefixCode,
        routeNumber, modifierCode, mainlineCode, routeTypeCode, routeOfficialName,
        routeFullName, routeAlternateName, beginPlace, endPlace,dirs,
      _id)
  }

  def addSegment(dir:String, segment:String, afterRPName:String, leftConnect:Boolean, beforeRPName:String, rightConnect:Boolean) = {
    val afterRP = ReferencePoint(afterRPName, roadName, dir,0,0)
    val beforeRP = ReferencePoint(beforeRPName, roadName, dir, 0, 0)
    val dirs = directions.filterNot(_.dir==dir) ++ directions.filter(_.dir==dir).map(
      d=>d.addSegmentString(roadName, segment, Some(afterRP), leftConnect, Some(beforeRP), rightConnect))
    Road(roadName, roadId, mainDir, jurisdictionType, ownerShip, prefixCode,
      routeNumber, modifierCode, mainlineCode, routeTypeCode, routeOfficialName,
      routeFullName, routeAlternateName, beginPlace, endPlace,dirs,_id)
  }

  def getSegmentString(dir: String, start: PointRecord, end: PointRecord) : String = ???

 def updateLanes(dir: String, lanes: List[String])  = {
   val dirs = directions.filterNot(_.dir==dir) ++ directions.filter(_.dir==dir).map(
     d=>d.updateLanes(lanes))
   Road(roadName, roadId, mainDir, jurisdictionType, ownerShip, prefixCode,
     routeNumber, modifierCode, mainlineCode, routeTypeCode, routeOfficialName,
     routeFullName, routeAlternateName, beginPlace, endPlace,dirs,_id)
 }
}

object Road{

  private def ID(roadId: Long, roadName: String) =
    roadId match {
    case 0 => MurmurHash3.stringHash(roadName).toLong
    case _ => roadId
  }


  def apply(roadName:String, roadId: Long, mainDir: String): Road = {
    new Road(roadName, ID(roadId, roadName), mainDir, "", "", "", "", "", "", "", "", "", "", "", "")
  }

  def fromJson(record: AddRoadRecord) : Road = {
    Road(record.roadName, ID(record.roadId, record.roadName), record.mainDir, record.jurisdictionType, record.ownerShip, record.prefixCode,
      record.routeNumber, record.modifierCode, record.mainlineCode, record.routeTypeCode, record.routeOfficialName,
      record.routeFullName, record.routeAlternateName, record.beginPlace, record.endPlace,
      record.directions.map(d=>Direction.fromString(record.roadName, d.dir, d.segments.toList)).toList)
  }

  import reactivemongo.play.json.BSONFormats.BSONObjectIDFormat // This is required
  implicit val roadFormat = Json.format[Road]
}