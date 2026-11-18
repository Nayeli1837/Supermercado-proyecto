package com.example.demo.service;

// Permite acceder a las herramientas necesarias para gestionar productos.
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.ComposicionInventario;
import com.example.demo.model.Producto;
import com.example.demo.repository.ProductoRepository;

import java.util.List;

// Servicio encargado de gestionar los productos y consultar la información del inventario.
@Service
public class ProductoService {

    // Permite realizar consultas y operaciones sobre los productos registrados.
    @Autowired
    private ProductoRepository productoRepo;

    // Obtiene todos los productos registrados en la base de datos.
    public List<Producto> listarProductos() {
        return productoRepo.findAll();
    }

    // Busca productos por su nombre, sin distinguir entre mayúsculas y minúsculas.
    public List<Producto> buscarProductos(String buscar) {
        return productoRepo.findByNombreContainingIgnoreCase(buscar);
    }

    // Obtiene los productos que pertenecen a una categoría específica.
    public List<Producto> buscarPorCategoria(Long categoriaId) {
        return productoRepo.findByCategoriaId(categoriaId);
    }

    // Guarda un nuevo producto o actualiza la información de uno existente.
    public void guardarProducto(Producto producto) {
        productoRepo.save(producto);
    }

    // Busca un producto por su identificador.
    // Si no existe, muestra un mensaje indicando que no fue encontrado.
    public Producto obtenerProducto(Long id) {
        return productoRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + id));
    }

    // Elimina un producto utilizando su identificador.
    public void eliminarProducto(Long id) {
        productoRepo.deleteById(id);
    }

    // Obtiene los productos que presentan alertas relacionadas con su nivel de stock.
    public List<Producto> obtenerAlertasStock() {
        return productoRepo.obtenerAlertasStock();
    }

    // Obtiene la cantidad de productos agrupados por categoría
    // y calcula su porcentaje respecto a la categoría con mayor cantidad.
    public List<ComposicionInventario> obtenerComposicionInventario() {

        // Consulta los datos de composición del inventario.
        var datos = productoRepo.obtenerComposicionInventario();

        // Si no existen registros, devuelve una lista vacía.
        if (datos.isEmpty()) {
            return List.of();
        }

        // Obtiene la cantidad más alta entre las categorías.
        long maximo = (Long) datos.get(0)[1];

        // Recorre los resultados y crea un objeto con la categoría,
        // su cantidad y el porcentaje calculado.
        return datos.stream()
                .map(item -> new ComposicionInventario(
                        (String) item[0],
                        (Long) item[1],
                        ((Long) item[1] * 100.0) / maximo
                ))
                .toList();
    }

    // Obtiene el número total de productos registrados.
    public long contarProductos() {
        return productoRepo.contarProductos();
    }

    // Cuenta los productos que no tienen unidades disponibles.
    public long contarProductosAgotados() {
        return productoRepo.contarProductosAgotados();
    }

    // Cuenta los productos cuyo stock se encuentra por debajo del mínimo establecido.
    public long contarProductosStockBajo() {
        return productoRepo.contarProductosStockBajo();
    }

}