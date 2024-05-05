package com.example.spring.authentication.service;

import com.example.spring.authentication.dto.ActivateAccountDto;
import com.example.spring.authentication.dto.CustomerDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Map;

public interface MailService {

    void sendUserRegisterEmail(final CustomerDto customerDto) throws MessagingException;

    void sendActivationAccountEmail(final ActivateAccountDto activateAccountDto) throws MessagingException;

    JavaMailSenderImpl getMailSender();

    MimeMessage getMimeMessage(final JavaMailSenderImpl mailSender, final Map<String, Object> properties,
                               final String emailTo, final String templateName, final String subject)
            throws MessagingException;
}
