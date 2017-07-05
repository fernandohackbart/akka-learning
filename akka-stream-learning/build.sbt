
name := "akka-stream-learning"

organization := "org.biosphere"

version := "1.0"

scalaVersion := "2.11.11"

lazy val akkaVersion = "2.5.3"
val logbackV   = "1.1.3"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  //"com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" % "akka-stream-kafka" % "0.11-M1",
  "ch.qos.logback" % "logback-classic" % logbackV % Runtime,
  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)


