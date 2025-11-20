package com.proyecto.Oksumoda.config;

import com.proyecto.Oksumoda.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(usuarioService).passwordEncoder(passwordEncoder); 
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((requests) -> requests
                // Recursos públicos (accesibles sin autenticación)
                .requestMatchers("/", "/registro", "/login", "/contactanos", "/404", "/403",
                                "/css/**", "/js/**", "/imágenes/**", "/images/**").permitAll()
                
                //  Panel de administración - Acepta ADMIN, ADMINISTRADOR
                .requestMatchers("/admin/**").hasAnyRole("ADMIN", "ADMINISTRADOR")
                
                //  Perfil de usuario - Acepta CLIENTE y ADMINISTRADOR
                .requestMatchers("/perfil/**").hasAnyRole("CLIENTE", "ADMINISTRADOR", "ADMIN")
                
                //  Cualquier otra petición requiere autenticación
                .anyRequest().authenticated()
            )
            .formLogin((form) -> form
                .loginPage("/login")
                .permitAll()
                .successHandler((request, response, authentication) -> {
                    System.out.println("========================================");
                    System.out.println(" LOGIN EXITOSO");
                    System.out.println("Usuario: " + authentication.getName());
                    System.out.println("Roles/Authorities: " + authentication.getAuthorities());
                    
                    String redirectUrl = "/";
                    boolean esAdmin = authentication.getAuthorities().stream()
                            .anyMatch(auth -> 
                                auth.getAuthority().equals("ROLE_ADMIN") || 
                                auth.getAuthority().equals("ROLE_ADMINISTRADOR")
                            );
                    
                    boolean esCliente = authentication.getAuthorities().stream()
                            .anyMatch(auth -> auth.getAuthority().equals("ROLE_CLIENTE"));
                    
                    System.out.println("¿Es ADMIN?: " + esAdmin);
                    System.out.println("¿Es CLIENTE?: " + esCliente);
                    
                    if (esAdmin) {
                        redirectUrl = "/";
                        System.out.println(" Redirigiendo a: " + redirectUrl);
                    } else if (esCliente) {
                        redirectUrl = "/";
                        System.out.println(" Cliente redirigiendo a: " + redirectUrl);
                    }
                    
                    System.out.println("========================================");
                    response.sendRedirect(redirectUrl);
                })
                .failureHandler((request, response, exception) -> {
                    System.out.println(" LOGIN FALLIDO");
                    System.out.println("Error: " + exception.getMessage());
                    System.out.println("Tipo de error: " + exception.getClass().getName());
                    response.sendRedirect("/login?error");
                })
            )
            .logout((logout) -> logout
                .logoutUrl("/logout")
                .permitAll()
                .logoutSuccessUrl("/login?logout")
            )
            .exceptionHandling((exceptions) -> exceptions
                .accessDeniedHandler(customAccessDeniedHandler)
            );

        return http.build();
    }
}