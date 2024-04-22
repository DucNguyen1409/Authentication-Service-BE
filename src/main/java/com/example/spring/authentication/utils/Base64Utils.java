package com.example.spring.authentication.utils;

import java.util.Base64;

public class Base64Utils {

    public static String base64Encode(final String originalString) {
        return Base64.getEncoder().encodeToString(originalString.getBytes());
    }
    public static String base64Decode(final String encodedString) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        return new String(decodedBytes);
    }

}
