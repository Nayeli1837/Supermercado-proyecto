package com.example.demo.service;

// Permite trabajar con listas de proveedores.
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Proveedor;
import com.example.demo.repository.ProveedorRepository;

// Servicio encargado de gestionar las operaciones relacionadas con los proveedores.
@Service
public class ProveedorService {

    // Permite acceder a las operaciones de consulta y almacenamiento de proveedores.
    @Autowired
    private ProveedorRepository proveedorRepository;

    // Obtiene todos los proveedores registrados en la base de datos.
    public List<Proveedor> listarProveedores() {
        return proveedorRepository.findAll();
    }

    // Guarda un nuevo proveedor o actualiza sus datos si ya existe.
    public Proveedor guardarProveedor(Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    // Busca un proveedor por su identificador.
    // Si no encuentra resultados, devuelve null.
    public Proveedor obtenerProveedorPorId(Long id) {
        return proveedorRepository.findById(id).orElse(null);
    }

    // Elimina un proveedor utilizando su identificador.
    public void eliminarProveedor(Long id) {
        proveedorRepository.deleteById(id);
    }
}