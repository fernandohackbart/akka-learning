name := "akka-learning"
version := "1.0"
scalaVersion := "2.12.2"
organization := "org.biosphere"
scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")
libraryDependencies ++= {
  val akkaV      = "2.5.3"
  val akkaHttpV  = "10.0.9"
  val scalaTestV = "3.2.0-SNAP5"
  Seq(
    "com.typesafe.akka" %% "akka-http-core" % akkaHttpV,
    "com.typesafe.akka" %% "akka-http" % akkaHttpV,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpV,
    "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpV % "test",
    //"com.typesafe.akka" %% "akka-persistence" % akkaV,
    //"org.iq80.leveldb"  % "leveldb" % "0.9",
    //"org.fusesource.leveldbjni" % "leveldbjni-all" % "1.8",
    "org.scalatest"     %% "scalatest" % scalaTestV % "test"
  )
}

enablePlugins(JavaAppPackaging)
enablePlugins(UniversalPlugin)
enablePlugins(DockerPlugin)

packageName in Docker := "akka-learning"
version in Docker := "0.0.1"
maintainer in Docker := "Fernando Hackbart<fhackbart@gmail.com>"
packageSummary in Docker := "Akka learning application (Fernando Hackbart)"
packageDescription := "Docker [micro|nano] Akka based Service"
dockerRepository := Some("biosphere")
dockerExecCommand := Seq("sudo","/usr/bin/docker")
dockerBaseImage := "biosphere/biosphere:base"
daemonUser in Docker := "biosphere"
dockerExposedPorts := Seq(9000)
defaultLinuxInstallLocation in Docker := "/u01/akka-learning"