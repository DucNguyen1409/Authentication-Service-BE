package com.example.spring.authentication.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

public interface MailService {

    void sendUserRegisterEmail(final String mailTo, final String userName) throws MessagingException;

    JavaMailSenderImpl getMailSender();

    MimeMessage getMimeMessage(final JavaMailSenderImpl mailSender, final String recipient, final String userName,
                               final String templateName, final String subject) throws MessagingException;
}
