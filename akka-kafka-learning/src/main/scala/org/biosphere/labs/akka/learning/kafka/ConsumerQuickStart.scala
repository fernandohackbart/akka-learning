package org.biosphere.labs.akka.learning.kafka

import akka.Done
import akka.actor.ActorSystem
import akka.kafka.scaladsl.Consumer
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink
import com.typesafe.config.ConfigFactory
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.{ByteArrayDeserializer, StringDeserializer}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

// https://github.com/akka/reactive-kafka/blob/master/docs/src/test/scala/sample/scaladsl/ConsumerExample.scala
// http://doc.akka.io/docs/akka-stream-kafka/current/consumer.html
// https://github.com/jvwilge/akka-stream-kafka-getting-started/blob/master/src/main/scala/net/jvw/Main.scala


trait ConsumerQuickStart {

  implicit val system = ActorSystem.create("ConsumerQuickStart")
  implicit val executor: ExecutionContext = system.dispatcher
  implicit val mat = ActorMaterializer()

  val maxPartitions = 100

  val consumerSettings = ConsumerSettings(system, new ByteArrayDeserializer, new StringDeserializer)
    .withBootstrapServers("172.17.0.2:9092")
    .withGroupId("ConsumerQuickStartGroup")
    .withClientId("ConsumerQuickStartClient")
    .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")

  def terminateWhenDone(result: Future[Done]): Unit = {
    result.onComplete {
      case Failure(e) =>
        system.log.error(e, e.getMessage)
        system.terminate()
      case Success(_) => system.terminate()
    }
  }

}

object AtLeastOnceQuickStart extends ConsumerQuickStart {
  def main(args: Array[String]): Unit = {
    val done = Consumer.committableSource(consumerSettings, Subscriptions.topics("topic1"))
      .map(msg => {
        println(msg)
      })
      .runWith(Sink.ignore)
    terminateWhenDone(done)
  }
}