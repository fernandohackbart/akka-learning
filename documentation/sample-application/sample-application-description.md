# Description of the simple project

```
                     
 ----------           -------------            ------------------------
 |        |---------->| Persisted |----------->|                      |
 |        |           |   actor   |            |      Cassandra       |
 |        |           -------------            |                      |
 |        |                V                   ------------------------
 |        |           -------------            
 |        |           |    Log    |            
 |  HTTP  |           |   actor   |            
 |        |           -------------
 |        |                V                   ------------------------
 |        |           -------------            |                      |
 |        |           |   Event   |----------->|        Kafka         |
 |        |<----------|   actor   |            |                      |
 ----------           -------------            ------------------------ 
            
```

## Components:

### The message:
The circuit relies on one message with the case class Product.

### HTTP
The App receives a request and serializes the message using Google Protocol Buffer (protobuf),
the serialized message is submit to one Akka Streams flow that ensures is passes through the the following actors.


### Persisted actor
This actor receives the message with the Product and persists is into the Cassandra database  

### Log actor
This actor receives the message with the Product write a log of the operation

### Event actor
This actor receives the message with the Product and propagate one event over Kafka

##Containerization
The application will be consisted of three containers:
* Cassandra container
    * library/cassandra:latest
    * https://hub.docker.com/_/cassandra/
* Kafka container
    * Wurstmeister
        * wurstmeister/kafka
        * https://hub.docker.com/r/wurstmeister/kafka/
        * http://wurstmeister.github.io/kafka-docker/
        * https://store.docker.com/community/images/wurstmeister/kafka
        * https://github.com/wurstmeister/kafka-docker
    * Spotify
        * https://github.com/spotify/docker-kafka
        
* akka-learning container
    * biosphere/akka-learning:0.0.1
    * https://github.com/fernandohackbart/akka-learning/blob/master/documentation/sbt-package-docker.md

