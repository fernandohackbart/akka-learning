package org.biosphere.labs.akka.learning.utils

import akka.actor.ActorRef
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

final case class Product(brand: String, name: String)
final case class ProductOperation(operation: String, product: Product)
final case class GreetingRequest(messageBody: String)
final case class GreetingResponse(one: String, key: String)
final case class GreetingRequestActor(actorRef: ActorRef)

trait Protocols extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val productFormat = jsonFormat2(Product)
  implicit val productOperationFormat = jsonFormat2(ProductOperation)
  implicit val greetingRequestFormat = jsonFormat1(GreetingRequest)
  implicit val greetingResponseFormat = jsonFormat2(GreetingResponse)
}