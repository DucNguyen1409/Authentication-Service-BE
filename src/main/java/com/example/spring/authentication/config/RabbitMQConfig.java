package com.example.spring.authentication.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String TOPIC_EXCHANGE_NAME = "RegisterEmailTopicExchange";
    public static final String QUEUE_NAME = "EmailRegister";
    public static final String ROUTING_KEY = "register.email.#";

    @Bean
    Queue queue() {
        return new Queue(QUEUE_NAME, false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(TOPIC_EXCHANGE_NAME);
    }

    @Bean
    Binding bindingSendRegisterEmail(Queue queue, TopicExchange topicExchange) {
        return BindingBuilder
                .bind(queue)
                .to(exchange())
                .with(ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        converter.setClassMapper(typeMapper());

        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public DefaultJackson2JavaTypeMapper typeMapper() {
        DefaultJackson2JavaTypeMapper mapper = new DefaultJackson2JavaTypeMapper();
        mapper.setTrustedPackages("com.example.spring.authentication.dto");

        return mapper;
    }

}
