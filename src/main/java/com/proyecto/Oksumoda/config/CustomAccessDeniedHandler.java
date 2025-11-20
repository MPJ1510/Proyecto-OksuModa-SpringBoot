package com.proyecto.Oksumoda.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, 
                      HttpServletResponse response,
                      AccessDeniedException accessDeniedException) throws IOException, ServletException {
        
        // Log para debug
        System.out.println(" ACCESO DENEGADO");
        System.out.println("Usuario intentó acceder a: " + request.getRequestURI());
        System.out.println("Redirigiendo a la página de inicio...");
        
        // Redireccionar a la página de inicio en lugar de mostrar error 403
        response.sendRedirect("/403");
    }
}