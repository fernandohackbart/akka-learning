package org.biosphere.labs.BuyAlong

import akka.actor.{ActorSystem, Props}
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import org.biosphere.labs.BuyAlong.utils.Service
import org.biosphere.labs.BuyAlong.actors.{GreetingFetcher, ProductPersister}

import scala.concurrent.ExecutionContext
import scala.io.StdIn

object BuyAlong extends App with Service {
  override implicit val actorSystem = ActorSystem()
  implicit val executor: ExecutionContext = actorSystem.dispatcher
  override implicit val log: LoggingAdapter = Logging(actorSystem, getClass)
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  override val productPersister = actorSystem.actorOf(Props[ProductPersister], "productpersister")
  override val greetingFetcher = actorSystem.actorOf(Props[GreetingFetcher], "greetingFetcher")
  val bindingFuture = Http().bindAndHandle(routes, httpHost, httpPort)
  println(s"Waiting for requests at http://$httpHost:$httpPort/...\nHit RETURN to terminate")
  StdIn.readLine()
  bindingFuture.flatMap(_.unbind())
  actorSystem.terminate()

  //curl -XPOST -H "Content-Type:application/json" -d '{"brand":"IKEA","name":"Malmo"}' http://localhost:9000/product
  //curl -XPOST -H "Content-Type:application/json" -d '{"messageBody":"Greetings!"}' http://localhost:9000/product
  //curl -XGET  http://localhost:9000/product
  //curl -XGET  http://localhost:9000/status
}