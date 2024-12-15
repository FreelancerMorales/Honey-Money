package com.honeymoney.Honey_Money.util.BCryptHasher;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptHasher {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // Método estático para encriptar contraseñas
    public static String hashPassword(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    // Método para verificar contraseñas
    public static boolean checkPassword(String rawPassword, String hashedPassword) {
        return encoder.matches(rawPassword, hashedPassword);
    }
}
