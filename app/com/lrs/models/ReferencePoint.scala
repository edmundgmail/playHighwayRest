package com.lrs.models

import play.api.libs.json.Json

import scala.util.hashing.MurmurHash3
import  com.lrs.utils.MyImplicits._

/**
  * Created by vagrant on 11/9/17.
  */
case class ReferencePoint(val rpId: Long, val name: String, val roadName:String, val dir:String, val globalOffset: Double, val distance: Double, override val x: Double = 0, override val y:Double = 0, override val z:Double = 0, val rpID: Long= 0) extends Point
{
  def withIncrementOffset(offset:Double) = ReferencePoint(rpId, name, roadName, dir, globalOffset+offset, distance, x,y,z)
  def withDistance(d:Double) = ReferencePoint(rpId, name, roadName, dir, globalOffset, d, x,y,z)
  def ID = MurmurHash3.stringHash((roadName, dir, name).toString)

  override def toString: String = {
    s"{RP name=${name} distance=${distance} globaloffset=${globalOffset}}"
  }

  override def equals(obj: Any): Boolean = {
    obj match {
      case that:ReferencePoint => super.equals(that) && that.globalOffset=~=this.globalOffset && that.distance=~=this.distance
      case  _=> false
    }
  }

  def same(that:ReferencePoint) : Boolean = this.ID == that.ID
}

object ReferencePoint{
  def findIndex(rps:List[ReferencePoint], rp:ReferencePoint) : Int= {
    val f = rps.zipWithIndex.filter(r=>rp.same(r._1))
    if(f.isEmpty) -1
    else f(0)._2
  }

  def findIDIndex(ID:Int, rps:List[ReferencePoint]):Int = {
    val rpIDs = rps.zipWithIndex.filter(_._1.ID == ID)
    if(rpIDs.isEmpty) -1
    else rpIDs(0)._2
  }

  def getByID(ID:Int, rps:List[ReferencePoint]) : Option[ReferencePoint] = {
    val rpIDs = rps.filter(_.ID==ID)
    if(rpIDs.isEmpty) None
    else Some(rpIDs(0))
  }

  def getByName(rps:List[ReferencePoint], rpName: String) : Option[ReferencePoint] = {
    val ret = rps.filter(_.name==rpName)
    if(ret.isEmpty) None
    else Some(ret(0))
  }

  def ID(rpId: Long, name: String, roadName:String, dir: String) = rpId match {
    case 0 => MurmurHash3.stringHash((name, roadName, dir).toString).toLong
    case _ => rpId
  }


  def apply(name: String, roadName:String, dir:String, globalOffset: Double, distance: Double) = {
    new ReferencePoint(ID(0, name, roadName, dir), name, roadName, dir, globalOffset, distance)
  }

  def apply(rpId: Long, name: String, roadName:String, dir:String, globalOffset: Double, distance: Double) = {
    new ReferencePoint(ID(rpId, name, roadName, dir), name, roadName, dir, globalOffset, distance)
  }

  import reactivemongo.play.json.BSONFormats.BSONObjectIDFormat // This is required
  implicit val rpFormat = Json.format[ReferencePoint]
}
