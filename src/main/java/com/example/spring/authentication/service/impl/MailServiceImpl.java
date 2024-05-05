package com.example.spring.authentication.service.impl;

import com.example.spring.authentication.common.Constant;
import com.example.spring.authentication.config.MailConfig;
import com.example.spring.authentication.dto.ActivateAccountDto;
import com.example.spring.authentication.dto.CustomerDto;
import com.example.spring.authentication.service.MailService;
import com.example.spring.authentication.utils.Base64Utils;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private static final String MAIL_REGISTER_TITLE = "Account Registration Successful";
    private static final String MAIL_ACTIVATION_ACCOUNT_TITLE = "Activation your account";

    private final MailConfig mailConfig;

    private final SpringTemplateEngine templateEngine;

    @Override
    public void sendUserRegisterEmail(CustomerDto customerDto) throws MessagingException {
        log.info("[MailServiceImpl] sendUserRegisterEmail: {}", customerDto);
        // get mime message
        JavaMailSenderImpl mailSender = getMailSender();
        MimeMessage mimeMessage = getMimeMessage(mailSender, getUserRegisterMailProperty(customerDto),
                customerDto.getEmail(), mailConfig.getRegisterTemplateName(), MAIL_REGISTER_TITLE);

        // send mail
        mailSender.send(mimeMessage);
        log.info("[MailServiceImpl] sendUserRegisterEmail::done send mail to: {}", customerDto.getEmail());
    }

    @Override
    public void sendActivationAccountEmail(ActivateAccountDto activateAccountDto) throws MessagingException {
        log.info("[MailServiceImpl] sendActivationAccountEmail: {}", activateAccountDto);
        // get mime message
        JavaMailSenderImpl mailSender = getMailSender();
        MimeMessage mimeMessage = getMimeMessage(mailSender, getActivationMailProperty(activateAccountDto),
                activateAccountDto.getEmail(), mailConfig.getActivateTemplateName(), MAIL_ACTIVATION_ACCOUNT_TITLE);

        // send mail
        mailSender.send(mimeMessage);
        log.info("[MailServiceImpl] sendActivationAccountEmail::done send mail to: {}", activateAccountDto.getEmail());
    }

    @Override
    public JavaMailSenderImpl getMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailConfig.getHost());
        mailSender.setPort(Integer.parseInt(mailConfig.getPort()));
        mailSender.setUsername(mailConfig.getUserName());
        mailSender.setPassword(Base64Utils.base64Decode(mailConfig.getPassword()));

        Properties props = mailSender.getJavaMailProperties();
        props.put(Constant.MAIL_TRANSPORT_PROTOCOL, mailConfig.getProtocol());
        props.put(Constant.MAIL_SMTP_AUTH, mailConfig.getAuth());
        props.put(Constant.MAIL_SMTP_STARTTLS, mailConfig.getStarttls());

        return mailSender;
    }

    @Override
    public MimeMessage getMimeMessage(JavaMailSenderImpl mailSender,
                                      Map<String, Object> properties,
                                      String emailTo,
                                      String templateName,
                                      String subject) throws MessagingException {
        // set mailer sender
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        // set username
        Context context = new Context();
        context.setVariables(properties);

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, mailConfig.getEncoding());
        helper.setFrom(mailConfig.getUserName());
        helper.setTo(InternetAddress.parse(emailTo));
        helper.setSubject(subject);
        helper.setText(templateEngine.process(templateName, context), true);

        return mimeMessage;
    }

    private Map<String, Object> getUserRegisterMailProperty(CustomerDto customerDto) {
        Map<String, Object> properties = new HashMap<>();
        properties.put(Constant.MailTemplateProperty.NAME_PROPERTY, customerDto.getName());
        properties.put(Constant.MailTemplateProperty.EMAIL_PROPERTY, customerDto.getEmail());
        properties.put(Constant.MailTemplateProperty.ROLE_PROPERTY, customerDto.getRole());

        return properties;
    }

    private Map<String, Object> getActivationMailProperty(ActivateAccountDto dto) {
        Map<String, Object> properties = new HashMap<>();
        properties.put(Constant.MailTemplateProperty.NAME_PROPERTY, dto.getName());
        properties.put(Constant.MailTemplateProperty.CODE_PROPERTY, dto.getCode());
        properties.put(Constant.MailTemplateProperty.CONFIRM_URL_PROPERTY, dto.getConfirmUrl());

        return properties;
    }

}
