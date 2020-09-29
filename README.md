# kafka-demo
Example project used to demonstrate Kafka. Docker files used from https://github.com/wurstmeister/kafka-docker

## References

- Kafka documentation https://kafka.apache.org/documentation/
- Spring Initializr https://start.spring.io/
- Kafkacat https://github.com/edenhill/kafkacat
- Kafka minion https://github.com/cloudworkz/kafka-minion

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

### Essentials

1. produce message (will auto create topic with 1 replication factor)
    - `kafkacat -b 192.168.99.100:9092 -t email -P`

2. consume message
    - `kafkacat -b 192.168.99.100:9092 -C -t email`
    - `kafkacat -b 192.168.99.100:9092 -C -t email -o 0 -p 0 -f '\nKey (%K bytes): %k\n  Value (%S bytes): %s\n  Partition: %p\n  Offset: %o\n  Headers: %h\n'`
    - `kafkacat -h`

3. describe topic (see replication factor + check leader)
    - `kafkacat -b 192.168.99.100:9092 -L -t email`
    - `./kafka-topics.sh --bootstrap-server 192.168.99.100:9092 --describe  --topic email`

4. now kill a broker that contains topic
5. check that you're unable to consume messages (even with another broker)
    - `kafkacat -b 192.168.99.100:9093 -C -t email`
    - `kafkacat -b 192.168.99.100:9092 -L -t email`

6. Check broker configurations of default replication factor and retention time
    - `cat /opt/kafka/config/server.properties`
    - `cat server.properties | grep 'partitions' -B 3`
    - `num.partitions=1`
    - See [kafka documentation](https://kafka.apache.org/documentation/)

### Replication with failover

1. create replicated topic
    - `./kafka-topics.sh --bootstrap-server 192.168.99.100:9092 --create --replication-factor 2 --partitions 1 --topic survey`

2. describe topic
    - `kafkacat -b 192.168.99.100:<port> -L -t survey`

3. produce message
    - `echo "some answer" | kafkacat -b 192.168.99.100:9092 -t survey`

4. consume it
    - `kafkacat -b 192.168.99.100:9092 -C -t survey -e`

5. kill the leader broker
6. check still able to consume message
    - `kafkacat -b 192.168.99.100:9092 -C -t survey -e`

### Spring boot

In order to produce or consume Kafka messages in a Spring Boot application, the Spring community created the user friendly [Spring for Apache Kafka](https://spring.io/projects/spring-kafka) library.
See an example in `/springboot` folder, or create an application using [Spring initializer](https://start.spring.io/#!type=gradle-project&language=java&platformVersion=2.3.4.RELEASE&packaging=jar&jvmVersion=11&groupId=com.example&artifactId=kafka&name=kafka&description=Demo%20kafka-consumer%20project%20with%20Spring%20Boot&packageName=com.example.kafka&dependencies=kafka,devtools).

1. Start application with topic `survey`

2. produce message to that topic, show the log
    - `echo "another answer" | kafkacat -b 192.168.99.100:9092 -t survey`

3. show consumer lag
    - `./kafka-consumer-groups.sh --bootstrap-server 192.168.99.100:9092 --list`
    - `./kafka-consumer-groups.sh --bootstrap-server 192.168.99.100:9092 --describe --group springboot`

4. kill the consumer, produce a message and check consumer lag again
    - `echo "yet another answer" | kafkacat -b 192.168.99.100:9092 -t survey`
    - `./kafka-consumer-groups.sh --bootstrap-server 192.168.99.100:9092 --describe --group springboot`

5. restart consumer, show consumed
    - `./kafka-consumer-groups.sh --bootstrap-server 192.168.99.100:9092 --describe --group springboot`

### Compaction

1. create topic with compaction
    - `./kafka-topics.sh --bootstrap-server 192.168.99.100:9092 --create --topic transaction --replication-factor 2 --partitions 1 --config cleanup.policy=compact --config min.cleanable.dirty.ratio=0.01 --config segment.ms=1000 --config delete.retention.ms=100`

2. check compaction settings
    - `cat /opt/kafka/config/server.properties`

3. Start spring boot application with consuming from transaction topic
4. produce messages with different keys
    - `echo "14>{\"transactionId\":14,\"customerName\":\"Mitchel Nijdam\"}" | kafkacat -b 192.168.99.100:9092 -t transaction -P -K '>'`
    - `echo "15>{\"transactionId\":15,\"customerName\":\"John Doe\"}" | kafkacat -b 192.168.99.100:9092 -t transaction -P -K '>'`
    - `echo "15>{\"transactionId\":15,\"customerName\":\"Timothy Tseng\"}" | kafkacat -b 192.168.99.100:9092 -t transaction -P -K '>'`
    - `echo "14>{\"transactionId\":14,\"customerName\":\"Mitchel Nijdam\"}" | kafkacat -b 192.168.99.100:9092 -t transaction -P -K '>'`
    - `echo "16>{\"transactionId\":16,\"customerName\":\"Unknown\"}" | kafkacat -b 192.168.99.100:9092 -t transaction -P -K '>'`

5. Check that all messages have been consumed one by one
6. Restart Spring boot app with diffferent consumer group
7. Show only latest message is consumed

