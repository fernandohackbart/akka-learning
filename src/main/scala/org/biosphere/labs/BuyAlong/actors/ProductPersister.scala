package org.biosphere.labs.BuyAlong.actors

import akka.actor.{Actor, ActorLogging}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.duration._
import org.biosphere.labs.BuyAlong.actors.GreetingFetcherCommands.FETCHGREETING
import org.biosphere.labs.BuyAlong.actors.ProductPersisterStatus.FAIL
import org.biosphere.labs.BuyAlong.utils._

import scala.concurrent.Await

object ProductPersisterStatus {
  case object FAIL
}

class ProductPersister extends Actor with ActorLogging {
  var sequence = 0
  implicit val timeout = Timeout(10.seconds)

  def receive = {
    case Product(brand,name) =>
      log.info(s"ProductPersister $brand.$name!")
      sequence+=1
      val respProduct = Product(brand,s"$name-Persisted-$sequence")
      sender() ! respProduct
    case ProductOperation(operation,Product(brand,name)) =>
      log.info(s"ProductPersister ProductOperation $operation $brand.$name!")
      sequence+=1
      val respProduct = Product(brand,s"$name-$operation-$sequence")
      sender() ! respProduct
    case GreetingRequestActor(actorRef) =>
      val respFuture = actorRef ? FETCHGREETING
      val response = Await.result(respFuture, 5.second).asInstanceOf[GreetingResponse]
      if (response.isInstanceOf[GreetingResponse])
        sender() ! response
      else
        sender() ! FAIL
    case _      =>
      log.warning("ProductPersister received unknown message")
      sender() ! FAIL
  }
}