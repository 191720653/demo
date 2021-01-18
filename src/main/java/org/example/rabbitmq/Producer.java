package org.example.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Producer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(String exchange, String routingKey, String message) {
        // 参数介绍：交换机名字，路由建，消息内容
        rabbitTemplate.convertAndSend(exchange, routingKey, message, new CorrelationData(message));
        log.info("消息发送成功：{}，{}，{}", exchange, routingKey, message);
    }

}
