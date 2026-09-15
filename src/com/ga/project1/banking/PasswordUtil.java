package com.ga.project1.banking;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordUtil {
    private static final int SALT_LENGTH = 16;
    private static final int ITERATIONS = 210000;
    private static final int KEY_LENGTH = 256;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";

    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public static String hashPassword(String password, String salt) {
        try {
            byte[] saltBytes = Base64.getDecoder().decode(salt);
            PBEKeySpec spec = new PBEKeySpec(
                    password.toCharArray(),
                    saltBytes,
                    ITERATIONS,
                    KEY_LENGTH
            );

            SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);

            byte[] hash = factory
                    .generateSecret(spec)
                    .getEncoded();

            spec.clearPassword();

            return Base64.getEncoder()
                    .encodeToString(hash);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Error hashing password", e
            );
        }
    }

    public static boolean verifyPassword(
            String password,
            String salt,
            String storedHash) {

        String newHash = hashPassword(password, salt);

        return MessageDigest.isEqual(
                newHash.getBytes(),
                storedHash.getBytes()
        );
    }
}