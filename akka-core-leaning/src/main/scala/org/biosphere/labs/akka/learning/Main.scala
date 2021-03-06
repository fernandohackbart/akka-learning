package org.biosphere.labs.akka.learning

import akka.actor.{ActorSystem, Props}
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import org.biosphere.labs.akka.learning.actors.{GreetingFetcher, ProductCacher, ProductWriter}
import org.biosphere.labs.akka.learning.utils.Service

import scala.concurrent.ExecutionContext

object Main extends App with Service {
  override implicit val actorSystem = ActorSystem()
  implicit val executor: ExecutionContext = actorSystem.dispatcher
  override implicit val log: LoggingAdapter = Logging(actorSystem, getClass)
  override implicit val materializer: ActorMaterializer = ActorMaterializer()
  override val productCacher = actorSystem.actorOf(Props[ProductCacher], "product-cacher")
  override val productWriter = actorSystem.actorOf(Props(new ProductWriter(cluster)),"product-writer")
  override val greetingFetcher = actorSystem.actorOf(Props[GreetingFetcher], "greeting-fetcher")
  val bindingFuture = Http().bindAndHandle(routes, httpHost, httpPort)

  //Should be commented out to be run into Docker (otherwise shuts down the system :P)
  //println(s"Waiting for requests at http://$httpHost:$httpPort/...\nHit RETURN to terminate")
  //StdIn.readLine()
  //bindingFuture.flatMap(_.unbind())
  //actorSystem.terminate()
}