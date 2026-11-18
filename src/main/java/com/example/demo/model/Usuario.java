package com.example.demo.model;

// Importamos las herramientas para relacionar esta clase con la base de datos.
import jakarta.persistence.*;

// Importamos las validaciones que se aplican a los datos del usuario.
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Indica que esta clase representa una tabla dentro de la base de datos.
@Entity
@Table(name = "usuarios")
public class Usuario {

    // ACTORES DEL SISTEMA
    // Define los diferentes roles que pueden tener los usuarios del sistema.
    public static final String ADMINISTRADOR = "ADMINISTRADOR";
    public static final String ENCARGADO_ALMACEN = "ALMACEN";
    public static final String GERENCIA = "GERENCIA";

    // Identificador único de cada usuario.
    @Id
    // La base de datos genera automáticamente el número de identificación.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Comprueba que el nombre de usuario no esté vacío y tenga entre 4 y 50 caracteres.
    @NotBlank(message = "El nombre de usuario es obligatorio.")
    @Size(min = 4, max = 50, message = "El usuario debe tener entre 4 y 50 caracteres.")
    // Guarda el nombre de usuario y evita que se repita en la base de datos.
    @Column(unique = true, nullable = false, length = 50)
    private String username;

    // Comprueba que la contraseña no esté vacía y tenga al menos 6 caracteres.
    @NotBlank(message = "La contraseña es obligatoria.")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres.")
    // Indica que la contraseña es un dato obligatorio.
    @Column(nullable = false)
    private String password;

    // Comprueba que el nombre completo no esté vacío y no supere los 100 caracteres.
    @NotBlank(message = "El nombre completo es obligatorio.")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres.")
    // Guarda el nombre completo del usuario.
    @Column(nullable = false, length = 100)
    private String nombre;

    // Comprueba que el correo no esté vacío.
    @NotBlank(message = "El correo electrónico es obligatorio.")
    // Verifica que el correo tenga un formato válido.
    @Email(message = "Por favor, ingrese un correo electrónico válido.")
    @Size(max = 100, message = "El correo no puede exceder los 100 caracteres.")
    // Evita que dos usuarios tengan el mismo correo electrónico.
    @Column(unique = true, nullable = false, length = 100)
    private String email;

    // Comprueba que cada usuario tenga un rol asignado.
    @NotBlank(message = "Debe asignar un rol al usuario.")
    // Guarda el rol correspondiente al usuario.
    @Column(nullable = false, length = 50)
    private String rol;

    // Guarda el estado de la cuenta. Por defecto, el usuario se crea como activo.
	@Column(nullable = false)
	private String estado = "ACTIVO";

    // Constructor vacío que permite crear un usuario sin ingresar datos inicialmente.
    public Usuario() {}


    // Constructor que permite crear un usuario ingresando todos sus datos.
	public Usuario(Long id,
			@NotBlank(message = "El nombre de usuario es obligatorio.") @Size(min = 4, max = 50, message = "El usuario debe tener entre 4 y 50 caracteres.") String username,
			@NotBlank(message = "La contraseña es obligatoria.") @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres.") String password,
			@NotBlank(message = "El nombre completo es obligatorio.") @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres.") String nombre,
			@NotBlank(message = "El correo electrónico es obligatorio.") @Email(message = "Por favor, ingrese un correo electrónico válido.") @Size(max = 100, message = "El correo no puede exceder los 100 caracteres.") String email,
			@NotBlank(message = "Debe asignar un rol al usuario.") String rol, String estado) {
		super();

        // Asigna a cada atributo el valor recibido en el constructor.
		this.id = id;
		this.username = username;
		this.password = password;
		this.nombre = nombre;
		this.email = email;
		this.rol = rol;
		this.estado = estado;
	}

    // GETTERS Y SETTERS
    // Los métodos GET permiten consultar los datos de cada atributo.
    // Los métodos SET permiten asignar o modificar sus valores.

    // Obtiene el identificador del usuario.
	public Long getId() {
		return id;
	}

    // Asigna un identificador al usuario.
	public void setId(Long id) {
		this.id = id;
	}

    // Obtiene el nombre de usuario.
	public String getUsername() {
		return username;
	}

    // Permite establecer o cambiar el nombre de usuario.
	public void setUsername(String username) {
		this.username = username;
	}

    // Obtiene la contraseña del usuario.
	public String getPassword() {
		return password;
	}

    // Permite establecer o cambiar la contraseña.
	public void setPassword(String password) {
		this.password = password;
	}

    // Obtiene el nombre completo del usuario.
	public String getNombre() {
		return nombre;
	}

    // Permite establecer o cambiar el nombre completo.
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

    // Obtiene el correo electrónico del usuario.
	public String getEmail() {
		return email;
	}

    // Permite establecer o cambiar el correo electrónico.
	public void setEmail(String email) {
		this.email = email;
	}

    // Obtiene el rol asignado al usuario.
	public String getRol() {
		return rol;
	}

    // Permite asignar o cambiar el rol del usuario.
	public void setRol(String rol) {
		this.rol = rol;
	}

    // Obtiene el estado actual de la cuenta.
	public String getEstado() {
		return estado;
	}

    // Permite cambiar el estado de la cuenta.
	public void setEstado(String estado) {
		this.estado = estado;
	}


}


    

