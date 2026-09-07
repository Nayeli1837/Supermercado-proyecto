package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.Usuario;
import com.example.demo.service.ProductoService;
import com.example.demo.service.UsuarioService;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService uService;
    @Autowired
    private ProductoService productoService;

    public UsuarioController(UsuarioService usService) {
        this.uService = usService;
    }

    @GetMapping
    public String abrirUsuarios(Model model) {
        model.addAttribute("usuarios", uService.listarTodos());
        model.addAttribute("totalProductos", productoService.listarProductos().size());
        
        return "usuarios"; 
    }
    
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("nuevoUsuario", new Usuario()); 
        return "nuevo-usuario"; 
    }

    @PostMapping("/guardar")
    public String guardarUsuario(@ModelAttribute("nuevoUsuario") Usuario usuario) {
        uService.guardar(usuario); 
        return "redirect:/usuarios"; 
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id) {
        uService.eliminar(id);
        return "redirect:/usuarios";
    }
}