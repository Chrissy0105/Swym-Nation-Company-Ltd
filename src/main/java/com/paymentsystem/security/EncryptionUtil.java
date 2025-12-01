package com.paymentsystem.security;

public class EncryptionUtil {
    public static String encrypt(String data) {
        // Lightweight placeholder encryption: Base64 encode.
        // Replace with proper AES and secure key management for production.
        try {
            return java.util.Base64.getEncoder().encodeToString(data.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }

    public static String decrypt(String encryptedData) {
        try {
            byte[] decoded = java.util.Base64.getDecoder().decode(encryptedData);
            return new String(decoded, java.nio.charset.StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Decryption failed", e);
        }
    }
}
