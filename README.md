# akka-learning
Some (very) basic application to learn Akka Core concepts and deployment, everything is minimal learn the basic concepts and make it work...

*Please do not take this as something done by someone that knows what is doing...*

## Research done
* Akka core components
    * Actor message with ActorRef 
    * Marshal and unmarshal of JSON
    * Get Actor system status
* Akka HTTP
    * http://doc.akka.io/docs/akka-http/current/scala/http/introduction.html
    * routes
    * tests for Akka HTTP
    
* Checking the packaging for Docker (sbt-native-packager) documentation/[sbt-package-docker.md](documentation/sbt-package-docker.md)

* Running `biosphere/akka-learning:0.0.1` container on DC/OS 
    * This application was configured to be run as a container
    * DC/OS (Vagrant) documentation/[dcos-vagrant.md](documentation/dcos-vagrant.md)
 
* One actor interacting with Cassandra
    * Received a JSON request via Akka HTTP and the actor insert in the Cassandra database
    * 

## Current research
* Actor interacting with Kafka
    * Publishing events in one actor
        * Maybe the HTTP request publishes one event?
    * Consuming events in another actor
        * Maybe the ProductWriter consumes one event?

## Next research
* Akka core components
    * ActorSystem.actorSelection: discovering actors and sending messages
    * Actor.become(): changing the actor behavior based on received messages

* Akka core components
    * Akka FSM
        * http://doc.akka.io/docs/akka/current/scala/fsm.html
        * Receiving HTTP requests and pushing through a stream for Actors to consume  
    * Akka Persistence
        * http://doc.akka.io/docs/akka/current/scala/persistence.html

* Akka Streams
    * http://doc.akka.io/docs/akka/current/scala/stream/index.html

* Akka clusters
    * http://doc.akka.io/docs/akka/current/scala/common/cluster.html

* Describe a basic application
    * Create a simple application that uses the learned technologies and makes some sense

* Running `biosphere/akka-learning:0.0.1` container on Kubernetes
    * Will hold a bit in the deployment and wati to have a more complex application that can be described as a POD
    * DC/OS x Kubernetes
	* https://kubernetes.io/docs/getting-started-guides/dcos/

* Maybe I can run on Google/AmazonWS/Azure cloud?
    * https://cloud.google.com/container-engine/
    * https://azure.microsoft.com/en-us/offers/ms-azr-0044p/
    * http://docs.aws.amazon.com/awsaccountbilling/latest/aboutv2/billing-free-tier.html

* Check org.flywaydb looks like a very nice serialization / schema evolution tool 