package com.example.demo.controller;

// Importaciones necesarias
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.model.Producto;
import com.example.demo.service.ProductoService;

// Controlador para mostrar el panel principal
@Controller
public class DashboardController {

    // Servicio para consultar los productos
    @Autowired
    private ProductoService productoService;

    // Mostrar el dashboard con los datos del inventario
    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication auth) {

        // Obtener la lista de productos
        List<Producto> productos =
                productoService.listarProductos();

        // Contar los productos agotados
        long agotados = productos.stream()
                .filter(p -> p.getStock() == 0)
                .count();

        // Contar los productos con stock bajo
        long stockBajo = productos.stream()
                .filter(p -> p.getStock() <= p.getStockMinimo())
                .count();

        // Enviar los datos a la vista
        model.addAttribute("productos", productos);

        model.addAttribute("totalProductos",
                productos.size());

        model.addAttribute("productosAgotados",
                agotados);

        model.addAttribute("stockBajo",
                stockBajo);

        return "dashboard";
    }
}