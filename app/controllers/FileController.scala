package controllers

import akka.actor.ActorSystem
import javax.inject.Inject
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}

import scala.concurrent.ExecutionContext

class FileController @Inject()(cc: ControllerComponents, actorSystem: ActorSystem, fileServer: services.fileServer)(implicit exec: ExecutionContext) extends AbstractController(cc) {


  def listObjects(username: String) = Action { implicit request =>

    if (checkSessionIdStatus(request)) {
      Ok(Json.toJson(fileServer.listObjects(username)))
    } else {
      Unauthorized("Oops, you are not authorised")
    }
  }


  def getAccessRequests(username: String) = Action { request =>
    if (checkSessionIdStatus(request)) {
      Ok(Json.toJson(fileServer.getAccessRequests(username)))
    } else {
      Unauthorized("Oops, you are not authorised")
    }


  }

  def getAccessWaitingForApprovals(owner: String) = Action { request =>
    if (checkSessionIdStatus(request)) {
      Ok(Json.toJson(fileServer.getAccessWaitingForApprovals(owner)))
    } else {
      Unauthorized("Oops, you are not authorised")
    }


  }

  def getAccessedRecords(username: String) = Action { request =>

    if (checkSessionIdStatus(request)) {
      Ok(Json.toJson(fileServer.getAccessedRecords(username)))
    } else {
      Unauthorized("Oops, you are not authorised")
    }


  }


  def getObject(key: String) = Action { request =>
    if (checkSessionIdStatus(request)) {
      Ok(fileServer.getObject(key))
    } else {
      Unauthorized("Oops, you are not authorised")
    }

  }

  def uploadObject() = Action { request =>

    if (checkSessionIdStatus(request)) {
      val json = request.body.asJson.get
      if (fileServer.uploadObject(json)) {
        Ok("Success")
      } else {
        Ok("Error")
      }
    } else {
      Unauthorized("Oops, you are not authorised")
    }
  }

  def setAccessRequestsStatus() = Action { request =>
    if (checkSessionIdStatus(request)) {
      val json = request.body.asJson.get
      if (fileServer.setAccessRequestsStatus(json)) {
        Ok("Success")
      } else {
        Ok("Error")
      }
    } else {
      Unauthorized("Oops, you are not authorised")
    }
  }


  def deleteObject() = Action { request =>

    if (checkSessionIdStatus(request)) {
      val json = request.body.asJson.get

      if (fileServer.deleteObject(json)) {
        Ok("Success")
      } else {
        Ok("Error")
      }
    } else {
      Unauthorized("Oops, you are not authorised")
    }
  }

  def deleteRecord() = Action { request =>

    if (checkSessionIdStatus(request)) {
      val json = request.body.asJson.get

      if (fileServer.deleteRecord(json)) {
        Ok("Success")
      } else {
        Ok("Error")
      }
    } else {
      Unauthorized("Oops, you are not authorised")
    }
  }


  def lockObject() = Action { request =>

    if (checkSessionIdStatus(request)) {
      val json = request.body.asJson.get

      if (fileServer.lockObject(json)) {
        Ok("Success")
      } else {
        Ok("Error")
      }
    } else {
      Unauthorized("Oops, you are not authorised")
    }

  }

  def lockStatus() = Action { request =>

    if (checkSessionIdStatus(request)) {
      val json = request.body.asJson.get

      if (fileServer.lockStatus(json)) {
        Ok("Success")
      } else {
        Ok("Error")
      }
    } else {
      Unauthorized("Oops, you are not authorised")
    }

  }

  def sendAccessRequest(path: String, username: String, access: String, owner: String) = Action { request =>

    if (checkSessionIdStatus(request)) {
      if (fileServer.sendAccessRequest(path, username, access, owner)) {
        Ok("Success")
      } else {
        Ok("Error")
      }
    } else {
      Unauthorized("Oops, you are not authorised")
    }

  }

  def logOut(username: String) = Action { request =>
    if (checkSessionIdStatus(request)) {
      if (fileServer.logOut(username)) {
        Ok("Success").withNewSession
      } else {
        Ok("Error")
      }
    } else {
      Unauthorized("Oops, you are not authorised")
    }

  }


  def checkSessionIdStatus(implicit request: Request[AnyContent]): Boolean = {
    var exists: Boolean = false;
    var userName = ""
    var sessionId = ""
    request.session.data.map { data =>
      if (data._1 == "username") userName = data._2 else sessionId = data._2
    }
    exists = fileServer.checkSessionIdStatus(userName, sessionId)
    exists
  }





}
