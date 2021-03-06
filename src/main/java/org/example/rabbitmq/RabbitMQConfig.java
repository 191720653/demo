package org.example.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class RabbitMQConfig {

    /**
     * 消息发送工具
     *
     * rabbitmq保证一条消息只会被队列的一个消费者消费（一个队列可以有多个消费者）
     *
     * @param connectionFactory
     * @return
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        // 消息发送到rabbitmq服务器回调
        template.setConfirmCallback((correlationData, ack, cause) -> {
            // 发送消息时，可以指定correlationData
            log.info("发送回调，data：{}，ack：{}，cause：{}", correlationData, ack, cause);
        });
        // 失败回调（发送到队列失败），发送回调与失败回调一起使用，可以确认消息是否已经成功发送到mq相应的队列
        template.setMandatory(true);
        template.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            log.info("失败回调，message：{}，replyCode：{}，replyText：{}，exchange：{}，routingKey：{}", message, replyCode, replyText, exchange, routingKey);
        });
        return template;
    }

    /**
     * direct类型交换机
     * 消息的routingKey与交换机的routingKey匹配上（完全一样），则转发消息给队列。
     *
     * 如果两个队列（A、B）绑定同一个交换机，使用相同的routingKey，是否都会接收到消息？
     * 会
     */
    public static final String DIRECT_EXCHANGE = "DIRECT_EXCHANGE";
    public static final String DIRECT_QUEUE_A = "DIRECT_QUEUE_A";
    public static final String DIRECT_QUEUE_B = "DIRECT_QUEUE_B";
    public static final String DIRECT_ROUTING_KEY = "DIRECT_ROUTING_KEY";

    @Bean
    public DirectExchange directExchange() {
        // direct交换机
        return new DirectExchange(DIRECT_EXCHANGE);
    }

    @Bean
    public Queue directQueueA() {
        // 名字，是否持久化
        return new Queue(DIRECT_QUEUE_A, true);
    }

    @Bean
    public Binding bindingDirectA() {
        // 绑定一个队列，to: 绑定到哪个交换机上面，with：绑定的路由建（routingKey）
        return BindingBuilder.bind(directQueueA()).to(directExchange()).with(DIRECT_ROUTING_KEY);
    }

    @Bean
    public Queue directQueueB() {
        // 名字，是否持久化
        return new Queue(DIRECT_QUEUE_B, true);
    }

    @Bean
    public Binding bindingDirectB() {
        // 绑定一个队列，to: 绑定到哪个交换机上面，with：绑定的路由建（routingKey）
        return BindingBuilder.bind(directQueueB()).to(directExchange()).with(DIRECT_ROUTING_KEY);
    }

    /**
     * topic类型交换机，通过*、#来匹配消息的routingKey，根据自己需要消费消息
     * [*]：匹配一个单词（单词间通过[.]分隔）
     * [#]：匹配任意数量单词
     */
    public static final String TOPIC_EXCHANGE = "TOPIC_EXCHANGE";
    public static final String TOPIC_LOG_QUEUE = "TOPIC_LOG_QUEUE";
    public static final String TOPIC_lOG_INFO_QUEUE = "TOPIC_LOG_INFO_QUEUE";
    public static final String TOPIC_ROUTING_KEY_ALL_LOG = "TOPIC_ROUTING_KEY.#";
    public static final String TOPIC_ROUTING_KEY_INFO_LOG = "TOPIC_ROUTING_KEY.INFO.*";

    @Bean
    public TopicExchange topicExchange() {
        // topic交换机
        return new TopicExchange(TOPIC_EXCHANGE);
    }

    @Bean
    public Queue topicLogQueue() {
        // 名字，是否持久化，接收所有日志消息队列
        return new Queue(TOPIC_LOG_QUEUE, true);
    }

    @Bean
    public Binding bindingLogTopic() {
        // 绑定一个队列，to: 绑定到哪个交换机上面，with：绑定的路由建（routingKey）
        return BindingBuilder.bind(topicLogQueue()).to(topicExchange()).with(TOPIC_ROUTING_KEY_ALL_LOG);
    }

    @Bean
    public Queue topicLogInfoQueue() {
        // 名字，是否持久化，接收info级别日志消息
        return new Queue(TOPIC_lOG_INFO_QUEUE, true);
    }

    @Bean
    public Binding bindingLogInfoTopic() {
        // 绑定一个队列，to: 绑定到哪个交换机上面，with：绑定的路由建（routingKey）
        return BindingBuilder.bind(topicLogInfoQueue()).to(topicExchange()).with(TOPIC_ROUTING_KEY_INFO_LOG);
    }

    /**
     * fanout类型交换机，没有路由规则，发消息不用指定routingKey
     * 会把消息发送给所有绑定此交换机的队列（广播模式/发布订阅模式）
     */
    public static final String FANOUT_EXCHANGE = "FANOUT_EXCHANGE";
    public static final String FANOUT_QUEUE_A = "FANOUT_QUEUE_A";
    public static final String FANOUT_QUEUE_B = "FANOUT_QUEUE_B";

    @Bean
    public FanoutExchange fanoutExchange() {
        // fanout交换机
        return new FanoutExchange(FANOUT_EXCHANGE);
    }

    @Bean
    public Queue fanoutQueueA() {
        // 名字，是否持久化
        return new Queue(FANOUT_QUEUE_A, true);
    }

    @Bean
    public Binding bindingFanoutA() {
        // 绑定一个队列，to: 绑定到哪个交换机上面
        return BindingBuilder.bind(fanoutQueueA()).to(fanoutExchange());
    }

    @Bean
    public Queue fanoutQueueB() {
        // 名字，是否持久化
        return new Queue(FANOUT_QUEUE_B, true);
    }

    @Bean
    public Binding bindingFanoutB() {
        // 绑定一个队列，to: 绑定到哪个交换机上面
        return BindingBuilder.bind(fanoutQueueB()).to(fanoutExchange());
    }

    /**
     * 死信交换机，创建队列的时候设置的附带交换机，当队列的消息发送失败后会交给私死信交换机处理
     * 1、消息被拒绝（basic.reject 或者 basic.nack），并且requeue为false（不重新放回队列）
     * 2、消息的过期时间到了
     * 3、队列长度超过限制了
     * 可以利用消息过期时间来实现延时队列
     * 消息先发到 timeout 队列，等待过期时间 30s 后，由死信交换机发给死信队列处理
     */
    public static final long TIME_OUT_MILLS = 30 * 1000;
    public static final String TIME_OUT_KEY_NAME = "x-message-ttl";
    public static final String DEAD_EXCHANGE_KEY_NAME = "x-dead-letter-exchange";
    public static final String DEAD_ROUTING_KEY_NAME = "x-dead-letter-routing-key";

    public static final String TIME_OUT_QUEUE = "TIME_OUT_QUEUE";
    public static final String TIME_OUT_EXCHANGE = "TIME_OUT_EXCHANGE";
    public static final String TIME_OUT_ROUTING_KEY = "TIME_OUT_ROUTING_KEY";
    public static final String DEAD_QUEUE = "DEAD_QUEUE";
    public static final String DEAD_EXCHANGE = "DEAD_EXCHANGE";
    public static final String DEAD_ROUTING_KEY = "DEAD_ROUTING_KEY";

    @Bean
    public Queue timeoutQueue() {
        Map<String,Object> map = new HashMap<>();
        // 设置消息的过期时间，单位毫秒
        map.put(TIME_OUT_KEY_NAME, TIME_OUT_MILLS);
        // 设置死信交换机
        map.put(DEAD_EXCHANGE_KEY_NAME, DEAD_EXCHANGE);
        // 指定死信交换机的路由
        map.put(DEAD_ROUTING_KEY_NAME, DEAD_ROUTING_KEY);
        // 名字，是否持久化，是否仅声明队列的连接可见（其它连接不可见，断开连接后队列自动删除），是否自动删除（所有消费者断开连接后自动删除）
        return new Queue(TIME_OUT_QUEUE, true, false, false, map);
    }

    @Bean
    public DirectExchange timeoutExchange() {
        // direct交换机
        return new DirectExchange(TIME_OUT_EXCHANGE);
    }

    @Bean
    public Binding bindingTimeout() {
        // 绑定一个队列，to: 绑定到哪个交换机上面，with：绑定的路由建（routingKey）
        return BindingBuilder.bind(timeoutQueue()).to(timeoutExchange()).with(TIME_OUT_ROUTING_KEY);
    }

    @Bean
    public Queue deadQueue() {
        // 名字，是否持久化
        return new Queue(DEAD_QUEUE, true);
    }

    @Bean
    public DirectExchange deadExchange() {
        // direct交换机
        return new DirectExchange(DEAD_EXCHANGE);
    }

    @Bean
    public Binding bindingDead() {
        // 绑定一个队列，to: 绑定到哪个交换机上面，with：绑定的路由建（routingKey）
        return BindingBuilder.bind(deadQueue()).to(deadExchange()).with(DEAD_ROUTING_KEY);
    }

}
