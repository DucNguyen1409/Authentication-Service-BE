package com.example.spring.authentication.service;

import com.example.spring.authentication.dto.CustomerDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

public interface MailService {

    void sendUserRegisterEmail(final CustomerDto customerDto) throws MessagingException;

    JavaMailSenderImpl getMailSender();

    MimeMessage getMimeMessage(final JavaMailSenderImpl mailSender, final CustomerDto customerDto,
                               final String templateName, final String subject) throws MessagingException;
}
