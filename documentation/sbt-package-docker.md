# Packaging a Akka application as a docker container

* https://github.com/sbt/sbt-native-packager/blob/master/README.md
* http://www.scala-sbt.org/sbt-native-packager/formats/universal.html
* http://www.scala-sbt.org/sbt-native-packager/formats/docker.html


Changes in the prject files:

Added to the plugins.sbt
```
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.2.0")
```

Added to the build.sbt
```
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
```

Running in IntelliJ IDEA 2017.1

Tools > Start SBT Shell (and wait a bit for the updating ...)

Generate the container locally
```
docker:publishLocal
```

Publish the container
```
docker:publish
```

To run the container
```
docker run -p 9000 biosphere/akka-learning:0.0.1
```
Consume some services
```
curl -XGET  http://172.17.0.1:9000/product
curl -XGET  http://172.17.0.1:9000/status
curl -XPOST -H "Content-Type:application/json" -d '{"brand":"ACME","name":"RoadRunner"}' http://172.17.0.1:9000/product
curl -XPOST -H "Content-Type:application/json" -d '{"messageBody":"Greetings!"}' http://172.17.0.1:9000/product
```

