package com.example.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Simple kafka listener which will just log the content of the message.
 */
@Component
public class KafkaConsumer {
    private final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "survey")
    private void listen(ConsumerRecord<String, String> record) {
        logger.warn("Received kafka record with offset: {}, key: {} and value: {}",
                record.offset(), record.key(), record.value());
    }
}
