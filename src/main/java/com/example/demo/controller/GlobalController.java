package com.example.demo.controller;

// Importaciones necesarias
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

// Controlador que comparte datos con todas las vistas
@ControllerAdvice
public class GlobalController {

    // Agregar los datos del usuario autenticado
    @ModelAttribute
    public void agregarDatosUsuario(Model model, Authentication authentication) {

        // Verificar si el usuario ha iniciado sesión
        if (authentication != null && authentication.isAuthenticated()) {

            // Obtener el nombre del usuario
            model.addAttribute("usuario", authentication.getName());

            String rol = "";

            // Obtener los permisos del usuario
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

            // Buscar el rol asignado al usuario
            for (GrantedAuthority authority : authorities) {

                String nombreRol = authority.getAuthority();

                if (nombreRol.startsWith("ROLE_")) {

                    rol = nombreRol.replace("ROLE_", "");

                    break;
                }
            }

            // Convertir los roles a nombres más descriptivos
            switch (rol) {
                case "ADMINISTRADOR":
                    rol = "Administrador";
                    break;

                case "ALMACEN":
                    rol = "Encargado de Almacén";
                    break;

                case "GERENCIA":
                    rol = "Gerencia";
                    break;
            }

            // Enviar el rol a las vistas
            model.addAttribute("rol", rol);
        }
    }
}