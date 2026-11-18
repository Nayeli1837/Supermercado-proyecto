package com.example.demo.controller;

// Importaciones necesarias
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.Categoria;
import com.example.demo.model.ComposicionInventario;
import com.example.demo.model.Producto;
import com.example.demo.repository.CategoriaRepository;
import com.example.demo.service.CategoriaService;
import com.example.demo.service.MovimientoService;
import com.example.demo.service.ProductoService;
import com.example.demo.service.ReporteService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

// Controlador para gestionar los productos
@Controller
@RequestMapping("/productos")
public class ProductoController {

    // Servicios y repositorios necesarios para gestionar productos
    @Autowired
    private ProductoService productoService;

    @Autowired
    private MovimientoService movimientoService;
    @Autowired
    private CategoriaService categoriaService;
    @Autowired
    private CategoriaRepository categoriaRepo;
    @Autowired
    private ReporteService pdfService;

    // Redirigir al listado de productos
    @GetMapping("/")
    public String inicio() {
        return "redirect:/productos";
    }

    // Mostrar productos aplicando filtros de búsqueda o categoría
    @GetMapping
    public String mostrarProductos(@RequestParam(required = false) String buscar,
            @RequestParam(required = false) Long categoriaId, // Parámetro para filtrar por categoría
            Model model, Principal principal) {

        List<Producto> listaProductos;

        // Aplicar el filtro correspondiente
        if (buscar != null && !buscar.isEmpty()) {
            listaProductos = productoService.buscarProductos(buscar);
        } else if (categoriaId != null) {

            listaProductos = productoService.buscarPorCategoria(categoriaId);
        } else {
            listaProductos = productoService.listarProductos();
        }

        // Enviar los datos a la vista
        model.addAttribute("productos", listaProductos);

        model.addAttribute("totalProductos", listaProductos.size());

        if (categoriaRepo != null) {
            model.addAttribute("categorias", categoriaRepo.findAll());
        }

        model.addAttribute("producto", new Producto());
        model.addAttribute("movimientos", movimientoService.listarMovimientos());

        return "productos";
    }

    // Mostrar el panel principal con los productos
    @GetMapping("/dashboard")
    public String dashboard(@RequestParam(required = false) String buscar,
            @RequestParam(required = false) Long categoriaId, Model model, Principal principal) {

        List<Producto> listaProductos;

        // Aplicar filtros de búsqueda o categoría
        if (buscar != null && !buscar.isBlank()) {
            listaProductos = productoService.buscarProductos(buscar);

        } else if (categoriaId != null) {
            listaProductos = productoService.buscarPorCategoria(categoriaId);

        } else {
            listaProductos = productoService.listarProductos();
        }

        // Enviar los datos a la vista
        model.addAttribute("productos", listaProductos);

        model.addAttribute("totalProductos", listaProductos.size());

        if (categoriaRepo != null) {
            model.addAttribute("categorias", categoriaRepo.findAll());
        }

        model.addAttribute("producto", new Producto());
        model.addAttribute("movimientos", movimientoService.listarMovimientos());

        return "dashboard";
    }

    // Mostrar el formulario para registrar un producto
    @GetMapping("/nuevo")
    public String nuevo(Model model) {

        model.addAttribute("producto", new Producto());
        model.addAttribute("categorias", categoriaService.listarCategorias());
        model.addAttribute("totalProductos", productoService.listarProductos().size());

        return "nuevo-producto";
    }

    // Guardar un producto validando sus datos
    @PostMapping("/guardar")
    public String guardarProducto(
            @Valid @ModelAttribute("producto") Producto producto,
            BindingResult result,
            Model model) {

        // Si hay errores, regresar al formulario
        if (result.hasErrors()) {

            model.addAttribute("categorias", categoriaService.listarCategorias());
            model.addAttribute("totalProductos", productoService.listarProductos().size());

            return "nuevo-producto";
        }

        // Obtener la categoría seleccionada
        Long categoriaId = producto.getCategoria().getId();

        Categoria categoria = categoriaService.buscarPorId(categoriaId);

        producto.setCategoria(categoria);

        // Guardar el producto
        productoService.guardarProducto(producto);

        return "redirect:/productos";
    }

    // Mostrar los datos de un producto para editarlo
    @GetMapping("/editar/{id}")
    public String editarProducto(@PathVariable Long id, Model model) {
        model.addAttribute("producto", productoService.obtenerProducto(id));
        model.addAttribute("productos", productoService.listarProductos());
        model.addAttribute("movimientos", movimientoService.listarMovimientos());
        model.addAttribute("categorias", categoriaService.listarCategorias()
        );

        model.addAttribute("totalProductos", productoService.listarProductos().size());

        return "editar-producto";
    }

    // Eliminar un producto según su ID
    @PostMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return "redirect:/productos";
    }

    // Mostrar alertas de productos con stock bajo o agotado
    @GetMapping("/alertas")
    public String verAlertas(Model model) {

        List<Producto> alertas = productoService.obtenerAlertasStock();

        // Contar los productos agotados
        long agotados = alertas.stream()
                .filter(p -> p.getStock() == 0)
                .count();

        // Contar los productos con stock bajo
        long stockBajo = alertas.stream()
                .filter(p -> p.getStock() > 0)
                .count();

        // Enviar las alertas y cantidades a la vista
        model.addAttribute("alertas", alertas);
        model.addAttribute("totalAlertas", alertas.size());
        model.addAttribute("agotados", agotados);
        model.addAttribute("stockBajo", stockBajo);
        model.addAttribute("totalProductos", productoService.listarProductos().size());

        return "alertas";
    }

    // Mostrar el módulo de reportes del inventario
    @GetMapping("/reportes")
    public String verMóduloReportes(Model model) {
        List<Producto> listaProductos = productoService.listarProductos();
        model.addAttribute("productos", listaProductos);
        model.addAttribute("totalProductos", listaProductos.size());

        model.addAttribute("composicion", productoService.obtenerComposicionInventario());

        return "reportes";
    }

    // Generar y descargar el reporte de inventario en PDF
    @GetMapping("/reporte/pdf")
    public void descargarReportePdf(HttpServletResponse response) throws IOException {

        // Configurar la respuesta para descargar un archivo PDF
        response.setContentType("application/pdf");
        response.setHeader(
                "Content-Disposition",
                "attachment; filename=reporte_inventario_" + LocalDate.now() + ".pdf"
        );

        // Obtener el usuario que inició sesión
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String usuarioActual = auth != null ? auth.getName() : "Anónimo";

        // Obtener los datos del inventario
        List<Producto> productos = productoService.listarProductos();

        List<ComposicionInventario> composicion =
                productoService.obtenerComposicionInventario();

        // Obtener los indicadores del inventario
        long totalProductos = productoService.contarProductos();

        long productosAgotados = productoService.contarProductosAgotados();

        long stockBajo = productoService.contarProductosStockBajo();

        // Valor de precisión del inventario
        int precisionInventario = 94;

        // Exportar los datos al reporte PDF
        pdfService.exportar(
                response,
                productos,
                composicion,
                totalProductos,
                productosAgotados,
                stockBajo,
                precisionInventario,
                usuarioActual
        );
    }
}