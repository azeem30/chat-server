package com.example.server.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import java.io.ByteArrayOutputStream;

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

    public static String compress(String encrypted) {
        try {
            byte[] inputBytes = encrypted.getBytes(StandardCharsets.UTF_8);
            Deflater deflater = new Deflater(6);
            deflater.setInput(inputBytes);
            deflater.finish();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            while (!deflater.finished()) {
                int count = deflater.deflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            deflater.end();
            byte[] compressedBytes = outputStream.toByteArray();
            String compressedString = Base64.getEncoder().encodeToString(compressedBytes);

            if (compressedString.length() > 255) {
                throw new IllegalStateException("Compressed string exceeds 255 characters");
            }
            return compressedString;
        } catch (Exception ex) {
            throw new RuntimeException("Compression error: " + ex.getMessage(), ex);
        }
    }

    public static String decompress(String compressed) {
        try {
            byte[] compressedBytes = Base64.getDecoder().decode(compressed);
            Inflater inflater = new Inflater();
            inflater.setInput(compressedBytes);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            inflater.end();
            return new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
        } catch (Exception ex) {
            throw new RuntimeException("Decompression error: " + ex.getMessage(), ex);
        }
    }
}
