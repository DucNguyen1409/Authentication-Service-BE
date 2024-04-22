package com.example.spring.authentication.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constant {
    public static final String USER_API_URL = "/api/v1/**";
    public static final String ADMIN_API_URL = "/api/v1/admin/**";
    public static final String LOG_OUT_URL = "/api/v1/auth/logout";

    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";
    public static final int JWT_BEARER_INDEX = 7;

    public static final String MAIL_TRANSPORT_PROTOCOL = "mail.transport.protocol";
    public static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
    public static final String MAIL_SMTP_STARTTLS = "mail.smtp.starttls.enable";

    public static final String USER_NAME_PROPERTY = "userName";
}
