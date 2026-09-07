package com.example.demo.controller;


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


@Controller
@RequestMapping("/proveedores")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;
    @Autowired
    private ProductoService productoService;
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
    @GetMapping("/nuevo")
    public String nuevoProveedor(Model model,
                                 Authentication auth) {

        model.addAttribute("proveedor",
                new Proveedor());

        model.addAttribute("totalProveedores",
                proveedorService.listarProveedores().size());


        return "nuevo-proveedor";
    }

    @PostMapping("/guardar")
    public String guardarProveedor(
            @Valid @ModelAttribute("proveedor") Proveedor proveedor,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            return "nuevo-proveedor";
        }

        proveedorService.guardarProveedor(proveedor);

        return "redirect:/proveedores";
    }

    @GetMapping("/editar/{id}")
    public String editarProveedor(@PathVariable Long id,
                                  Model model, Authentication auth) {
        model.addAttribute("proveedor", proveedorService.obtenerProveedorPorId(id));

        model.addAttribute("totalProveedores", proveedorService.listarProveedores().size());


        return "editar-proveedor";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarProveedor(@PathVariable Long id) {

        proveedorService.eliminarProveedor(id);

        return "redirect:/proveedores";
    }
}