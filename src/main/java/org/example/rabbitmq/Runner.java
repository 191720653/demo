package org.example.rabbitmq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class Runner implements ApplicationRunner {

    @Autowired
    private Producer producer;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // direct 类型，发消息，交换机会转发给路由键一样的队列
        producer.send(RabbitMQConfig.DIRECT_EXCHANGE, RabbitMQConfig.DIRECT_ROUTING_KEY, "This is a direct message !");
        Thread.sleep(1000);

        // 测试消息 发送回调（没有对应的交换机）
        producer.send("nothing", RabbitMQConfig.DIRECT_ROUTING_KEY, "This is a message ! without exchange");
        Thread.sleep(1000);

        // 测试消息 失败回调（消息已经到达交换机，但没有找到路由键对应的队列）
        producer.send(RabbitMQConfig.DIRECT_EXCHANGE, "nothing", "This is a message ! without routingKey");
        Thread.sleep(1000);

        // topic 类型，发消息，交换机会把消息发送给路由键匹配上的队列
        producer.send(RabbitMQConfig.TOPIC_EXCHANGE, "TOPIC_ROUTING_KEY.INFO.ORDER", "This is a topic message for log info order!");
        Thread.sleep(1000);
        producer.send(RabbitMQConfig.TOPIC_EXCHANGE, "TOPIC_ROUTING_KEY.ERROR.ORDER", "This is a topic message for log error order!");
        Thread.sleep(1000);

        // fanout 类型，发消息，交换机直接把消息转发给绑定的队列
        producer.send(RabbitMQConfig.FANOUT_EXCHANGE, null, "This is a fanout message !");
        Thread.sleep(1000);

        // 发送延时消息（死信交换机实现）
        producer.send(RabbitMQConfig.TIME_OUT_EXCHANGE, RabbitMQConfig.TIME_OUT_ROUTING_KEY, "This is a dead for delay message !");
        Thread.sleep(1000);

    }

}
