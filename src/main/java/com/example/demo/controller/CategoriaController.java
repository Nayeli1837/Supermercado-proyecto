package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.Categoria;
import com.example.demo.model.Producto;
import com.example.demo.service.CategoriaService; // Cambiado de Repository a Service
import com.example.demo.service.ProductoService;

@Controller
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService; // Inyectamos el servicio
    @Autowired
    private ProductoService productoService;

    @GetMapping
    public String listarCategorias(Model model) {
        List<Producto> listaProductos =
                productoService.listarProductos();
        model.addAttribute("categorias", categoriaService.listarCategorias());
        model.addAttribute("categoria", new Categoria());
        model.addAttribute("totalProductos", listaProductos.size()); 
        return "categorias"; 
    }
    @GetMapping("/nuevo")
    public String nuevo(Model model){

        model.addAttribute("categoria", new Categoria()); 
        model.addAttribute("categorias", categoriaService.listarCategorias());
        model.addAttribute("totalProductos",productoService.listarProductos().size());

        return "nuevo-categoria";
    }

    @PostMapping("/guardar")
    public String guardarCategoria(@ModelAttribute Categoria categoria, RedirectAttributes redirectAttrs) {
        try {
            categoriaService.guardarCategoria(categoria);
            redirectAttrs.addFlashAttribute("mensajeExito", "Categoría guardada correctamente.");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("mensajeError", "Error al guardar la categoría: " + e.getMessage());
        }
        return "redirect:/categorias"; 
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarCategoria(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        try {
            categoriaService.eliminarCategoria(id);
            redirectAttrs.addFlashAttribute("mensajeExito", "Categoría eliminada correctamente.");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("mensajeError", "No se puede eliminar la categoría porque contiene productos asociados.");
        }
        return "redirect:/categorias";
    }
}
