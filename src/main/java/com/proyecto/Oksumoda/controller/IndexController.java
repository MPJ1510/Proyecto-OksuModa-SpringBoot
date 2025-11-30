package com.proyecto.Oksumoda.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    
    /**
     * Maneja la solicitud GET a la raíz (/) o a la ruta de éxito tras el login.
     * Retorna el nombre del archivo HTML (Inicio.html) sin la extensión.
     */
    @GetMapping("/") // La ruta principal o a donde Spring Security redirige por defecto
    public String inicio() {
        return "Inicio"; // ¡Debe coincidir exactamente con el nombre del archivo: Inicio.html!
    }

    @GetMapping("/404")
    public String error404() {
        return "404";
    }

    @GetMapping("/403")
    public String error403() {
        return "403";
    }

        @GetMapping("/contactanos")
    public String contactanos() {
        return "contactanos";
    }

    @GetMapping("/hombres")
    public String hombres() {
        return "hombres";
    }

    @GetMapping("/mujeres")
    public String mujeres() {
        return "mujeres";
    }

        @GetMapping("/ninos")
    public String ninos() {
        return "ninos";
    }

        @GetMapping("/otros")
    public String otros() {
        return "otros";
    }
}