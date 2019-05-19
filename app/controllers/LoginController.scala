package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import akka.actor.ActorSystem
import scala.concurrent.ExecutionContext
import play.api.libs.json.{JsObject, JsString, JsValue}

class LoginController @Inject()(cc: ControllerComponents, actorSystem: ActorSystem, autheicate: services.Authenticator)(implicit exec: ExecutionContext) extends AbstractController(cc) {

  def signUp = Action { implicit request =>
    var (userName, password) = extractingParametersFromRequest(request)

    val json: JsValue = JsObject(Seq(
      "username" -> JsString(userName),
      "password" -> JsString(password)
    )
    )
    if (autheicate.signUp(json)) {
      Ok("Legitamate User")
    }
    else {
      Status(403)("Forbidden")
    }
  }


  def signIn = Action { implicit request =>
    var (userName, password) = extractingParametersFromRequest(request)
    val initalJson: JsValue = JsObject(Seq(
      "username" -> JsString(userName),
      "password" -> JsString(password)
    )
    )
    val response = autheicate.signIn(initalJson)
    if (response.get("status").asBoolean()) {

      Ok("Legitamate User").withSession("username" -> userName, "sessionid" -> response.get("sessionid").asText())

    }
    else {
      Status(403)("Forbidden")
    }

  }


  def extractingParametersFromRequest(implicit request: Request[AnyContent]) = {
    var userName = ""
    var password = ""
    for (singleObject <- request.body.asFormUrlEncoded.get) {
      if (singleObject._1 == "username") userName = singleObject._2.head else password = singleObject._2.head
    }

    (userName, password)
  }


}
