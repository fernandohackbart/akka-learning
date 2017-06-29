package org.biosphere.labs.BuyAlong.actors

import scala.concurrent.duration._
import akka.actor.{Actor, ActorLogging}
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, StatusCodes}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import org.biosphere.labs.BuyAlong.actors.GreetingFetcherCommands.FETCHGREETING
import org.biosphere.labs.BuyAlong.actors.ProductPersisterStatus.FAIL
import org.biosphere.labs.BuyAlong.utils.{GreetingResponse, Protocols}

import scala.concurrent.{Await, ExecutionContext}

object GreetingFetcherCommands {
  case object FETCHGREETING
}

class GreetingFetcher extends Actor with ActorLogging with Protocols with SprayJsonSupport{

  import akka.pattern.pipe
  import context.dispatcher

  val http = Http(context.system)
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  def receive = {
    case FETCHGREETING =>
      log.info("GreetingFetcher FETCHGREETING")
      http.singleRequest(HttpRequest(uri = "http://rest-service.guides.spring.io/greeting")).pipeTo(self)
    case HttpResponse(StatusCodes.OK, headers, entity, _) =>
      log.info("GreetingFetcher HttpResponse.OK")
      val respFuture = Unmarshal(entity).to[GreetingResponse]
      val response = Await.result(respFuture, 1.second)
      if (response.isInstanceOf[GreetingResponse]){
        log.info("GreetingFetcher entity.isInstanceOf[GreetingResponse]")
        sender() ! response
      }
      else{
        log.info("GreetingFetcher !entity.isInstanceOf[GreetingResponse]")
        sender() ! FAIL
      }
    case resp @ HttpResponse(code, _, _, _) =>
      log.info("Request failed, response code: " + code)
      resp.discardEntityBytes()
      sender() ! FAIL
  }
}