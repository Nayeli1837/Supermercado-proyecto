package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.Movimiento;
import com.example.demo.service.MovimientoService;
import com.example.demo.service.ProductoService; // 👈 Asegúrate de importar tu servicio de productos

import java.security.Principal;

@Controller
@RequestMapping("/movimientos")
public class MovimientoController {

    @Autowired
    private MovimientoService movimientoService;

    @Autowired
    private ProductoService productoService; 

    @GetMapping
    public String verMovimientos(Model model, Principal principal) {

        model.addAttribute("movimiento", new Movimiento());
       
        model.addAttribute("movimientos", movimientoService.listarMovimientos()); 
        model.addAttribute("productos", productoService.listarProductos());
        
        model.addAttribute("totalProductos", productoService.listarProductos().size());
        model.addAttribute("totalMovimientos", movimientoService.listarMovimientos().size());

        
        return "movimientos"; 
    }

    @PostMapping("/guardar")
    public String guardarMovimiento(@ModelAttribute Movimiento movimiento, 
                                    Principal principal, 
                                    RedirectAttributes redirectAttrs) {
        try {
          
            String username = principal.getName(); 
            
            if ("ENTRADA".equals(movimiento.getTipo())) {
                movimientoService.registrarEntrada(movimiento.getProducto().getId(), movimiento.getCantidad(), username);
                redirectAttrs.addFlashAttribute("mensajeExito", "📥 Entrada de stock registrada correctamente.");
            } else if ("SALIDA".equals(movimiento.getTipo())) {
                movimientoService.registrarSalida(movimiento.getProducto().getId(), movimiento.getCantidad(), username);
                redirectAttrs.addFlashAttribute("mensajeExito", "📤 Salida de stock registrada correctamente.");
            }
        } catch (IllegalStateException e) {
            redirectAttrs.addFlashAttribute("mensajeError", e.getMessage());
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("mensajeError", "❌ Error al procesar la operación: " + e.getMessage());
        }
        
        return "redirect:/movimientos"; 
    }
}
