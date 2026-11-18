package com.example.demo.model;

// Permite registrar la fecha y hora de cada movimiento.
import java.time.LocalDateTime;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

// Representa la tabla de movimientos en la base de datos.
@Entity
@Table(name = "movimientos")
public class Movimiento {

    // Identificador único de cada movimiento.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Indica si el movimiento corresponde a una entrada o salida de productos.
    @NotBlank(message = "El tipo de movimiento es obligatorio")
    @Size(max = 20, message = "El tipo no puede superar los 20 caracteres")
    @Column(nullable = false, length = 20)
    private String tipo; // ENTRADA" o "SALIDA"

    // Cantidad de unidades que ingresan o salen del almacén.
    @Min(value = 1, message = "La cantidad del movimiento debe ser como mínimo 1")
    @Column(nullable = false)
    private int cantidad;

    // Fecha y hora en que se registra el movimiento. No puede modificarse desde la base de datos.
    @Column(nullable = false, updatable = false, name = "fecha")
    private LocalDateTime fecha;

    // Producto relacionado con el movimiento registrado.
    @NotNull(message = "El producto es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    // Usuario responsable de registrar el movimiento.
    @NotNull(message = "El usuario que registra es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Se ejecuta automáticamente antes de guardar un nuevo movimiento.
    @PrePersist 
    protected void onCreate() {
        // Asigna la fecha y hora actual al momento del registro.
        this.fecha = LocalDateTime.now(); 
    }

    // Constructor vacío utilizado para crear objetos de tipo Movimiento.
    public Movimiento() {
    }

    // GETTERS Y SETTERS
    // Permiten consultar y actualizar los datos del movimiento.

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}


	}