package com.proyecto.Oksumoda.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "admin12345";
        String encodedPassword = encoder.encode(rawPassword);
        
        System.out.println("Contraseña en texto plano: " + rawPassword);
        System.out.println("HASH BCrypt para la DB: " + encodedPassword);
    }
}