package com.evo.storage.infrastructure.configuration;

import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class HashingUtils {

    public String hashWithMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();

            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing file name with MD5", e);
        }
    }
}
