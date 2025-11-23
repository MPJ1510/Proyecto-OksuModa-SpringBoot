package com.proyecto.Oksumoda.controller;

import com.proyecto.Oksumoda.model.Usuario;
import com.proyecto.Oksumoda.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Muestra el formulario de login (Spring Security se encarga del POST)
    @GetMapping("/login")
    public String showLoginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null){
         model.addAttribute("error", "Correo o contraseña incorrectos.");   
        }
        return "login"; // Hace referencia a src/main/resources/templates/login.html
    }

    // Muestra el formulario de registro
    @GetMapping("/registro")
    public String showRegistrationForm(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro"; // Hace referencia a src/main/resources/templates/registro.html
    }

    // Maneja el envío del formulario de registro
    @PostMapping("/registro")
    public String registerUser(@ModelAttribute("usuario") Usuario usuario) {
        usuarioService.registrarNuevoUsuario(usuario);
        // Redirigir a la página de login después de un registro exitoso
        return "redirect:/login?success"; 
    }
}