package com.example.demo.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "productos")
public class Producto {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "El código es obligatorio")
	@Size(max = 20, message = "El código no puede tener más de 20 caracteres")
	@Column(nullable = false, unique = true, length = 20)
	private String codigo;

	@NotBlank(message = "El nombre del producto es obligatorio")
	@Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
	@Column(nullable = false, length = 100)
	private String nombre;

	@NotNull(message = "Debes seleccionar una categoría")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "categoria_id", nullable = false)
	private Categoria categoria;

	@Size(max = 50, message = "La zona no puede superar los 50 caracteres")
	@Column(length = 50)
	private String zona;

	@Min(value = 0, message = "El stock no puede ser negativo")
	@Column(nullable = false)
	private int stock;

	@Min(value = 0, message = "El stock mínimo no puede ser negativo")
	@Column(name = "stock_minimo", nullable = false)
	private int stockMinimo;

	@FutureOrPresent(message = "La fecha de vencimiento debe ser de hoy en adelante")
	@Column(name = "fecha_vencimiento")
	private LocalDate fechaVencimiento;

	public Producto() {
	}

	public Producto(Long id, String codigo, String nombre, Categoria categoria, String zona, int stock, int stockMinimo,
			LocalDate fechaVencimiento) {
		super();
		this.id = id;
		this.codigo = codigo;
		this.nombre = nombre;
		this.categoria = categoria;
		this.zona = zona;
		this.stock = stock;
		this.stockMinimo = stockMinimo;
		this.fechaVencimiento = fechaVencimiento;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public String getZona() {
		return zona;
	}

	public void setZona(String zona) {
		this.zona = zona;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public int getStockMinimo() {
		return stockMinimo;
	}

	public void setStockMinimo(int stockMinimo) {
		this.stockMinimo = stockMinimo;
	}

	public LocalDate getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(LocalDate fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

}
