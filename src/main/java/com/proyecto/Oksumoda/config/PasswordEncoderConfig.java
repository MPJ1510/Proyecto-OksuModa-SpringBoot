package com.proyecto.Oksumoda.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration // Indica que esta clase define beans
public class PasswordEncoderConfig {

    @Bean // Define el bean del codificador
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}