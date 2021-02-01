package org.example.kafka;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class KafkaRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {

        // 启动生产者，发送两条消息
        new Thread(new OriginalProducer()).start();

        // A、B都在消费者组A下，故消息要么被A消费，要么被B消费
        // 启动消费者A
        new Thread(new OriginalConsumer(OriginalConsumer.GROUP_ID_A, OriginalConsumer.CLIENT_ID_A)).start();
        // 启动消费者B
        new Thread(new OriginalConsumer(OriginalConsumer.GROUP_ID_A, OriginalConsumer.CLIENT_ID_B)).start();

        // 启动消费者C，在消费者组B下，可以消费到两条消息
        new Thread(new OriginalConsumer(OriginalConsumer.GROUP_ID_B, OriginalConsumer.CLIENT_ID_C)).start();

    }

}
