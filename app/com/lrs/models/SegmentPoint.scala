package com.lrs.models

import com.lrs.utils.AssertException
import play.api.libs.json.Json
import  com.lrs.utils.MyImplicits._
/**
  * Created by vagrant on 11/9/17.
  */
case class SegmentPoint(val name: String, referencePoint: Int, offset: Double, override val x: Double = 0, override val y:Double = 0, override val z:Double = 0) extends Point{
  override def toString: String = {
    s"SegmentPoint RP = ${referencePoint} offset=${offset}"
  }

  def useNext(rps: List[ReferencePoint]):SegmentPoint = {
    val i = ReferencePoint.findIDIndex(referencePoint, rps)
    AssertException(i>=0)
    if(i>=rps.length - 1) this
    else  {
      AssertException(rps(i).distance>0)
      SegmentPoint(SegmentPoint.generateName(rps(i+1).name, offset - rps(i).distance), rps(i+1).ID, offset - rps(i).distance, x,y,z)
    }
  }

  def usePrev(rps:List[ReferencePoint]) : SegmentPoint= {
    val i = ReferencePoint.findIDIndex(referencePoint, rps)
    AssertException(i>=0)
    if(i==0) this
    else {
      AssertException(rps(i-1).distance>0)
      SegmentPoint(SegmentPoint.generateName(rps(i+1).name,offset + rps(i-1).distance), rps(i-1).ID, offset + rps(i-1).distance, x,y,z)
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