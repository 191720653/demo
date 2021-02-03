package org.example.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * spring-kafka 消费者
 */
@Slf4j
@Component
public class SpringKafkaConsumer {

    /**
     * 因为有多个消费者，所以先用命令创建一个多分区的主题（防止出现消费者没有分区可以消费）
     */
    public static final String SPRING_TEST_TOPIC = "SPRING_TEST_TOPIC";

    public static final String SPRING_TEST_GROUP_A = "SPRING_TEST_GROUP_A";
    public static final String SPRING_TEST_GROUP_A_CLIENT_A = "SPRING_TEST_GROUP_A_CLIENT_A";
    public static final String SPRING_TEST_GROUP_A_CLIENT_B = "SPRING_TEST_GROUP_A_CLIENT_B";

    public static final String SPRING_TEST_GROUP_B = "SPRING_TEST_GROUP_B";
    public static final String SPRING_TEST_GROUP_B_CLIENT_C = "SPRING_TEST_GROUP_B_CLIENT_C";

    /**
     * 消费主题：SPRING_TEST_TOPIC
     * 消费组：SPRING_TEST_GROUP_A
     * 消费者：SPRING_TEST_GROUP_A_CLIENT_A
     *
     * 因为组 A 下有 CLIENT_A、CLIENT_B 两个消费者，故消息要么被 CLIENT_A 消费，要么被 CLIENT_B 消费
     */
    @KafkaListener(topics = {SPRING_TEST_TOPIC}, groupId = SPRING_TEST_GROUP_A, id = SPRING_TEST_GROUP_A_CLIENT_A)
    public void consumeA(ConsumerRecord<String, String> record) {
        log.info("消费者 A 消费消息：{}", record.toString());
    }
    /**
     * 消费主题：SPRING_TEST_TOPIC
     * 消费组：SPRING_TEST_GROUP_A
     * 消费者：SPRING_TEST_GROUP_A_CLIENT_B
     *
     * 因为组 A 下有 CLIENT_A、CLIENT_B 两个消费者，故消息要么被 CLIENT_A 消费，要么被 CLIENT_B 消费
     */
    @KafkaListener(topics = {SPRING_TEST_TOPIC}, groupId = SPRING_TEST_GROUP_A, id = SPRING_TEST_GROUP_A_CLIENT_B)
    public void consumeB(ConsumerRecord<String, String> record) {
        log.info("消费者 B 消费消息：{}", record.toString());
    }

    /**
     * 消费主题：SPRING_TEST_TOPIC
     * 消费组：SPRING_TEST_GROUP_B
     * 消费者：SPRING_TEST_GROUP_B_CLIENT_C
     *
     * 因为 GROUP_B 下只有 CLIENT_C 一个消费者，故 CLIENT_C 会消费所有发送到 SPRING_TEST_TOPIC 的消息
     */
    @KafkaListener(topics = {SPRING_TEST_TOPIC}, groupId = SPRING_TEST_GROUP_B, id = SPRING_TEST_GROUP_B_CLIENT_C)
    public void consumeC(ConsumerRecord<String, String> record) {
        log.info("消费者 C 消费消息：{}", record.toString());
    }

}
