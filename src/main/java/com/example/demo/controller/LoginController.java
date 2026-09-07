package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {


	@GetMapping("/login")
	public String login(@RequestParam(required = false) String error, @RequestParam(required = false) String logout,
			Model model) {
		if (error != null)
			model.addAttribute("error", "Usuario o contraseña incorrectos. Intenta nuevamente.");
		if (logout != null)
			model.addAttribute("mensaje", "Has cerrado sesion correctamente.");
		return "login";
	}

	@GetMapping("/acceso-denegado")
	public String accesoDenegado() {
		return "acceso-denegado";
	}
}
