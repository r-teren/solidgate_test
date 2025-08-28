package com.solidgate.utils;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class SignatureGenerator {


    public static String generateSignature(String publicKey, String jsonString, String secretKey) {
        String text = publicKey + (jsonString == null ? "" : jsonString) + publicKey;
        byte[] hashedBytes = Hashing.hmacSha512(secretKey.getBytes())
                .hashString(text, StandardCharsets.UTF_8).toString().getBytes();

        return Base64.getEncoder().encodeToString(hashedBytes);
    }


}
