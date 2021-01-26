package org.example.kafka;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class KafkaRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {

        // 启动生产者
        OriginalProducer.produce();

        // 启动消费者
        OriginalConsumer.consume();

    }

}
