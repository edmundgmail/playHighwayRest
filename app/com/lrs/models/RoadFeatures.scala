package com.lrs.models

/**
  * Created by vagrant on 10/18/17.
  */

case class RoadFeature( COG: String,
                        county: String,
                        engineerDistrict: String,
                        cutAndGutter: String,
                        cutAndGutterLeftOrCard: Boolean)

case class RoadFeatures(roadId: Long, dir: String, segmentFeatures: Map[List[Segment], RoadFeature]) {
  def addFeature(rps: List[ReferencePoint], start: PointRecord, end: PointRecord, roadFeatures: List[RoadFeature]): RoadFeatures = ???
  def removeFeature(rps: List[ReferencePoint],  start: PointRecord, end: PointRecord) : RoadFeatures = ???
  def getFeatures(rps: List[ReferencePoint], start: PointRecord, end: PointRecord) : List[RoadFeature] = ???
}

object RoadFeatures{
  def getRoadFeatures(roadId: Long, dir: String) : RoadFeatures = ???
}