package org.example.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Consumer {

    /**
     * 交换机（DIRECT_EXCHANGE）收到一条消息（message，路由键：DIRECT_ROUTING_KEY）
     *
     * 队列（DIRECT_QUEUE_B）和队列（DIRECT_QUEUE_A）都绑定了交换机（DIRECT_EXCHANGE），同时使用了相同的路由键（DIRECT_ROUTING_KEY）
     * 所以交换机（DIRECT_EXCHANGE）会同时把消息（message）转发给两个队列
     *
     * 消费者（DirectA_1）、消费者（DirectA_2）消费同一个队列（DIRECT_QUEUE_A）
     * 所以队列（DIRECT_QUEUE_A）的消息（message）要么被消费者（DirectA_1）消费，要么被消费者（DirectA_2）消费
     *
     * 队列（DIRECT_QUEUE_B）只有一个消费者，所以消费者（DirectB）也会消费到消息（message）
     *
     * @param message
     * @throws Exception
     */
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

    /**
     * 交换机（TOPIC_EXCHANGE）收到两条消息（m1，路由：TOPIC_ROUTING_KEY.ERROR.PAY）、（m2，路由：TOPIC_ROUTING_KEY.INFO.ORDER）
     * m1、m2都匹配路由（TOPIC_ROUTING_KEY.#），同时m2还匹配路由（TOPIC_ROUTING_KEY.INFO.*）
     * 所以队列（TOPIC_LOG_QUEUE，路由TOPIC_ROUTING_KEY.#）会收到m1、m2两条消息
     * 队列（TOPIC_LOG_INFO_QUEUE，路由：TOPIC_ROUTING_KEY.INFO.*）收到m2一条消息
     *
     * @param message
     * @throws Exception
     */
    @RabbitListener(queues = RabbitMQConfig.TOPIC_LOG_QUEUE)
    public void consumerTopicLog(String message) throws Exception {
        log.info("队列：{}，成功消费消息：{}", RabbitMQConfig.TOPIC_LOG_QUEUE, message);
    }

    @RabbitListener(queues = RabbitMQConfig.TOPIC_lOG_INFO_QUEUE)
    public void consumerTopicLogInfo(String message) throws Exception {
        log.info("队列：{}，成功消费消息：{}", RabbitMQConfig.TOPIC_lOG_INFO_QUEUE, message);
    }

    /**
     * 交换机（FANOUT_EXCHANGE）会把消息（m1）转发给和它绑定的所有队列
     * 即队列（FANOUT_QUEUE_A）、队列（FANOUT_QUEUE_B）都会收到消息（m1）
     * 但队列（FANOUT_QUEUE_A）的消息（m1）要么被消费者（FanoutA_1）消费，要么被消费者（FanoutA_2）消费
     *
     * @param message
     * @throws Exception
     */
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

    /**
     * 消费延时后的消息
     * @param message
     * @throws Exception
     */
    @RabbitListener(queues = RabbitMQConfig.DEAD_QUEUE)
    public void consumerDead(String message) throws Exception {
        log.info("队列：{}，成功消费消息：{}", RabbitMQConfig.DEAD_QUEUE, message);
    }

}
