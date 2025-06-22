package com.notifyme.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String TOPIC_EXCHANGE = "notifyme.notification.exchange";

    // Main queues
    public static final String EMAIL_QUEUE = "notifyme.email.queue";
    public static final String SMS_QUEUE = "notifyme.sms.queue";
    public static final String PUSH_QUEUE = "notifyme.push.queue";

    // Retry queues
    public static final String RETRY_EMAIL_QUEUE = "notifyme.retry.email.queue";
    public static final String RETRY_SMS_QUEUE = "notifyme.retry.sms.queue";
    public static final String RETRY_PUSH_QUEUE = "notifyme.retry.push.queue";

    // Routing keys
    public static final String EMAIL_ROUTING = "notify.email.send";
    public static final String SMS_ROUTING = "notify.sms.send";
    public static final String PUSH_ROUTING = "notify.push.send";

    public static final String EMAIL_RETRY_ROUTING = "notify.email.retry";
    public static final String SMS_RETRY_ROUTING = "notify.sms.retry";
    public static final String PUSH_RETRY_ROUTING = "notify.push.retry";

    // TTL for retry
    public static final int RETRY_DELAY_MS = 10000;

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE);
    }

    // Main queues
    @Bean public Queue emailQueue() { return new Queue(EMAIL_QUEUE, true); }
    @Bean public Queue smsQueue() { return new Queue(SMS_QUEUE, true); }
    @Bean public Queue pushQueue() { return new Queue(PUSH_QUEUE, true); }

    // Retry queues with DLX and TTL
    @Bean
    public Queue emailRetryQueue() {
        return QueueBuilder.durable(RETRY_EMAIL_QUEUE)
                .withArgument("x-dead-letter-exchange", TOPIC_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", EMAIL_ROUTING)
                .withArgument("x-message-ttl", RETRY_DELAY_MS)
                .build();
    }

    @Bean
    public Queue smsRetryQueue() {
        return QueueBuilder.durable(RETRY_SMS_QUEUE)
                .withArgument("x-dead-letter-exchange", TOPIC_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", SMS_ROUTING)
                .withArgument("x-message-ttl", RETRY_DELAY_MS)
                .build();
    }

    @Bean
    public Queue pushRetryQueue() {
        return QueueBuilder.durable(RETRY_PUSH_QUEUE)
                .withArgument("x-dead-letter-exchange", TOPIC_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", PUSH_ROUTING)
                .withArgument("x-message-ttl", RETRY_DELAY_MS)
                .build();
    }

    // Bindings for main queues
    @Bean public Binding emailBinding() {
        return BindingBuilder.bind(emailQueue()).to(topicExchange()).with(EMAIL_ROUTING);
    }
    @Bean public Binding smsBinding() {
        return BindingBuilder.bind(smsQueue()).to(topicExchange()).with(SMS_ROUTING);
    }
    @Bean public Binding pushBinding() {
        return BindingBuilder.bind(pushQueue()).to(topicExchange()).with(PUSH_ROUTING);
    }

    // Bindings for retry queues
    @Bean public Binding emailRetryBinding() {
        return BindingBuilder.bind(emailRetryQueue()).to(topicExchange()).with(EMAIL_RETRY_ROUTING);
    }
    @Bean public Binding smsRetryBinding() {
        return BindingBuilder.bind(smsRetryQueue()).to(topicExchange()).with(SMS_RETRY_ROUTING);
    }
    @Bean public Binding pushRetryBinding() {
        return BindingBuilder.bind(pushRetryQueue()).to(topicExchange()).with(PUSH_RETRY_ROUTING);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}
