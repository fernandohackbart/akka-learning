package org.biosphere.labs.akka.learning.actors

import akka.actor.{Actor, ActorLogging}
import akka.pattern.ask
import akka.util.Timeout
import org.biosphere.labs.akka.learning.actors.GreetingFetcherCommands.FETCHGREETING
import org.biosphere.labs.akka.learning.actors.ProductPersisterStatus.FAIL
import org.biosphere.labs.akka.learning.utils._

import scala.concurrent.Await
import scala.concurrent.duration._

object ProductPersisterStatus {
  case object FAIL
  case object OK
}

class ProductCacher extends Actor with ActorLogging {
  var sequence = 0
  implicit val timeout = Timeout(10.seconds)

  def receive = {
    case Product(brand,name) =>
      log.info(s"ProductCache $brand.$name!")
      sequence+=1
      val respProduct = Product(brand,s"$name-Persisted-$sequence")
      sender() ! respProduct
    case ProductOperationRequest(operation,Product(brand,name)) =>
      log.info(s"ProductCache ProductOperation $operation $brand.$name!")
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
      log.warning("ProductCache received unknown message")
      sender() ! FAIL
  }
}