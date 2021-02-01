package org.example.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class OriginalConsumer implements Runnable {

    private static final String TOPIC = "TEST_TOPIC";

    private static final String BROKER_LIST = "192.168.3.4:9092,192.168.3.5:9092,192.168.3.6:9092";

    /**
     * 一个主题下可以有多个分区，可以有多个消费者组消费消息，不同消费者组的消费者可以消费同一个分区的消息（各自维护一个offSet）
     * 一个消费者组下可以有多个消费者
     * 一个消费者可以消费一个或多个分区，一个分区只能由同个消费者组下的一个消费者消费
     * 同一个消费组下，消费者数应小于等于分区数
     *
     * 正常消息队列（消息只消费一次）
     * 可以设置一个主题，一个消费者组，消费者组下多个消费者
     *
     * 发布订阅（广播模式）
     * 可以设置一个主题，多个消费者组，每个消费者组下一个或多个消费者
     */

    /**
     * 消费者组：A
     */
    public static final String GROUP_ID_A = "TEST_GROUP_A";
    /**
     * 消费者组：B
     */
    public static final String GROUP_ID_B = "TEST_GROUP_B";

    /**
     * 消费者：A
     */
    public static final String CLIENT_ID_A = "TEST_CLIENT_A";
    /**
     * 消费者：B
     */
    public static final String CLIENT_ID_B = "TEST_CLIENT_B";
    /**
     * 消费者：C
     */
    public static final String CLIENT_ID_C = "TEST_CLIENT_C";

    private KafkaConsumer<String, String> consumer;

    private String clientId;

    public OriginalConsumer(String groupId, String clientId) {
        this.clientId = clientId;
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BROKER_LIST);
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, clientId);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumer = new KafkaConsumer<String, String>(properties);
        consumer.subscribe(Arrays.asList(TOPIC));
    }

    private void consume() {
        // 获取消息，超时时间：1S
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1L));
        records.forEach(record -> {
            System.out.println(clientId + "-消费消息：" + record.toString());
        });
    }

    @Override
    public void run() {
        try {
            while (true) {
                consume();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            consumer.close();
        }
    }
}
