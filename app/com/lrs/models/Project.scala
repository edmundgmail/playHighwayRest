package com.lrs.models

import com.lrs.daos.core.TemporalModel
import org.joda.time.DateTime
import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID
/**
  * Created by vagrant on 11/16/17.
  */

case class Project(projectId: Long, projectName: String, roadName: String, roadId: Long, projectType: String, projectCode: String, federalNumber: String, stateNumber: String, program: String, cost: Double, completionDate: String, projectManager: String,                 var _id: Option[BSONObjectID] = None,
                   var created: Option[DateTime] = None,
                   var updated: Option[DateTime] = None) extends TemporalModel


object Project{
  import reactivemongo.play.json.BSONFormats.BSONObjectIDFormat // This is required
  implicit def projectFormat = Json.format[Project]

}


