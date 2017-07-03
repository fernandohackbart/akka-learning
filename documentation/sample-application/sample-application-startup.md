# Starting the container for the sample-aplication
 
## Cassandra
```
docker run --name some-cassandra -d -e CASSANDRA_BROADCAST_ADDRESS=10.42.42.42 -p 7000:7000 cassandra:tag
```

## Kafka

### Spotify
```
docker run -p 2181:2181 -p 9092:9092 --env ADVERTISED_HOST=`docker-machine ip \`docker-machine active\`` --env ADVERTISED_PORT=9092 spotify/kafka
```
### Wurstmeister


## akka-learning
```

```
