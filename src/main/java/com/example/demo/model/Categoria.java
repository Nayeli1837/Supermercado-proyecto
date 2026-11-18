package com.example.demo.model;

// Permite trabajar con listas de productos asociados a una categoría.
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

// Representa la tabla de categorías en la base de datos.
@Entity
@Table(name = "categorias")
public class Categoria {

    // Identificador único generado automáticamente para cada categoría.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nombre de la categoría, obligatorio y con un máximo de 50 caracteres.
    @NotBlank(message = "El nombre de la categoría es obligatorio")
    @Size(max = 50, message = "El nombre no puede tener más de 50 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;

    // Relaciona una categoría con los productos que pertenecen a ella.
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL)
    private List<Producto> productos;

    // Constructor vacío utilizado para crear objetos de tipo Categoria.
	public Categoria() {
	}

	// GETTERS Y SETTERS
	// Permiten consultar y actualizar la información de la categoría.

    // Obtiene el identificador de la categoría.
	public Long getId() {
		return id;
	}

    // Asigna un identificador a la categoría.
	public void setId(Long id) {
		this.id = id;
	}

    // Obtiene el nombre de la categoría.
	public String getNombre() {
		return nombre;
	}

    // Permite establecer o modificar el nombre de la categoría.
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

    // Obtiene la lista de productos pertenecientes a la categoría.
	public List<Producto> getProductos() {
		return productos;
	}

    // Permite asignar los productos correspondientes a la categoría.
	public void setProductos(List<Producto> productos) {
		this.productos = productos;
	}



}