package com.lightbend.akka.sample

import akka.actor.ActorSystem
import akka.kafka.scaladsl.Producer.Message
import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.Producer
import akka.stream.scaladsl.Source
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.ByteArraySerializer
import org.apache.kafka.common.serialization.StringSerializer
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink

import scala.concurrent.Future
import akka.{Done, NotUsed}

import scala.util.{Failure, Success}

trait ProducerExample {
  val system = ActorSystem("example")

  val producerSettings = ProducerSettings(system, new ByteArraySerializer, new StringSerializer).withBootstrapServers("172.17.0.2:9092")
  val kafkaProducer = producerSettings.createKafkaProducer()

  implicit val ec = system.dispatcher
  implicit val materializer = ActorMaterializer.create(system)

  def terminateWhenDone(result: Future[Done]): Unit = {
    result.onComplete {
      case Failure(e) =>
        println("######################################### Failure #########################################")
        system.log.error(e, e.getMessage)
        system.terminate()
      case Success(_) =>
        println("######################################### Success #########################################")
        system.terminate()
    }
  }
}

object PlainSinkExample extends ProducerExample {
  def main(args: Array[String]): Unit = {
    val done: NotUsed = Source(1 to 100)
      .map(_.toString)
      .map { elem =>
        new ProducerRecord[Array[Byte], String]("topic1", elem)
      }
      .runWith(Producer.plainSink(producerSettings))
    println("######################################### PlainSinkExample #########################################")
    //terminateWhenDone(done)
  }
}

/*
object PlainSinkWithProducerExample extends ProducerExample {
  def main(args: Array[String]): Unit = {
    val done = Source(1 to 100)
      .map(_.toString)
      .map { elem =>
        new ProducerRecord[Array[Byte], String]("topic1", elem)
      }
      .runWith(Producer.plainSink(producerSettings, kafkaProducer))
    terminateWhenDone(done)
  }
}
*/

object ProducerFlowExample extends ProducerExample {
  def main(args: Array[String]): Unit = {
    val done = Source(1 to 100)
      .map { n =>
        val partition = 0
        Message(new ProducerRecord[Array[Byte], String]("topic1", partition, null, n.toString), n)
      }
      .via(Producer.flow(producerSettings))
      .map { result =>
        val record = result.message.record
        println(s"${record.topic}/${record.partition} ${result.offset}: ${record.value}" +
          s"(${result.message.passThrough})")
        result
      }
      .runWith(Sink.ignore)
    println("######################################### ProducerFlowExample #########################################")
    terminateWhenDone(done)
  }
}