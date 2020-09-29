# kafka-demo
Example project used to demonstrate Kafka. Docker files used from https://github.com/wurstmeister/kafka-docker

## Pre-requisites

- Docker
- Docker compose https://docs.docker.com/compose/install/
- (Optional) Kafkacat https://github.com/edenhill/kafkacat (see `install_kafkacat.md`)

 
## Run

In order to startup the kafka cluster, make sure Docker is running and navigate to the `docker/` folder. Then run:

```bash
cd docker/
docker-compose up --no-recreate
```

This will start two containers, one with a Zookeper node and one Kafka broker. If you would like to startup a cluster with 3 brokers (in detached mode), run:

```bash
docker-compose up -d --scale kafka=3 --no-recreate
```

Or, if you want to start your containers from scratch without any old data (only useful when already started and used before):

```bash
 docker-compose up -d --scale kafka=3 --force-recreate --renew-anon-volumes
```

To stop all running docker containers: `docker-compose stop`

## Now use it!

### Command line

Inside the container of the kafka broker (`docker exec`), go to the folder `/opt/kafka/bin/` where the shell scripts live.

**Create a topic with 1 partition**
```
./kafka-topics.sh --bootstrap-server <ip:port> --create --replication-factor 2 --partitions 1 --topic <topic>
```

**Create compacted topic**
```
./kafka-topics.sh  --bootstrap-server <ip:port> --create --topic <topic> --replication-factor 2 --partitions 1 --config cleanup.policy=compact
```

**Describe the topic**

Kafka console tools:
```
./kafka-topics.sh --bootstrap-server <ip:port> --describe --topic <topic>
```
Kafkacat:
```
kafkacat -L -b <ip:port>
```

**Produce to a topic**

Kafka console tools:
```
./kafka-console-producer.sh --bootstrap-server <ip:port> --topic <topic>
```
Kafkacat:
```
kafkacat -b <ip:port> -t <topic> -P
or
echo "Hello World" | kafkacat -b <ip:port> -t <topic> -P
```

**Consume from a topic**

Kafka console tools:
```
./kafka-console-consumer.sh --bootstrap-server <ip:port> --topic <topic> --from-beginning
```
Kafkacat:
```
kafkacat -b <ip:port> -t <topic> -C
```
or with more information and formatting (see `kafkacat -h`)
```
kafkacat -b <ip:port> -t <topic> -C \
  -f '\nKey (%K bytes): %k\n  Value (%S bytes): %s\n  Partition: %p\n  Offset: %o\n  Headers: %h\n'
```

### Spring Boot application

In order to produce or consume Kafka messages in a Spring Boot application, the Spring community created the user friendly [Spring for Apache Kafka](https://spring.io/projects/spring-kafka) library.
See an example in `/springboot` folder, or create an application using [Spring initializer](https://start.spring.io/#!type=gradle-project&language=java&platformVersion=2.3.4.RELEASE&packaging=jar&jvmVersion=11&groupId=com.example&artifactId=kafka&name=kafka&description=Demo%20kafka-consumer%20project%20with%20Spring%20Boot&packageName=com.example.kafka&dependencies=kafka,devtools).

