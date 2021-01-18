package org.example.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Consumer {

    @RabbitListener(queues = RabbitMQConfig.DIRECT_QUEUE_A)
    public void consumerDirectA_1(String message) throws Exception {
        log.info("队列：{}，1，成功消费消息：{}", RabbitMQConfig.DIRECT_QUEUE_A, message);
    }
    
    @RabbitListener(queues = RabbitMQConfig.DIRECT_QUEUE_A)
    public void consumerDirectA_2(String message) throws Exception {
        log.info("队列：{}，2，成功消费消息：{}", RabbitMQConfig.DIRECT_QUEUE_A, message);
    }

    @RabbitListener(queues = RabbitMQConfig.DIRECT_QUEUE_B)
    public void consumerDirectB(String message) throws Exception {
        log.info("队列：{}，成功消费消息：{}", RabbitMQConfig.DIRECT_QUEUE_B, message);
    }

    @RabbitListener(queues = RabbitMQConfig.TOPIC_LOG_QUEUE)
    public void consumerTopicLog(String message) throws Exception {
        log.info("队列：{}，成功消费消息：{}", RabbitMQConfig.TOPIC_LOG_QUEUE, message);
    }

    @RabbitListener(queues = RabbitMQConfig.TOPIC_lOG_INFO_QUEUE)
    public void consumerTopicLogInfo(String message) throws Exception {
        log.info("队列：{}，成功消费消息：{}", RabbitMQConfig.TOPIC_lOG_INFO_QUEUE, message);
    }

    @RabbitListener(queues = RabbitMQConfig.FANOUT_QUEUE_A)
    public void consumerFanoutA_1(String message) throws Exception {
        log.info("队列：{}，1，成功消费消息：{}", RabbitMQConfig.FANOUT_QUEUE_A, message);
    }

    @RabbitListener(queues = RabbitMQConfig.FANOUT_QUEUE_A)
    public void consumerFanoutA_2(String message) throws Exception {
        log.info("队列：{}，2，成功消费消息：{}", RabbitMQConfig.FANOUT_QUEUE_A, message);
    }

    @RabbitListener(queues = RabbitMQConfig.FANOUT_QUEUE_B)
    public void consumerFanoutB(String message) throws Exception {
        log.info("队列：{}，成功消费消息：{}", RabbitMQConfig.FANOUT_QUEUE_B, message);
    }

    @RabbitListener(queues = RabbitMQConfig.DEAD_QUEUE)
    public void consumerDead(String message) throws Exception {
        log.info("队列：{}，成功消费消息：{}", RabbitMQConfig.DEAD_QUEUE, message);
    }

}
