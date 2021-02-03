package org.example.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * spring-kafka 生产者
 */
@Slf4j
@Component
public class SpringKafkaProducer {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    public void produce(String topic, String message) {
        kafkaTemplate.send(topic, message);
        log.info("生产者发送消息：{}，{}", topic, message);
    }

    public void produce(ProducerRecord<String, String> record) {
        kafkaTemplate.send(record);
        log.info("生产者发送消息：{}", record.toString());
    }

}
