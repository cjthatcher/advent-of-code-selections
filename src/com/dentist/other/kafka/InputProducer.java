package com.dentist.other.kafka;

import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.ListTopicsOptions;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.config.TopicConfig;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Stream;

public class InputProducer {

    private final String filePath;
    private final String topicName;
    private final Admin admin;
    private final Producer<String, String> producer;

    public InputProducer(String filePath, String topicName, String serverAddress) {
        this.filePath = filePath;
        this.topicName = topicName;
        this.admin = createAdmin(serverAddress);
        this.producer = createProducer(serverAddress);
    }

    private Admin createAdmin(String serverAddress) {
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, serverAddress);
        return Admin.create(props);
    }

    private Producer<String, String> createProducer(String serverAddress) {
        Properties props = new Properties();
        props.put("bootstrap.servers", serverAddress);
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        return new KafkaProducer<>(props);
    }

    public void produceData() throws IOException {
        createTopic();
        sendLinesToBroker(filePath);
    }

    private void createTopic() {
        try {
            createTopicIfNotExists(topicName).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to create topic.", e);
        }
    }

    private void sendLinesToBroker(String filePath) {
        System.err.println("CHRIS PRODUCE: STARTING PRODUCING OF FILE " + filePath);
        try (Stream<String> lines = new BufferedReader(new FileReader(filePath)).lines()) {
            List<Future<RecordMetadata>> futures = new ArrayList<Future<RecordMetadata>>();
            lines.forEach(line -> {
                var foo = producer.send(new ProducerRecord<>(topicName, "arbitraryKey", line));
                futures.add(foo);
                System.err.println("CHRIS: PRODUCED A LINE");
                try {
                    Thread.sleep(125);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });

            for (Future<RecordMetadata> f : futures) {
                f.get();
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    private CompletableFuture<Boolean> createTopicIfNotExists(String topicName) {
        ListTopicsOptions options = new ListTopicsOptions().timeoutMs(15_000);

        Set<String> topicNames;
        try {
            topicNames = admin.listTopics(options).names().get();
            topicNames.forEach(name -> System.err.println("DISCOVERED TOPIC: " + name));
            return maybeCreateTopic(topicNames.contains(topicName), topicName, admin);
        }
        catch (Exception e) { //(InterruptedException | ExecutionException e) {
            throw new RuntimeException("Couldn't get list of topics from broker. Admin is probably not connected / configured incorrectly.", e);
        }
    }

    private static CompletableFuture<Boolean> maybeCreateTopic(Boolean topicExists, String topicName, Admin admin) {
        if (topicExists) {
            return CompletableFuture.completedFuture(true);
        } else {
            int partitions = 4;
            short replicationFactor = 1;
            // Create a compacted topic
            return admin.createTopics(Collections.singleton(newTopicConfigs(topicName, partitions, replicationFactor)))
                    .values().get(topicName)
                    .thenApply(ignored -> true)
                    .toCompletionStage()
                    .toCompletableFuture();
        }
    }

    private static NewTopic newTopicConfigs(String topicName, int partitions, short replicationFactor) {
        return new NewTopic(topicName, partitions, replicationFactor)
                .configs(Collections.singletonMap(TopicConfig.CLEANUP_POLICY_CONFIG, TopicConfig.CLEANUP_POLICY_COMPACT));
    }
}
