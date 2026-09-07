package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.ComposicionInventario;
import com.example.demo.model.Producto;
import com.example.demo.repository.ProductoRepository;

import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepo;


    public List<Producto> listarProductos() {
        return productoRepo.findAll();
    }

    public List<Producto> buscarProductos(String buscar) {
        return productoRepo.findByNombreContainingIgnoreCase(buscar);
    }

    public List<Producto> buscarPorCategoria(Long categoriaId) {
        return productoRepo.findByCategoriaId(categoriaId);
    }

    public void guardarProducto(Producto producto) {
        productoRepo.save(producto);
    }

    public Producto obtenerProducto(Long id) {
        return productoRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + id));
    }

    public void eliminarProducto(Long id) {
        productoRepo.deleteById(id);
    }

    public List<Producto> obtenerAlertasStock() {
        return productoRepo.obtenerAlertasStock();
    }
    
    public List<ComposicionInventario> obtenerComposicionInventario() {

        var datos = productoRepo.obtenerComposicionInventario();

        if (datos.isEmpty()) {
            return List.of();
        }

        long maximo = (Long) datos.get(0)[1];

        return datos.stream()
                .map(item -> new ComposicionInventario(
                        (String) item[0],
                        (Long) item[1],
                        ((Long) item[1] * 100.0) / maximo
                ))
                .toList();
    }
    
    public long contarProductos() {
        return productoRepo.contarProductos();
    }
    public long contarProductosAgotados() {
        return productoRepo.contarProductosAgotados();
    }
    public long contarProductosStockBajo() {
        return productoRepo.contarProductosStockBajo();
    }
  
    

}