package com.example.demo.controller;

// Importaciones necesarias
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.Movimiento;
import com.example.demo.service.MovimientoService;
import com.example.demo.service.ProductoService;

import java.security.Principal;

// Controlador para gestionar los movimientos del inventario
@Controller
@RequestMapping("/movimientos")
public class MovimientoController {

    // Servicio para gestionar los movimientos
    @Autowired
    private MovimientoService movimientoService;

    // Servicio para consultar los productos
    @Autowired
    private ProductoService productoService; 

    // Mostrar la lista de movimientos y productos
    @GetMapping
    public String verMovimientos(Model model, Principal principal) {

        // Preparar los datos para el formulario
        model.addAttribute("movimiento", new Movimiento());

        model.addAttribute("movimientos", movimientoService.listarMovimientos()); 
        model.addAttribute("productos", productoService.listarProductos());

        // Obtener los totales de productos y movimientos
        model.addAttribute("totalProductos", productoService.listarProductos().size());
        model.addAttribute("totalMovimientos", movimientoService.listarMovimientos().size());


        return "movimientos"; 
    }

    // Registrar una entrada o salida de productos
    @PostMapping("/guardar")
    public String guardarMovimiento(@ModelAttribute Movimiento movimiento, 
                                    Principal principal, 
                                    RedirectAttributes redirectAttrs) {
        try {

            // Obtener el usuario que realiza el movimiento
            String username = principal.getName(); 

            // Registrar una entrada de stock
            if ("ENTRADA".equals(movimiento.getTipo())) {
                movimientoService.registrarEntrada(movimiento.getProducto().getId(), movimiento.getCantidad(), username);
                redirectAttrs.addFlashAttribute("mensajeExito", "📥 Entrada de stock registrada correctamente.");
            } 
            // Registrar una salida de stock
            else if ("SALIDA".equals(movimiento.getTipo())) {
                movimientoService.registrarSalida(movimiento.getProducto().getId(), movimiento.getCantidad(), username);
                redirectAttrs.addFlashAttribute("mensajeExito", "📤 Salida de stock registrada correctamente.");
            }
        } catch (IllegalStateException e) {
            // Mostrar un mensaje si ocurre un error en la operación
            redirectAttrs.addFlashAttribute("mensajeError", e.getMessage());
        } catch (Exception e) {
            // Mostrar un mensaje si ocurre otro tipo de error
            redirectAttrs.addFlashAttribute("mensajeError", "❌ Error al procesar la operación: " + e.getMessage());
        }

        return "redirect:/movimientos"; 
    }
}