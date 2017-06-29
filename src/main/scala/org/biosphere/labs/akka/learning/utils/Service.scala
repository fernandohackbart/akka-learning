package org.biosphere.labs.akka.learning.utils

import akka.actor.{ActorRef, ActorSystem}
import akka.event.LoggingAdapter
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.duration._

trait Service extends Protocols with Config {
  implicit val actorSystem: ActorSystem
  implicit val log: LoggingAdapter
  val productPersister: ActorRef
  val greetingFetcher: ActorRef
  implicit val timeout = Timeout(10.seconds)

  implicit val routes: Route = {
    logRequestResult("buy-along-poc") {
      path("product") {
        get {
          log.info("Service(/product) GET")
          complete(StatusCodes.OK, Product("IKEA", "Billy"))
        } ~
          post {
            log.info("Service(/product) POST")
            entity(as[Product]) { product =>
              log.info(s"Service(/product) ${product.brand}.${product.name}")
              onSuccess(productPersister ? product) {
                case response: Product =>
                  complete(StatusCodes.OK, response)
                case _ =>
                  complete(StatusCodes.InternalServerError)
              }
              /*
            } ~
              entity(as[ProductOperation]) { productOp: ProductOperation =>
                log.info(s"Service(/productOp) ${productOp.operation} ${productOp.product.brand}.${productOp.product.name}")
                val productPersisterLocal: Future[ActorRef] = actorSystem.actorSelection("/user/productpersister").resolveOne(10.seconds){
                  onSuccess()

                }
                onSuccess(productPersisterLocal ! productOp) {
                  case response: Product =>
                    complete(StatusCodes.OK, response)
                  case _ =>
                    complete(StatusCodes.InternalServerError)
                }
                */
            } ~
              entity(as[GreetingRequest]) { greetingRequest =>
                log.info(s"Service(/product) GreetingRequest ${greetingRequest.messageBody}")
                onSuccess(productPersister ? GreetingRequestActor(greetingFetcher)) {
                  case response: GreetingResponse =>
                    complete(StatusCodes.OK, response)
                  case _ =>
                    complete(StatusCodes.InternalServerError)
                }
              } ~
              entity(as[String]) { text =>
                log.info(s"Service(/product) text  ${text}")
                complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, s"Service(/product) ERROR!!! ${text}"))
              }
          }
      } ~
        path("status") {
          val res = new DisplayActorsExposer(actorSystem)('printTree)()
          complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, res.toString))
        }
    }
  }

}