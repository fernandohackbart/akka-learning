
name := "akka-learning"
version := "1.0"
scalaVersion := "2.12.1"
organization := "org.biosphere"
scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")
libraryDependencies ++= {
  //val akkaV      = "2.5.3"
  val akkaHttpV  = "10.0.9"
  val scalaTestV = "3.2.0-SNAP5"
  Seq(
    "com.typesafe.akka" %% "akka-http-core" % akkaHttpV,
    "com.typesafe.akka" %% "akka-http" % akkaHttpV,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpV,
    "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpV % "test",
    "org.scalatest"     %% "scalatest" % scalaTestV % "test"
  )
}

enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)