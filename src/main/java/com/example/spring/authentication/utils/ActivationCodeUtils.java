package com.example.spring.authentication.utils;

import java.security.SecureRandom;

public class ActivationCodeUtils {

    private static final String CHARACTER_CODE = "0123456789";

    public static String generateActivationCode(int length) {
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(CHARACTER_CODE.length());
            codeBuilder.append(randomIndex);
        }

        return codeBuilder.toString();
    }

}
