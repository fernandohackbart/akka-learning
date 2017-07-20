name := "akka-kafka-learning"
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

enablePlugins(JavaAppPackaging)
enablePlugins(UniversalPlugin)
enablePlugins(DockerPlugin)

packageName in Docker := "akka-kafka-learning"
version in Docker := "0.0.1"
maintainer in Docker := "Fernando Hackbart<fhackbart@gmail.com>"
packageSummary in Docker := "Akka learning application (Fernando Hackbart)"
packageDescription := "Docker [micro|nano] Akka based Service"
dockerRepository := Some("biosphere")
dockerExecCommand := Seq("sudo","/usr/bin/docker")
dockerBaseImage := "biosphere/biosphere:base"
daemonUser in Docker := "biosphere"
dockerExposedPorts := Seq(9000)
defaultLinuxInstallLocation in Docker := "/u01/akka-kafka-learning"