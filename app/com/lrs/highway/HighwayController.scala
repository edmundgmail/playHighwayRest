package com.lrs.highway

import javax.inject.Inject

import com.lrs.daos.core.ContextHelper
import com.lrs.daos.exceptions.ServiceException
import com.lrs.models.DataRecords._
import com.lrs.models._
import play.api.libs.json.{JsArray, JsObject, JsString, _}
import play.api.mvc._

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}



class HighwayController @Inject()(highwayService: HighwayService) extends Controller with ContextHelper {

  def handleException: PartialFunction[Throwable, Result] = {
    case e : ServiceException => BadRequest(e.message)
    case t: Throwable =>   {t.printStackTrace; BadRequest(t.getMessage)}
    case _ => BadRequest("Unknown Exception")
  }


  def createProject = Action.async(parse.json) { implicit request =>
    validateAndThen[Project] {
      entity => highwayService.createProject(entity).map {
        case Success(e) => Ok(Json.toJson(e))
      } recover handleException
    }
  }

  def getProjects =Action.async { implicit request =>
    highwayService.getProjects.map(
      projects => {
        val json = Json.toJson(projects)
        Ok(json)
      }
    )
  }

  def getProject(name: String) = Action.async { implicit request =>
    highwayService.getProject(name).map(
      project => {
        val json = Json.toJson(project)
        Ok(json)
      }
    )
  }

  def createRamp = Action.async(parse.json) { implicit request =>
    validateAndThen[Ramp] {
      entity => highwayService.createRamp(entity).map {
        case Success(e) => Ok(Json.toJson(e))
      } recover handleException
    }
  }

  def getRamps =Action.async { implicit request =>
    highwayService.getRamps.map(
      ramps => {
        val json = Json.toJson(ramps)
        Ok(json)
      }
    )
  }

  def getRamp(id:Long) = Action.async { implicit request =>
    highwayService.getRamp(id).map(
      ramp => {
        val json = Json.toJson(ramp)
        Ok(json)
      }
    )
  }


  def createCouplet = Action.async(parse.json) { implicit request =>
    validateAndThen[Couplet] {
      entity => highwayService.createCouplet(entity).map {
        case Success(e) => Ok(Json.toJson(e))
      } recover handleException
    }
  }

  def getCouplets =Action.async { implicit request =>
    highwayService.getCouplets.map(
      couplets => {
        val json = Json.toJson(couplets)
        Ok(json)
      }
    )
  }

  def getCouplet(name: String) = Action.async { implicit request =>
    highwayService.getCouplet(name).map(
      couplet => {
        val json = Json.toJson(couplet)
        Ok(json)
      }
    )
  }



  def getall = Action.async {implicit request =>
      highwayService.getall.map(
        roads=> {
          val json = Json.toJson(roads)
          Ok(json)
        }
      )
  }

  def getRPs(id: Long, dir:String) = Action.async {implicit request =>
    highwayService.getRPs(id, dir).map(
      roads=> {
        val json = Json.toJson(roads)
        Ok(json)
      }
    )
  }

  def getSegmentStartRPs(id: Long, dir:String) = Action.async {implicit request =>
    highwayService.getSegmentStartRPs(id, dir).map(
      roads=> {
        val json = Json.toJson(roads)
        Ok(json)
      }
    )
  }

  def getSegmentEndRPs(id: Long, dir:String) = Action.async {implicit request =>
    highwayService.getSegmentEndRPs(id, dir).map(
      roads=> {
        val json = Json.toJson(roads)
        Ok(json)
      }
    )
  }


  def get(id: Long) = Action.async {implicit request =>
    highwayService.get(id).map(
      road=>{
        val json = Json.toJson(road.get)
        Ok(json)
      }
    )
  }

  def createFeature = Action.async(parse.json) {implicit request =>
    validateAndThen[RoadFeatureRecord] {
      entity => highwayService.createFeature(entity).map {
        case Success(e) => Ok(Json.toJson(e))
      } recover handleException
    }
  }

  def getFeature(id: Long, dir: String) = Action.async {implicit request =>
    highwayService.getFeature(id, dir).map(
      feature=>{
        val json = Json.toJson(feature.get)
        Ok(json)
      }
    )
  }

  def createAttributes = Action.async(parse.json) {implicit request =>
      highwayService.createAttributes
      Future.successful(Ok("completed"))
  }

  def getAttribute(catid: Long, attrid: Long) = Action.async {implicit request =>
      highwayService.getAttribute(catid,attrid).map(
        res => Ok(Json.toJson(res))
      )
  }

  def create = Action.async(parse.json) { implicit request =>
    validateAndThen[RawDataRecord] {
        entity=> entity.action match {
          case "AddRoadRecord" => validateAndThen[AddRoadRecord](handleEntity(_))
          case "RemoveSegmentRecord" => validateAndThen[RemoveSegmentRecord](handleEntity(_))
          case "AddSegmentRecord" => validateAndThen[AddSegmentRecord](handleEntity(_))
          case "UpdateLaneRecord" =>   validateAndThen[UpdateLaneRecord](handleEntity(_))
          case "TransferSegmentRecord" =>   validateAndThen[TransferSegmentRecord](handleEntity(_))
        }
    }
  }

  private def handleEntity(entity: DataRecord) = {
    highwayService.handleHighwayRecord(entity).map {
      case Success(e) => Ok(Json.toJson(e))
    } recover handleException
  }

  def validateAndThen[T: Reads](t: T => Future[Result])(implicit request: Request[JsValue]) = {
    request.body.validate[T].map(t) match {
      case JsSuccess(result, _) => result
      case JsError(err) => Future.successful(BadRequest(Json.toJson(err.map {
        case (path, errors) => Json.obj("path" -> path.toString, "errors" -> JsArray(errors.flatMap(e => e.messages.map(JsString(_)))))
      })))
    }
  }

  def toError(t: (String, Try[JsValue])): JsObject = t match {
    case (paramName, Failure(e)) => Json.obj(paramName -> e.getMessage)
    case _ => Json.obj()
  }
 }
