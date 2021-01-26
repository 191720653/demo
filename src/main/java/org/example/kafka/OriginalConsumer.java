package org.example.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class OriginalConsumer {

    private static final String TOPIC = "TEST_TOPIC";

    private static final String BROKER_LIST = "192.168.3.4:9092,192.168.3.5:9092,192.168.3.6:9092";

    private static final String GROUP_ID = "TEST_GROUP";

    private static final String CLIENT_ID = "TEST_CLIENT";

    private static KafkaConsumer<String, String> consumer;

    static {
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BROKER_LIST);
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, CLIENT_ID);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumer = new KafkaConsumer<String, String>(properties);
        consumer.subscribe(Arrays.asList(TOPIC));
    }

    public static void consume() {
        try {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1L, 0));
            records.forEach(record -> {
                System.out.println("消费消息：" + record.toString());
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            consumer.close();
        }
    }

}
