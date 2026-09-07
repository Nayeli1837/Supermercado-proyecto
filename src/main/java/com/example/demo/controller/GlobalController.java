package com.example.demo.controller;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

@ControllerAdvice
public class GlobalController {

    @ModelAttribute
    public void agregarDatosUsuario(Model model, Authentication authentication) {

        if (authentication != null && authentication.isAuthenticated()) {

            model.addAttribute("usuario", authentication.getName());

            String rol = "";

            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

            for (GrantedAuthority authority : authorities) {

                String nombreRol = authority.getAuthority();

                if (nombreRol.startsWith("ROLE_")) {

                    rol = nombreRol.replace("ROLE_", "");

                    break;
                }
            }

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

            model.addAttribute("rol", rol);
        }
    }
}