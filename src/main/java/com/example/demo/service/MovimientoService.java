package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Movimiento;
import com.example.demo.model.Producto;
import com.example.demo.model.Usuario;
import com.example.demo.repository.MovimientoRepository;
import com.example.demo.repository.ProductoRepository;
import com.example.demo.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class MovimientoService {

    @Autowired
    private ProductoRepository productoRepo;

    @Autowired
    private MovimientoRepository movimientoRepo;
    
    @Autowired
    private UsuarioRepository usuarioRepository;


    @Transactional
    public Producto registrarEntrada(Long id, int cantidad, String username) {
  
        Producto p = productoRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + id));

        p.setStock(p.getStock() + cantidad);
        Usuario usuarioLogueado = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("El usuario de la sesión no existe en la BD: " + username));

        Movimiento m = new Movimiento();

        m.setTipo("ENTRADA"); 
        m.setCantidad(cantidad);
        m.setUsuario(usuarioLogueado);
        m.setFecha(LocalDateTime.now());
        m.setProducto(p);
        
        
        movimientoRepo.save(m);
        return productoRepo.save(p);
    }

    @Transactional
    public Producto registrarSalida(Long id, int cantidad, String username) {
        Producto p = productoRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + id));

        if (p.getStock() < cantidad) {
            throw new IllegalStateException("Stock insuficiente. Stock actual: " + p.getStock() + ", Solicitado: " + cantidad);
        }

        p.setStock(p.getStock() - cantidad);
        Usuario usuarioLogueado = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("El usuario de la sesión no existe en la BD: " + username));
        Movimiento m = new Movimiento();
        m.setTipo("SALIDA");
        m.setCantidad(cantidad);
        m.setUsuario(usuarioLogueado);
        m.setFecha(LocalDateTime.now());
        m.setProducto(p);

        movimientoRepo.save(m);
        return productoRepo.save(p);
    }

    @Transactional
    public Producto ajustarStock(Long id, int nuevoStock) {
        Producto p = productoRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + id));

        if (nuevoStock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }

        // Para el historial, la cantidad del movimiento en un ajuste 
        // podría ser la diferencia, no el nuevo stock total.
        int diferencia = nuevoStock - p.getStock(); 

        p.setStock(nuevoStock);

        Movimiento m = new Movimiento();
        m.setTipo("AJUSTE");
        m.setCantidad(diferencia); // Registramos cuánto cambió realmente (+ o -)
        m.setFecha(LocalDateTime.now());
        m.setProducto(p);

        movimientoRepo.save(m);
        return productoRepo.save(p);
    }

    public List<Movimiento> listarMovimientos() {
        return movimientoRepo.findAll();
    }
}