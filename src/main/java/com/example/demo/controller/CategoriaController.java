package com.example.demo.controller;

// Importaciones necesarias
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.Categoria;
import com.example.demo.model.Producto;
import com.example.demo.service.CategoriaService;
import com.example.demo.service.ProductoService;

// Controlador para gestionar las categorías
@Controller
@RequestMapping("/categorias")
public class CategoriaController {

    // Servicios para gestionar categorías y productos
    @Autowired
    private CategoriaService categoriaService;
    @Autowired
    private ProductoService productoService;

    // Mostrar la lista de categorías y el total de productos
    @GetMapping
    public String listarCategorias(Model model) {
        List<Producto> listaProductos =
                productoService.listarProductos();

        // Enviar las categorías y los datos a la vista
        model.addAttribute("categorias", categoriaService.listarCategorias());
        model.addAttribute("categoria", new Categoria());
        model.addAttribute("totalProductos", listaProductos.size()); 

        return "categorias"; 
    }

    // Mostrar el formulario para registrar una categoría
    @GetMapping("/nuevo")
    public String nuevo(Model model){

        model.addAttribute("categoria", new Categoria()); 
        model.addAttribute("categorias", categoriaService.listarCategorias());
        model.addAttribute("totalProductos",productoService.listarProductos().size());

        return "nuevo-categoria";
    }

    // Guardar una nueva categoría
    @PostMapping("/guardar")
    public String guardarCategoria(@ModelAttribute Categoria categoria, RedirectAttributes redirectAttrs) {
        try {
            categoriaService.guardarCategoria(categoria);

            // Mostrar mensaje de confirmación
            redirectAttrs.addFlashAttribute("mensajeExito", "Categoría guardada correctamente.");
        } catch (Exception e) {
            // Mostrar mensaje si ocurre un error
            redirectAttrs.addFlashAttribute("mensajeError", "Error al guardar la categoría: " + e.getMessage());
        }
        return "redirect:/categorias"; 
    }

    // Eliminar una categoría según su ID
    @PostMapping("/eliminar/{id}")
    public String eliminarCategoria(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        try {
            categoriaService.eliminarCategoria(id);

            // Mostrar mensaje de confirmación
            redirectAttrs.addFlashAttribute("mensajeExito", "Categoría eliminada correctamente.");
        } catch (Exception e) {
            // Mostrar mensaje si la categoría tiene productos asociados
            redirectAttrs.addFlashAttribute("mensajeError", "No se puede eliminar la categoría porque contiene productos asociados.");
        }
        return "redirect:/categorias";
    }
}