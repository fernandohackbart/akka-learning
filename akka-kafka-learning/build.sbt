name := "akka-stream-learning"
organization := "org.biosphere"
version := "1.0"
scalaVersion := "2.11.11"
lazy val akkaVersion = "2.4.18"
val logbackV   = "1.1.3"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" % "akka-stream-kafka_2.11" % "0.16",
  "ch.qos.logback" % "logback-classic" % logbackV % Runtime
)
