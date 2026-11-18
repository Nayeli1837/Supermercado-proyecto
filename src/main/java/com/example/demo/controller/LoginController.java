package com.example.demo.controller;

// Importaciones necesarias
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

// Controlador para gestionar el inicio de sesión
@Controller
public class LoginController {

    // Mostrar el formulario de inicio de sesión
    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, @RequestParam(required = false) String logout,
            Model model) {

        // Mostrar mensaje si las credenciales son incorrectas
        if (error != null)
            model.addAttribute("error", "Usuario o contraseña incorrectos. Intenta nuevamente.");

        // Mostrar mensaje al cerrar sesión
        if (logout != null)
            model.addAttribute("mensaje", "Has cerrado sesion correctamente.");

        return "login";
    }

    // Mostrar la página de acceso denegado
    @GetMapping("/acceso-denegado")
    public String accesoDenegado(Model model) {

        // Enviar mensaje de falta de permisos
        model.addAttribute("mensaje",
                "No tienes permisos para acceder a este recurso.");

        return "acceso-denegado";
    }
}