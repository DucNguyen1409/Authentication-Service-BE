package com.example.spring.authentication.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class MailConfig {

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private String port;

    @Value("${spring.mail.username}")
    private String userName;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.protocol}")
    private String protocol;

    @Value("${spring.mail.properties.mail.auth}")
    private String auth;

    @Value("${spring.mail.properties.mail.smtp.starttls}")
    private String starttls;

    @Value("${spring.mail.default-encoding}")
    private String encoding;

    @Value("${spring.mail.template.register-template-name}")
    private String registerTemplateName;

    @Value("${spring.mail.template.activate-template-name}")
    private String activateTemplateName;

}
