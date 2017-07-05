package org.biosphere.labs.akka.learning.actors

import akka.actor.{Actor, ActorLogging}
import org.biosphere.labs.akka.learning.domain.OperationOutcome.FAIL
import org.biosphere.labs.akka.learning.domain.{ProductOperationRequest, ProductOperationResponse}



class ProductProducer extends Actor with ActorLogging  {

  //val producerSettings = ProducerSettings = _
  //var preparedStatement: PreparedStatement = _

  override def preStart = {
    log.debug("ProductProducer.preStart() Calling ensureKeyspace()")
  }

  def receive = {
    case ProductOperationRequest(operation, product) =>
      val s = sender
      log.info(s"ProductProducer operation ($operation) on ${product.brand}.${product.name}!")
      val por = ProductOperationResponse("OK")
      s ! por
    case _ =>
      log.warning("ProductProducer received unknown message")
      sender() ! FAIL
  }

}
