package com.example.spring.authentication.service.impl;

import com.example.spring.authentication.common.Constant;
import com.example.spring.authentication.config.MailConfig;
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

    private final MailConfig mailConfig;

    private final SpringTemplateEngine templateEngine;

    @Override
    public void sendUserRegisterEmail(String mailTo, String userName) throws MessagingException {
        log.info("[MailServiceImpl] sendUserRegisterEmail");
        // get mime message
        JavaMailSenderImpl mailSender = getMailSender();
        String subject = "Register Success";
        MimeMessage mimeMessage = getMimeMessage(mailSender, mailTo,
                userName, mailConfig.getRegisterTemplateName(), subject);

        // send mail
        mailSender.send(mimeMessage);
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
    public MimeMessage getMimeMessage(JavaMailSenderImpl mailSender, String recipient, String userName,
                                      String templateName, String subject) throws MessagingException {
        // set mailer sender
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        Map<String, Object> properties = new HashMap<>();
        properties.put(Constant.USER_NAME_PROPERTY, userName);

        // set username
        Context context = new Context();
        context.setVariables(properties);

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, mailConfig.getEncoding());
        helper.setFrom(mailConfig.getUserName());
        helper.setTo(InternetAddress.parse(recipient));
        helper.setSubject(subject);
        helper.setText(templateEngine.process(templateName, context), true);

        return mimeMessage;
    }

}
