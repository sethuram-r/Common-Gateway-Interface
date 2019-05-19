package services

import com.fasterxml.jackson.databind.JsonNode
import javax.inject.Inject
import play.api.Configuration
import play.api.libs.json.JsValue
import play.libs.ws.{WSBodyReadables, WSBodyWritables, WSClient, WSRequest}

class fileServer @Inject()(val ws: WSClient, config: Configuration) extends WSBodyReadables with WSBodyWritables {

  val applicationHostAddress = config.get[String]("application.hostname")

  def checkSessionIdStatus(userName: String, sessionId: String): Boolean = {

    val url = "http://" + applicationHostAddress + ":5001/validateSession"
    val request: WSRequest = ws.url(url).addQueryParameter("username", userName).addQueryParameter("sessionid", sessionId)
    val complexRequest: WSRequest = request.addHeader("Accept", "application/json")
    val futureResponse = complexRequest.get()
    Thread.sleep(1000)
    return (futureResponse.toCompletableFuture.get().asJson().get("status").asBoolean())
  }

  def listObjects(username: String): JsonNode = {
    val url = "http://" + applicationHostAddress + ":5002/displayCurrentFilesandFoldersForSelectedTopics"
    val request: WSRequest = ws.url(url).addQueryParameter("username", username)
    val complexRequest: WSRequest = request.addHeader("Accept", "application/json")
    val futureResponse = complexRequest.get()
    Thread.sleep(1000)
    return (futureResponse.toCompletableFuture.get().asJson())
  }

  def getAccessRequests(username: String): JsonNode = {
    val url = "http://" + applicationHostAddress + ":5005/getAccessRequestsCreatedByUser"
    val request: WSRequest = ws.url(url).addQueryParameter("username", username)
    val complexRequest: WSRequest = request.addHeader("Accept", "application/json")
    val futureResponse = complexRequest.get()
    Thread.sleep(1000)
    return (futureResponse.toCompletableFuture.get().asJson())
  }

  def getAccessWaitingForApprovals(owner: String): JsonNode = {
    val url = "http://" + applicationHostAddress + ":5005/getAccessRequestsToBeApprovedByOwnerOfTheFile"
    val request: WSRequest = ws.url(url).addQueryParameter("owner", owner)
    val complexRequest: WSRequest = request.addHeader("Accept", "application/json")
    val futureResponse = complexRequest.get()
    Thread.sleep(1000)
    return (futureResponse.toCompletableFuture.get().asJson())
  }

  def getAccessedRecords(username: String): JsonNode = {
    val url = "http://" + applicationHostAddress + ":5005/getListOfUsersAccessingOwnersFiles"
    val request: WSRequest = ws.url(url).addQueryParameter("username", username)
    val complexRequest: WSRequest = request.addHeader("Accept", "application/json")
    val futureResponse = complexRequest.get()
    Thread.sleep(1000)
    return (futureResponse.toCompletableFuture.get().asJson())
  }

  def getObject(key: String): String = {
    val url = "http://" + applicationHostAddress + ":5002/downloadSelectedFileOrFolders"
    val request: WSRequest = ws.url(url).addQueryParameter("key", key)
    val complexRequest: WSRequest = request.addHeader("Accept", "application/octet-stream")
    val futureResponse = complexRequest.get()
    Thread.sleep(100)
    return (futureResponse.toCompletableFuture.get().getBody)
  }


  def uploadObject(file: JsValue): Boolean = {
    val url = "http://" + applicationHostAddress + ":5004/uploadFileOrFolder"
    val request: WSRequest = ws.url(url)
    val complexRequest: WSRequest = request.addHeader("Accept", "application/json")
    val futureResponse = complexRequest.post(file.toString())
    Thread.sleep(3000)
    return (futureResponse.toCompletableFuture.get().asJson().get("status").asBoolean())
  }

  def setAccessRequestsStatus(file: JsValue): Boolean = {
    val url = "http://" + applicationHostAddress + ":5005/FileOwnerApproveOrRejectAccessRequest"
    val request: WSRequest = ws.url(url)
    val complexRequest: WSRequest = request.addHeader("Accept", "application/json")
    val futureResponse = complexRequest.post(file.toString())
    Thread.sleep(3000)
    return (futureResponse.toCompletableFuture.get().asJson().get("status").asBoolean())
  }


  def deleteObject(file: JsValue): Boolean = {
    val url = "http://" + applicationHostAddress + ":5004/deleteSelectedFileOrFolders"
    val request: WSRequest = ws.url(url)
    val complexRequest: WSRequest = request.addHeader("Accept", "application/json")
    val futureResponse = complexRequest.post(file.toString())
    Thread.sleep(2000)
    return (futureResponse.toCompletableFuture.get().asJson().get("status").asBoolean())
  }

  def deleteRecord(file: JsValue): Boolean = {
    val url = "http://" + applicationHostAddress + ":5005/deleteApprovedOrRejectedAccessRequest"
    val request: WSRequest = ws.url(url)
    val complexRequest: WSRequest = request.addHeader("Accept", "application/json")
    val futureResponse = complexRequest.post(file.toString())
    Thread.sleep(2000)
    return (futureResponse.toCompletableFuture.get().asJson().get("status").asBoolean())
  }

  def lockObject(file: JsValue): Boolean = {
    val url = "http://" + applicationHostAddress + ":5003/lockFileOrFolder"
    val request: WSRequest = ws.url(url)
    val complexRequest: WSRequest = request.addHeader("Accept", "application/json")
    val futureResponse = complexRequest.post(file.toString())
    Thread.sleep(2000)
    return (futureResponse.toCompletableFuture.get().asJson().get("status").asBoolean())
  }

  def lockStatus(file: JsValue): Boolean = {
    val url = "http://" + applicationHostAddress + ":5003/getCurrentLockStatusForSelectedFolder"
    val request: WSRequest = ws.url(url)
    val complexRequest: WSRequest = request.addHeader("Accept", "application/json")
    val futureResponse = complexRequest.post(file.toString())
    Thread.sleep(2000)
    return (futureResponse.toCompletableFuture.get().asJson().get("status").asBoolean())
  }

  def sendAccessRequest(path: String, username: String, access: String, owner: String): Boolean = {
    val url = "http://" + applicationHostAddress + ":5005/createAccessRequest"
    val request: WSRequest = ws.url(url).addQueryParameter("path", path).addQueryParameter("username", username).addQueryParameter("access", access).addQueryParameter("owner", owner)
    val complexRequest: WSRequest = request.addHeader("Accept", "application/octet-stream")
    val futureResponse = complexRequest.get()
    Thread.sleep(100)
    return (futureResponse.toCompletableFuture.get().asJson().get("status").asBoolean())
  }

  def logOut(username: String): Boolean = {
    val url = "http://" + applicationHostAddress + ":5001/logOut"
    val request: WSRequest = ws.url(url).addQueryParameter("username", username)
    val complexRequest: WSRequest = request.addHeader("Accept", "application/octet-stream")
    val futureResponse = complexRequest.get()
    Thread.sleep(100)
    return (futureResponse.toCompletableFuture.get().asJson().get("status").asBoolean())
  }

}
