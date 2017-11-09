package com.lrs.models
import com.lrs.utils.MyImplicits._

/**
  * Created by eguo on 8/26/17.
  */
trait Point{
  val x: Double
  val y:Double
  val z:Double

  override def equals(obj: scala.Any): Boolean = {
    obj match {
      case that:Point => that.x =~= this.x && that.y =~= this.y && that.z =~= this.z
      case _=> false
    }
  }
}






