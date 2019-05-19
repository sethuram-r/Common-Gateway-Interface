package services

import com.fasterxml.jackson.databind.JsonNode
import javax.inject.Inject
import play.api.libs.json.JsValue
import play.libs.ws._
import play.api.Configuration

class Authenticator @Inject()(val ws: WSClient, config: Configuration) extends WSBodyReadables with WSBodyWritables {

  val applicationHostAddress = config.get[String]("application.hostname")

  def signIn(data: JsValue): JsonNode = {
    val url = "http://" + applicationHostAddress + ":5001/signIn"
      val request: WSRequest = ws.url(url)
      val complexRequest: WSRequest = request.addHeader("Accept", "application/json")
    val futureResponse = complexRequest.post(data.toString())
      Thread.sleep(1000)
    return futureResponse.toCompletableFuture.get().asJson()
  }

  def signUp(data: JsValue): Boolean = {
    var url = "http://" + applicationHostAddress + ":5001/signUp"
    val request: WSRequest = ws.url(url)
    val complexRequest: WSRequest = request.addHeader("Accept", "application/json")
    val futureResponse = complexRequest.post(data.toString())
    Thread.sleep(1000)
    return futureResponse.toCompletableFuture.get().asJson().get("status").asBoolean()
  }
}
