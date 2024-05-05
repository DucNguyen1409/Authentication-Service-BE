package com.example.spring.authentication.rabbitmq;

import com.example.spring.authentication.config.RabbitMQConfig;
import com.example.spring.authentication.dto.ActivateAccountDto;
import com.example.spring.authentication.dto.CustomerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class Producer {

    private final RabbitTemplate rabbitTemplate;

    private final Consumer consumer;

    private final RabbitMQConfig rabbitMQConfig;

    public void send(CustomerDto dto) {
        log.info("[Producer] send: {}", dto);
        rabbitTemplate.convertAndSend(RabbitMQConfig.TOPIC_REGISTER_EXCHANGE_NAME, "register.email.customer", dto);
    }

    public void send(ActivateAccountDto dto) {
        log.info("[Producer] send: {}", dto);
        rabbitTemplate.convertAndSend(RabbitMQConfig.TOPIC_ACTIVATE_EXCHANGE_NAME, "activate.email.account", dto);
    }
}
