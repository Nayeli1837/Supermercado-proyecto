package com.example.demo.controller;

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

@Controller
@RequestMapping("/productos")
public class ProductoController {

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

    @GetMapping("/")
    public String inicio() {
        return "redirect:/productos";
    }

	@GetMapping
	public String mostrarProductos(@RequestParam(required = false) String buscar,
			@RequestParam(required = false) Long categoriaId, // 2. Nuevo parámetro para el filtro por categoría
			Model model, Principal principal) {

		List<Producto> listaProductos;

		if (buscar != null && !buscar.isEmpty()) {
			listaProductos = productoService.buscarProductos(buscar);
		} else if (categoriaId != null) {

			listaProductos = productoService.buscarPorCategoria(categoriaId);
		} else {
			listaProductos = productoService.listarProductos();
		}

		
		model.addAttribute("productos", listaProductos);

		model.addAttribute("totalProductos", listaProductos.size());

		if (categoriaRepo != null) {
			model.addAttribute("categorias", categoriaRepo.findAll());
		}

		model.addAttribute("producto", new Producto());
		model.addAttribute("movimientos", movimientoService.listarMovimientos());

		return "productos"; 
	}
	@GetMapping("/dashboard")
	public String dashboard(@RequestParam(required = false) String buscar,
	        @RequestParam(required = false) Long categoriaId, Model model,Principal principal){

	    List<Producto> listaProductos;

	    if (buscar != null && !buscar.isBlank()) {
	        listaProductos = productoService.buscarProductos(buscar);

	    } else if (categoriaId != null) {
	        listaProductos = productoService.buscarPorCategoria(categoriaId);

	    } else {
	        listaProductos = productoService.listarProductos();
	    }
		
		model.addAttribute("productos", listaProductos);

		model.addAttribute("totalProductos", listaProductos.size());
		
	    if (categoriaRepo != null) {
			model.addAttribute("categorias", categoriaRepo.findAll());
		}

		model.addAttribute("producto", new Producto());
		model.addAttribute("movimientos", movimientoService.listarMovimientos());

	    return "dashboard";
	}
    @GetMapping("/nuevo")
    public String nuevo(Model model){

        model.addAttribute("producto", new Producto()); 
        model.addAttribute("categorias", categoriaService.listarCategorias());
        model.addAttribute("totalProductos",productoService.listarProductos().size());

        return "nuevo-producto";
    }

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

        Long categoriaId = producto.getCategoria().getId();

        Categoria categoria = categoriaService.buscarPorId(categoriaId);

        producto.setCategoria(categoria);

        productoService.guardarProducto(producto);

        return "redirect:/productos";
    }

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

    @PostMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return "redirect:/productos";
    }
    
    @GetMapping("/alertas")
    public String verAlertas(Model model) {

        List<Producto> alertas = productoService.obtenerAlertasStock();

        long agotados = alertas.stream()
                .filter(p -> p.getStock() == 0)
                .count();

        long stockBajo = alertas.stream()
                .filter(p -> p.getStock() > 0)
                .count();

        model.addAttribute("alertas", alertas);
        model.addAttribute("totalAlertas", alertas.size());
        model.addAttribute("agotados", agotados);
        model.addAttribute("stockBajo", stockBajo);
        model.addAttribute("totalProductos",productoService.listarProductos().size());

        return "alertas";
    }
    
    @GetMapping("/reportes")
    public String verMóduloReportes(Model model) {
        List<Producto> listaProductos = productoService.listarProductos();
        model.addAttribute("productos", listaProductos);
        model.addAttribute("totalProductos", listaProductos.size());
    
        model.addAttribute("composicion", productoService.obtenerComposicionInventario());

        return "reportes"; 
    }
    @GetMapping("/reporte/pdf")
    public void descargarReportePdf(HttpServletResponse response) throws IOException {

        response.setContentType("application/pdf");
        response.setHeader(
                "Content-Disposition",
                "attachment; filename=reporte_inventario_" + LocalDate.now() + ".pdf"
        );

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String usuarioActual = auth != null ? auth.getName() : "Anónimo";

        List<Producto> productos = productoService.listarProductos();

        List<ComposicionInventario> composicion =
                productoService.obtenerComposicionInventario();

        long totalProductos = productoService.contarProductos();

        long productosAgotados = productoService.contarProductosAgotados();

        long stockBajo = productoService.contarProductosStockBajo();

        // Puedes reemplazar este valor por el cálculo real más adelante
        int precisionInventario = 94;

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
