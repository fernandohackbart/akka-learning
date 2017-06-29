package org.biosphere.labs.akka.learning.actors

import akka.actor.{Actor, ActorLogging}
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, StatusCodes}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import org.biosphere.labs.akka.learning.actors.GreetingFetcherCommands.{FETCHGREETING, FETCHGREETINGPING}
import org.biosphere.labs.akka.learning.actors.ProductPersisterStatus.FAIL
import org.biosphere.labs.akka.learning.utils.{GreetingResponse, Protocols}

import scala.concurrent.Await
import scala.concurrent.duration._

object GreetingFetcherCommands {
  case object FETCHGREETING
  case object FETCHGREETINGPING
}

class GreetingFetcher extends Actor with ActorLogging with Protocols with SprayJsonSupport{

  import context.dispatcher

  implicit val materializer: ActorMaterializer = ActorMaterializer()

  def receive = {
    case FETCHGREETING =>
      log.info("GreetingFetcher FETCHGREETING")
      val s = sender
      val http = Http(context.system)
      val reqResp = http.singleRequest(HttpRequest(uri = "http://echo.jsontest.com/key/value/one/two"))
      val result = reqResp map {
        _ match {
          case HttpResponse(StatusCodes.OK, headers, entity, _) =>
            log.info("GreetingFetcher HttpResponse.OK")

            val resltFuture = Unmarshal(entity).to[GreetingResponse]
            val result = Await.result(resltFuture, 1.second)
            if (result.isInstanceOf[GreetingResponse]) {
              log.info(s"GreetingFetcher entity.isInstanceOf[GreetingResponse] Values: ${result.one}-${result.key} ")
              s ! result
            }
            else {
              log.info("GreetingFetcher NOT entity.isInstanceOf[GreetingResponse]")
              s ! FAIL
            }
          case resp@HttpResponse(code, _, _, _) =>
            log.info("GreetingFetcher request failed, response code: " + code)
            //resp.discardEntityBytes()
            s ! FAIL
          case unknown =>
            log.info(s"GreetingFetcher unknown response: $unknown")
            s ! FAIL
        }
      }
    case FETCHGREETINGPING =>
      sender() ! GreetingResponse("PONG","PONG")
  }
}