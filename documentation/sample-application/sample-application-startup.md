# Starting the container for the sample-aplication
 
## Cassandra

### Server
Running in backgound not exposed (have to run the application in another container)
```
docker run --name cassandra-server  -d -e CASSANDRA_BROADCAST_ADDRESS=172.17.0.2 -p 7000:7000 cassandra
```

Running in backgound *totally* exposed
```
docker run --name cassandra-server  -d -e CASSANDRA_BROADCAST_ADDRESS=172.17.0.2 -P cassandra
```

Running with terminal for debug
```
docker run --name cassandra-server  -t -e CASSANDRA_BROADCAST_ADDRESS=172.17.0.2 -P cassandra
```

Cleanup 
```
docker stop cassandra-server
docker rm cassandra-server
```

### Client 
```
docker run --name cassandra-client -it --link cassandra-server:cassandra --rm cassandra sh -c 'exec cqlsh "$CASSANDRA_PORT_9042_TCP_ADDR"'
```
Cleanup 
```
docker stop cassandra-client
docker rm assandra-client
```
Some useful commands
```
DESCRIBE keyspaces;
use <keyspace_name>;
DESCRIBE tables;
SELECT * FROM product;

```

## Kafka

### Spotify

Background
```
docker run --name kafka-server -d -p 2181:2181 -p 9092:9092 --env ADVERTISED_HOST=172.17.0.2 --env ADVERTISED_PORT=9092 spotify/kafka
```

Foreground
```
docker run --name kafka-server -p 2181:2181 -p 9092:9092 --env ADVERTISED_HOST=172.17.0.2 --env ADVERTISED_PORT=9092 spotify/kafka
```

## akka-learning

Run container
```
docker run --name akka-learning  -t --link cassandra-server:cassandra -p 9000 biosphere/akka-learning:0.0.1
```
Cleanup 
```
docker stop akka-learning
docker rm akka-learning
```

Invoking the application on container
```
curl -XPOST -H "Content-Type:application/json" -d '{"operation":"operation","product":{"brand":"ACME","name":"Train"}}' http://172.17.0.3:9000/persist
```

Invoking the application running on IDE
```
curl -XPOST -H "Content-Type:application/json" -d '{"operation":"operation","product":{"brand":"ACME","name":"Train"}}' http://localhost:9000/persist
```
