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

    public static final String TOPIC_REGISTER_EXCHANGE_NAME = "RegisterEmailTopicExchange";
    public static final String TOPIC_ACTIVATE_EXCHANGE_NAME = "ActivateEmailTopicExchange";
    public static final String QUEUE_USER_REGISTER_NAME = "EmailRegister";
    public static final String QUEUE_ACTIVATION_ACCOUNT_NAME = "EmailActivate";
    public static final String REGISTER_ROUTING_KEY = "register.email.#";
    public static final String ACTIVATE_ROUTING_KEY = "activate.email.#";

    @Bean
    Queue registerQueue() {
        return new Queue(QUEUE_USER_REGISTER_NAME, false);
    }

    @Bean
    Queue activateQueue() {
        return new Queue(QUEUE_ACTIVATION_ACCOUNT_NAME, false);
    }

    @Bean
    TopicExchange registerExchange() {
        return new TopicExchange(TOPIC_REGISTER_EXCHANGE_NAME);
    }

    @Bean
    TopicExchange activateExchange() {
        return new TopicExchange(TOPIC_ACTIVATE_EXCHANGE_NAME);
    }

    @Bean
    Binding bindingSendRegisterEmail() {
        return BindingBuilder
                .bind(registerQueue())
                .to(registerExchange())
                .with(REGISTER_ROUTING_KEY);
    }

    @Bean
    Binding bindingSendActivateEmail() {
        return BindingBuilder
                .bind(activateQueue())
                .to(activateExchange())
                .with(ACTIVATE_ROUTING_KEY);
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
        mapper.setTrustedPackages("com.example.spring.authentication.dto",
                                "com.example.spring.authentication.ActivateAccountDto");

        return mapper;
    }

}
