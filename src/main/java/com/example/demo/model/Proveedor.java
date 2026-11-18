package com.example.demo.model;

// Importamos las herramientas para relacionar esta clase con la base de datos.
import jakarta.persistence.*;

// Importamos las validaciones para comprobar que los datos sean correctos.
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

// Indica que esta clase representa una tabla en la base de datos.
@Entity
@Table(name = "proveedores")
public class Proveedor {

	// Identificador único de cada proveedor.
	@Id
	// La base de datos genera automáticamente el código del proveedor.
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idProveedor;

	// Comprueba que el nombre del proveedor no esté vacío.
	@NotBlank(message = "El nombre del proveedor es obligatorio")
	// Establece que el nombre no debe superar los 100 caracteres.
	@Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
	// Guarda el nombre como un dato obligatorio.
	@Column(nullable = false, length = 100)
	private String nombre;

	// Comprueba que el teléfono no esté vacío.
	@NotBlank(message = "El teléfono es obligatorio")
	// Verifica que el teléfono comience con 9 y tenga exactamente 9 dígitos.
	@Pattern(regexp = "^9\\d{8}$", message = "El teléfono debe contener exactamente 9 dígitos")
	// Establece un espacio máximo de 9 caracteres para el teléfono.
	@Column(length = 9)
	private String telefono;

	// Comprueba que el correo electrónico tenga un formato válido.
	@Email(message = "El formato del correo electrónico no es válido")
	// Establece que el correo no debe superar los 100 caracteres.
	@Size(max = 100, message = "El correo no puede tener más de 100 caracteres")
	// Guarda el correo electrónico del proveedor.
	@Column(length = 100)
	private String email;

	// Establece que la dirección no debe superar los 200 caracteres.
	@Size(max = 200, message = "La dirección no puede tener más de 200 caracteres")
	// Guarda la dirección del proveedor.
	@Column(length = 200)
	private String direccion;

	// Comprueba que el RUC no esté vacío.
	@NotBlank(message = "El RUC es obligatorio")
	// Establece que el RUC no debe superar los 20 caracteres.
	@Size(max = 20, message = "El RUC no puede tener más de 20 caracteres")
	// Guarda el RUC del proveedor.
	@Column(length = 20)
	private String ruc;

	// Constructor vacío que permite crear un proveedor sin ingresar datos inicialmente.
	public Proveedor() {
	}

	// Constructor que permite crear un proveedor ingresando sus datos principales.
	public Proveedor(String nombre, String telefono, String email, String direccion, String ruc) {

		// Asigna los valores recibidos a cada atributo del proveedor.
		this.nombre = nombre;
		this.telefono = telefono;
		this.email = email;
		this.direccion = direccion;
		this.ruc = ruc;
	}

	// GETTERS Y SETTERS
	// Los métodos GET permiten consultar los datos del proveedor.
	// Los métodos SET permiten asignar o modificar sus valores.

	// Obtiene el identificador del proveedor.
	public Long getIdProveedor() {
		return idProveedor;
	}

	// Asigna un identificador al proveedor.
	public void setIdProveedor(Long idProveedor) {
		this.idProveedor = idProveedor;
	}

	// Obtiene el nombre del proveedor.
	public String getNombre() {
		return nombre;
	}

	// Permite establecer o cambiar el nombre del proveedor.
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	// Obtiene el teléfono del proveedor.
	public String getTelefono() {
		return telefono;
	}

	// Permite establecer o cambiar el teléfono del proveedor.
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	// Obtiene el correo electrónico del proveedor.
	public String getEmail() {
		return email;
	}

	// Permite establecer o cambiar el correo electrónico.
	public void setEmail(String email) {
		this.email = email;
	}

	// Obtiene la dirección del proveedor.
	public String getDireccion() {
		return direccion;
	}

	// Permite establecer o cambiar la dirección del proveedor.
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	// Obtiene el RUC del proveedor.
	public String getRuc() {
		return ruc;
	}

	// Permite establecer o cambiar el RUC del proveedor.
	public void setRuc(String ruc) {
		this.ruc = ruc;
	}
}