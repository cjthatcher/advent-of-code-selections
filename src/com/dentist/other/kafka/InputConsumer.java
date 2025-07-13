package com.dentist.other.kafka;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public abstract class InputConsumer {

    private final Consumer<String, String> consumer;

    public InputConsumer(String topicName, String serverAddress) {
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, serverAddress); // Kafka broker address(es)
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "my-consumer-group"); // Consumer group ID
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); // or "latest", "none"

        consumer = new KafkaConsumer<>(properties);

        consumer.subscribe(Collections.singletonList(topicName));
    }

    public void beginConsumption() {
        try {
            int attempts = 0;
            while (attempts++ < 20) {
                System.err.println("CHRIS CONSUME: POLLING NUMBER " + attempts);
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(3_000)); // Poll for records
                for (ConsumerRecord<String, String> record : records) {
                    System.err.println("I GOT A LINE: " + record.key() + ":" + record.value());
                    handleLine(record.value());
                }
            }
        }
         finally {
            consumer.close(); // Close the consumer when done
        }
    }

    public abstract void handleLine(String s);

}
