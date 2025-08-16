package com.example.server.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Cryptography {
    private static String secretKey = System.getProperty("SECRET_KEY");
    private static String initVector = System.getProperty("INIT_VECTOR");

    public static String encrypt(String value) {
        try {
            if (secretKey == null || initVector == null) {
                throw new IllegalStateException("Secret key or init vector not initialized");
            }
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec skeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception ex) {
            throw new RuntimeException("Encryption error: " + ex.getMessage(), ex);
        }
    }

    public static String decrypt(String encrypted) {
        try {
            if (secretKey == null || initVector == null) {
                throw new IllegalStateException("Secret key or init vector not initialized");
            }
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec skeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.getDecoder().decode(encrypted));
            return new String(original);
        } catch (Exception ex) {
            throw new RuntimeException("Decryption error: " + ex.getMessage(), ex);
        }
    }
}
