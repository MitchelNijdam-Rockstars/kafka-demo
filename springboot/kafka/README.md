# Example Spring Boot project with Kafka
Simply to demonstrate the ease of use of Kafka within Spring boot.

## Get started
Make sure to change the `bootstrap-servers` configuration to point to the ip of your brokers.
In production, make sure all brokers are configured there, so there is a backup ip in case of failure of one of the brokers.


## Contents

| File  | Purpose |
|---|---|
| application.yml | Several configurations required to connect with Kafka |
| KafkaApplication  | Contains main method, and `@EnableKafka` annotation  |
| KafkaConsumer  | Contains a simple kafka consumer that reads from one topic and logs the content of the message  |

### Reference Documentation
For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.3.4.RELEASE/gradle-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.3.4.RELEASE/gradle-plugin/reference/html/#build-image)
* [Spring for Apache Kafka](https://docs.spring.io/spring-boot/docs/2.3.4.RELEASE/reference/htmlsingle/#boot-features-kafka)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/2.3.4.RELEASE/reference/htmlsingle/#using-boot-devtools)

### Additional Links
These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)

