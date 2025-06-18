package com.notifyme.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String NOTIFY_QUEUE = "notifyme.notification.queue";
    public static final String NOTIFY_EXCHANGE = "notifyme.notification.exchange";
    public static final String NOTIFY_ROUTING_KEY = "notifyme.notification.key";

    @Bean
    public Queue queue() {
        return new Queue(NOTIFY_QUEUE);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(NOTIFY_EXCHANGE);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(NOTIFY_ROUTING_KEY);
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
