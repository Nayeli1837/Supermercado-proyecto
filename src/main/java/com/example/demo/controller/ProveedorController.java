package com.example.demo.controller;

// Importaciones necesarias
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.Proveedor;
import com.example.demo.service.ProductoService;
import com.example.demo.service.ProveedorService;

import jakarta.validation.Valid;

// Controlador para gestionar los proveedores
@Controller
@RequestMapping("/proveedores")
public class ProveedorController {

    // Servicios para gestionar proveedores y productos
    @Autowired
    private ProveedorService proveedorService;
    @Autowired
    private ProductoService productoService;

    // Mostrar la lista de proveedores y los totales
    @GetMapping
    public String listarProveedores(Model model) {

        model.addAttribute("proveedores",
                proveedorService.listarProveedores());

        model.addAttribute("proveedor",
                new Proveedor());
        model.addAttribute("totalProveedores",
                proveedorService.listarProveedores().size());
        model.addAttribute("totalProductos",
                productoService.listarProductos().size());


        return "proveedores";
    }

    // Mostrar el formulario para registrar un proveedor
    @GetMapping("/nuevo")
    public String nuevoProveedor(Model model,
                                 Authentication auth) {

        model.addAttribute("proveedor",
                new Proveedor());

        model.addAttribute("totalProveedores",
                proveedorService.listarProveedores().size());


        return "nuevo-proveedor";
    }

    // Guardar un proveedor validando sus datos
    @PostMapping("/guardar")
    public String guardarProveedor(
            @Valid @ModelAttribute("proveedor") Proveedor proveedor,
            BindingResult result,
            Model model) {

        // Verificar si existen errores en los datos
        if (result.hasErrors()) {
            return "nuevo-proveedor";
        }

        proveedorService.guardarProveedor(proveedor);

        return "redirect:/proveedores";
    }

    // Buscar un proveedor para mostrar sus datos en el formulario de edición
    @GetMapping("/editar/{id}")
    public String editarProveedor(@PathVariable Long id,
                                  Model model, Authentication auth) {
        model.addAttribute("proveedor", proveedorService.obtenerProveedorPorId(id));

        model.addAttribute("totalProveedores", proveedorService.listarProveedores().size());


        return "editar-proveedor";
    }

    // Eliminar un proveedor según su ID
    @PostMapping("/eliminar/{id}")
    public String eliminarProveedor(@PathVariable Long id) {

        proveedorService.eliminarProveedor(id);

        return "redirect:/proveedores";
    }
}