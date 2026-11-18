package com.example.demo.service;

// Importaciones necesarias
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.model.Categoria;
import com.example.demo.repository.CategoriaRepository;

import java.util.List;

// Indica que esta clase contiene la lógica del servicio
@Service
public class CategoriaService {

    // Inyecta el repositorio de categorías
    @Autowired
    private CategoriaRepository categoriaRepo;

    // Obtener todas las categorías
    public List<Categoria> listarCategorias() {
        return categoriaRepo.findAll();
    }

    // Guardar o actualizar una categoría
    public void guardarCategoria(Categoria categoria) {
        categoriaRepo.save(categoria);
    }

    // Buscar una categoría por su ID
    public Categoria obtenerCategoria(Long id) {
        return categoriaRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada con ID: " + id));
    }

    // Eliminar una categoría por su ID
    public void eliminarCategoria(Long id) {
        categoriaRepo.deleteById(id);
    }

    // Buscar una categoría por ID y devolver null si no existe
	public Categoria buscarPorId(Long id){

	    return categoriaRepo.findById(id)
	            .orElse(null);

	}
}