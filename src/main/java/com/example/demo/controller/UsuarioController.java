package com.example.demo.controller;

// Importaciones necesarias
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.Usuario;
import com.example.demo.service.ProductoService;
import com.example.demo.service.UsuarioService;

// Indica que esta clase controla las solicitudes de usuarios
@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    // Servicio para gestionar los usuarios
    private final UsuarioService uService;

    // Servicio para gestionar los productos
    @Autowired
    private ProductoService productoService;

    // Inicializa el servicio de usuarios
    public UsuarioController(UsuarioService usService) {
        this.uService = usService;
    }

    // Mostrar la lista de usuarios y el total de productos
    @GetMapping
    public String abrirUsuarios(Model model) {
        model.addAttribute("usuarios", uService.listarTodos());
        model.addAttribute("totalProductos", productoService.listarProductos().size());

        return "usuarios"; 
    }

    // Mostrar el formulario para registrar un nuevo usuario
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("nuevoUsuario", new Usuario()); 
        return "nuevo-usuario"; 
    }

    // Guardar un nuevo usuario
    @PostMapping("/guardar")
    public String guardarUsuario(@ModelAttribute("nuevoUsuario") Usuario usuario) {
        uService.guardar(usuario); 
        return "redirect:/usuarios"; 
    }

    // Eliminar un usuario según su ID
    @PostMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id) {
        uService.eliminar(id);
        return "redirect:/usuarios";
    }
}