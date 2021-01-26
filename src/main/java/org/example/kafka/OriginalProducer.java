package org.example.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class OriginalProducer {

    private static final String TOPIC = "TEST_TOPIC";

    private static final String BROKER_LIST = "192.168.3.4:9092,192.168.3.5:9092,192.168.3.6:9092";

    private static final String ACKS_CONFIG = "all";

    private static final String BATCH_SIZE_CONFIG = "1";

    private static KafkaProducer<String, String> producer;

    static {
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BROKER_LIST);
        properties.setProperty(ProducerConfig.ACKS_CONFIG, ACKS_CONFIG);
        properties.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, BATCH_SIZE_CONFIG);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producer = new KafkaProducer<String, String>(properties);
    }

    public static void produce() {
        try {
            String message = "This is a message !";
            ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, message);
            producer.send(record, (recordMetadata, e) -> {
                if (e != null) {
                    System.out.println("发送消息异常！");
                }
                if (recordMetadata != null) {
                    System.out.println("消息发送成功：" + recordMetadata.offset());
                }
            });
            Thread.sleep(1000L);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            producer.close();
        }
    }

}
