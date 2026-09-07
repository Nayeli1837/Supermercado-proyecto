package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "usuarios")
public class Usuario {

    // ACTORES DEL SISTEMA
    public static final String ADMINISTRADOR = "ADMINISTRADOR";
    public static final String ENCARGADO_ALMACEN = "ALMACEN";
    public static final String GERENCIA = "GERENCIA";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de usuario es obligatorio.")
    @Size(min = 4, max = 50, message = "El usuario debe tener entre 4 y 50 caracteres.")
    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @NotBlank(message = "La contraseña es obligatoria.")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres.")
    @Column(nullable = false)
    private String password;

    @NotBlank(message = "El nombre completo es obligatorio.")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres.")
    @Column(nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "El correo electrónico es obligatorio.")
    @Email(message = "Por favor, ingrese un correo electrónico válido.")
    @Size(max = 100, message = "El correo no puede exceder los 100 caracteres.")
    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @NotBlank(message = "Debe asignar un rol al usuario.")
    @Column(nullable = false, length = 50)
    private String rol;

	@Column(nullable = false)
	private String estado = "ACTIVO";

    public Usuario() {}


	public Usuario(Long id,
			@NotBlank(message = "El nombre de usuario es obligatorio.") @Size(min = 4, max = 50, message = "El usuario debe tener entre 4 y 50 caracteres.") String username,
			@NotBlank(message = "La contraseña es obligatoria.") @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres.") String password,
			@NotBlank(message = "El nombre completo es obligatorio.") @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres.") String nombre,
			@NotBlank(message = "El correo electrónico es obligatorio.") @Email(message = "Por favor, ingrese un correo electrónico válido.") @Size(max = 100, message = "El correo no puede exceder los 100 caracteres.") String email,
			@NotBlank(message = "Debe asignar un rol al usuario.") String rol, String estado) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.nombre = nombre;
		this.email = email;
		this.rol = rol;
		this.estado = estado;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getRol() {
		return rol;
	}


	public void setRol(String rol) {
		this.rol = rol;
	}


	public String getEstado() {
		return estado;
	}


	public void setEstado(String estado) {
		this.estado = estado;
	}


}


    

