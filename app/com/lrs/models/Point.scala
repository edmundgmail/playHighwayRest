package com.lrs.models

import com.lrs.utils.AssertException

import scala.util.hashing.MurmurHash3
import com.lrs.utils.MyImplicits._
import play.api.libs.json.Json

/**
  * Created by eguo on 8/26/17.
  */
class Point (val x: Double, val y:Double, val z:Double){
  override def equals(obj: scala.Any): Boolean = {
    obj match {
      case that:Point => that.x =~= this.x && that.y =~= this.y && that.z =~= this.z
      case _=> false
    }
  }
}

case class SegmentPoint(val name: String, referencePoint: Int, offset: Double, override val x: Double = 0, override val y:Double = 0, override val z:Double = 0) extends Point(x,y,z){
  override def toString: String = {
    s"SegmentPoint RP = ${referencePoint} offset=${offset}"
  }

  def useNext(rps: List[ReferencePoint]):SegmentPoint = {
      val i = ReferencePoint.findIDIndex(referencePoint, rps)
      AssertException(i>=0)
      if(i>=rps.length - 1) this
      else  {
        AssertException(rps(i).distance>0)
        SegmentPoint(name, rps(i+1).ID, offset - rps(i).distance, x,y,z)
      }
  }

  def usePrev(rps:List[ReferencePoint]) : SegmentPoint= {
    val i = ReferencePoint.findIDIndex(referencePoint, rps)
    AssertException(i>=0)
    if(i==0) this
    else {
      AssertException(rps(i-1).distance>0)
      SegmentPoint(name, rps(i-1).ID, offset + rps(i-1).distance, x,y,z)
    }
  }

  override def equals(obj: Any): Boolean = {
    obj match {
      case that:SegmentPoint => this.referencePoint == that.referencePoint && that.offset =~= this.offset
      case _ => false
    }
  }
}

object SegmentPoint{
  import reactivemongo.play.json.BSONFormats.BSONObjectIDFormat // This is required
  implicit val segpFormat = Json.format[SegmentPoint]

  def generateName(name: String, offset:Double ) = s"${name}@${offset}"
}

case class ReferencePoint(val name: String, val roadName:String, val dir:String, val globalOffset: Double, val distance: Double, override val x: Double = 0, override val y:Double = 0, override val z:Double = 0) extends Point(x, y, z)
{
  def withIncrementOffset(offset:Double) = ReferencePoint(name, roadName, dir, globalOffset+offset, distance, x,y,z)
  def withDistance(d:Double) = ReferencePoint(name, roadName, dir, globalOffset, d, x,y,z)
  def ID =MurmurHash3.stringHash((roadName, dir, name).toString)

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

  import reactivemongo.play.json.BSONFormats.BSONObjectIDFormat // This is required
  implicit val rpFormat = Json.format[ReferencePoint]
}



