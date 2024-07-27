This is a sample application to play with Kafka clients

### Preliminary
- Go to https://confluent.cloud and create API key
- Copy properties into `resources/client.properties` in each module

### Modules
- `producer`: Producer application using [KafkaProducer](https://kafka.apache.org/38/javadoc/org/apache/kafka/clients/producer/KafkaProducer.html)
- `consumer`: Consumer application using [KafkaConsumer](https://kafka.apache.org/38/javadoc/org/apache/kafka/clients/consumer/KafkaConsumer.html)
- `spring-producer`: Producer application using Spring's [KafkaTemplate](https://docs.spring.io/spring-kafka/api/org/springframework/kafka/core/KafkaTemplate.html)
- `spring-consumer`: Consumer application using Spring's [KafkaListener](https://docs.spring.io/spring-kafka/api/org/springframework/kafka/annotation/KafkaListener.html)

### Commands
```
# Apply Formatter % Linter
./gradlew spotlessApply

# Run producer application
./gradlew :producer:run

# Run spring-producer application
./gradlew :spring-producer:run

# Run consumer application
./gradlew :consumer:run

# Run consumer-application:run
./gradlew :spring-consumer:run
```
