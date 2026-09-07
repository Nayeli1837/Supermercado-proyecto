package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.model.Categoria;
import com.example.demo.repository.CategoriaRepository;

import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepo;

    // Obtener todas las categorías para llenar listas o selectores en las vistas
    public List<Categoria> listarCategorias() {
        return categoriaRepo.findAll();
    }

    // Registrar una nueva categoría o actualizar una existente
    public void guardarCategoria(Categoria categoria) {
        categoriaRepo.save(categoria);
    }

    // Buscar una categoría específica por su ID
    public Categoria obtenerCategoria(Long id) {
        return categoriaRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada con ID: " + id));
    }

    // Eliminar una categoría por ID
    public void eliminarCategoria(Long id) {
        categoriaRepo.deleteById(id);
    }
    
	public Categoria buscarPorId(Long id){

	    return categoriaRepo.findById(id)
	            .orElse(null);

	}
}