//#full-example
package com.lightbend.akka.sample

import akka.actor.{ Actor, ActorLogging, ActorRef, ActorSystem, Props }
import scala.io.StdIn
import akka.stream._
import akka.stream.scaladsl._
import akka.{ NotUsed, Done }
import akka.actor.ActorSystem
import akka.util.ByteString
import scala.concurrent._
import scala.concurrent.duration._
import java.nio.file.Paths


object Main extends App {
  implicit val system = ActorSystem("QuickStart")
  implicit val materializer = ActorMaterializer()
  implicit val ec = system.dispatcher

  val source: Source[Int, NotUsed] = Source(1 to 100)

  // First example
  val done: Future[Done] = source.runForeach(i => println(i))(materializer)
  done.onComplete(_ => system.terminate())

  //val factorials = source.scan(BigInt(1))((acc, next) => acc * next)


  //val result: Future[IOResult] =
  //    factorials
  //    .map(num => ByteString(s"$num\n"))
  //    .runWith(FileIO.toPath(Paths.get("/tmp/factorial-1.txt")))
  //result.onComplete(_ => system.terminate())

  //def lineSink(filename: String): Sink[String, Future[IOResult]] =
  //  Flow[String]
  //      .map(s => ByteString(s + "\n"))
  //      .toMat(FileIO.toPath(Paths.get(filename)))(Keep.right)
  //factorials.map(_.toString).runWith(lineSink("/tmp/factorial-2.txt"))
  // How to do?
  //done.onComplete(_ => system.terminate())

  //factorials
  //  .zipWith(Source(0 to 100))((num, idx) => s"$idx! = $num")
  //  .throttle(1, 1.second, 1, ThrottleMode.shaping)
  //  .runForeach(println)


}