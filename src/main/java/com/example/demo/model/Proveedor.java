package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "proveedores")
public class Proveedor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idProveedor;

	@NotBlank(message = "El nombre del proveedor es obligatorio")
	@Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
	@Column(nullable = false, length = 100)
	private String nombre;

	@NotBlank(message = "El teléfono es obligatorio")
	@Pattern(regexp = "^9\\d{8}$", message = "El teléfono debe contener exactamente 9 dígitos")
	@Column(length = 9)
	private String telefono;

	@Email(message = "El formato del correo electrónico no es válido")
	@Size(max = 100, message = "El correo no puede tener más de 100 caracteres")
	@Column(length = 100)
	private String email;

	@Size(max = 200, message = "La dirección no puede tener más de 200 caracteres")
	@Column(length = 200)
	private String direccion;

	@NotBlank(message = "El RUC es obligatorio")
	@Size(max = 20, message = "El RUC no puede tener más de 20 caracteres")
	@Column(length = 20)
	private String ruc;

	public Proveedor() {
	}

	public Proveedor(String nombre, String telefono, String email, String direccion, String ruc) {
		this.nombre = nombre;
		this.telefono = telefono;
		this.email = email;
		this.direccion = direccion;
		this.ruc = ruc;
	}

	// GETTERS Y SETTERS

	public Long getIdProveedor() {
		return idProveedor;
	}

	public void setIdProveedor(Long idProveedor) {
		this.idProveedor = idProveedor;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getRuc() {
		return ruc;
	}

	public void setRuc(String ruc) {
		this.ruc = ruc;
	}
}
