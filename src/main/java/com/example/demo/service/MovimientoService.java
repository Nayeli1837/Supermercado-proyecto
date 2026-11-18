package com.example.demo.service;

// Permite trabajar con la fecha y hora de los movimientos.
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

// Servicio encargado de registrar y consultar los movimientos del inventario.
@Service
public class MovimientoService {

    // Permite consultar y actualizar los productos del almacén.
    @Autowired
    private ProductoRepository productoRepo;

    // Permite guardar y consultar el historial de movimientos.
    @Autowired
    private MovimientoRepository movimientoRepo;

    // Permite buscar al usuario responsable de cada operación.
    @Autowired
    private UsuarioRepository usuarioRepository;

    // Registra el ingreso de productos y actualiza la cantidad disponible.
    @Transactional
    public Producto registrarEntrada(Long id, int cantidad, String username) {

        // Busca el producto que recibirá las nuevas unidades.
        Producto p = productoRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + id));

        // Suma la cantidad ingresada al stock actual.
        p.setStock(p.getStock() + cantidad);

        // Busca al usuario que realiza el registro.
        Usuario usuarioLogueado = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("El usuario de la sesión no existe en la BD: " + username));

        // Crea el registro del movimiento de entrada.
        Movimiento m = new Movimiento();

        // Guarda los datos de la operación realizada.
        m.setTipo("ENTRADA"); 
        m.setCantidad(cantidad);
        m.setUsuario(usuarioLogueado);
        m.setFecha(LocalDateTime.now());
        m.setProducto(p);

        // Guarda el movimiento y actualiza el stock del producto.
        movimientoRepo.save(m);
        return productoRepo.save(p);
    }

    // Registra la salida de productos y descuenta las unidades del stock.
    @Transactional
    public Producto registrarSalida(Long id, int cantidad, String username) {

        // Busca el producto que tendrá una reducción en su stock.
        Producto p = productoRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + id));

        // Comprueba que existan suficientes unidades antes de registrar la salida.
        if (p.getStock() < cantidad) {
            throw new IllegalStateException("Stock insuficiente. Stock actual: " + p.getStock() + ", Solicitado: " + cantidad);
        }

        // Resta del stock la cantidad de productos que salen del almacén.
        p.setStock(p.getStock() - cantidad);

        // Identifica al usuario responsable de registrar la salida.
        Usuario usuarioLogueado = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("El usuario de la sesión no existe en la BD: " + username));

        // Crea el registro correspondiente a la salida.
        Movimiento m = new Movimiento();
        m.setTipo("SALIDA");
        m.setCantidad(cantidad);
        m.setUsuario(usuarioLogueado);
        m.setFecha(LocalDateTime.now());
        m.setProducto(p);

        // Guarda el movimiento y actualiza el stock del producto.
        movimientoRepo.save(m);
        return productoRepo.save(p);
    }

    // Permite corregir la cantidad disponible de un producto.
    @Transactional
    public Producto ajustarStock(Long id, int nuevoStock) {

        // Busca el producto que necesita una corrección en su stock.
        Producto p = productoRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + id));

        // Evita registrar cantidades negativas en el inventario.
        if (nuevoStock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }

        // Calcula la diferencia entre el stock nuevo y el stock anterior.
        // Este valor permite identificar cuánto aumentó o disminuyó la cantidad.
        int diferencia = nuevoStock - p.getStock(); 

        // Actualiza el stock con la nueva cantidad indicada.
        p.setStock(nuevoStock);

        // Crea un movimiento para dejar constancia del ajuste realizado.
        Movimiento m = new Movimiento();
        m.setTipo("AJUSTE");
        m.setCantidad(diferencia); // Registra cuánto cambió realmente (+ o -)
        m.setFecha(LocalDateTime.now());
        m.setProducto(p);

        // Guarda el ajuste en el historial y actualiza el producto.
        movimientoRepo.save(m);
        return productoRepo.save(p);
    }

    // Obtiene todos los movimientos registrados en el sistema.
    public List<Movimiento> listarMovimientos() {
        return movimientoRepo.findAll();
    }
}