package com.example.demo.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.model.Producto;
import com.example.demo.service.ProductoService;

@Controller
public class DashboardController {

    @Autowired
    private ProductoService productoService;

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication auth) {

        List<Producto> productos =
                productoService.listarProductos();

        long agotados = productos.stream()
                .filter(p -> p.getStock() == 0)
                .count();

        long stockBajo = productos.stream()
                .filter(p -> p.getStock() <= p.getStockMinimo())
                .count();

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
