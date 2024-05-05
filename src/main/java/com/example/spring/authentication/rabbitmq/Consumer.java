package com.example.spring.authentication.rabbitmq;

import com.example.spring.authentication.dto.ActivateAccountDto;
import com.example.spring.authentication.dto.CustomerDto;
import com.example.spring.authentication.service.MailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class Consumer {

    private final MailService mailService;

    @RabbitListener(queues = "EmailRegister")
    public void receiveUserRegister(final CustomerDto dto) throws MessagingException {
        log.info("[Consumer] receiveUserRegister: {}", dto);
        mailService.sendUserRegisterEmail(dto);
    }

    @RabbitListener(queues = "EmailActivate")
    public void receiveActivateAccount(final ActivateAccountDto dto) throws MessagingException {
        log.info("[Consumer] receiveActivateAccount: {}", dto);
        mailService.sendActivationAccountEmail(dto);
    }

}
