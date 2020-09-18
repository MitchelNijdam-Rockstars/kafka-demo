# kafka-demo
Example project used to demonstrate Kafka. Docker files used from https://github.com/wurstmeister/kafka-docker

## Pre-requisites

- Docker
- Docker compose https://docs.docker.com/compose/install/
- (Optional) Kafkacat https://github.com/edenhill/kafkacat (see `install_kafkacat.md`)

 
## Run

In order to startup the kafka cluster, make sure Docker is running and navigate to the `docker/` folder. Then run:

```
docker-compose up
```

This will start two containers, one with a Zookeper node and one Kafka broker. If you would like to startup a cluster with 3 brokers (in detached mode), run:

```
docker-compose up -d --scale kafka=3
``` 

## Now use it!

TODO: commands to create topics, produce, consume, etc.
