package com.example.demo.service;

// Importaciones para trabajar con listas y búsquedas de usuarios.
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// Importaciones para gestionar el inicio de sesión y los permisos.
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;

import jakarta.servlet.http.HttpServletRequest;

// Contiene las operaciones relacionadas con la gestión y autenticación de usuarios.
@Service
public class UsuarioService implements UserDetailsService {

	// Herramientas para consultar usuarios, proteger contraseñas y obtener datos de la solicitud.
	private final UsuarioRepository repo;
	private final PasswordEncoder encoder;
	private final HttpServletRequest request; 

	// Constructor que recibe las herramientas necesarias para trabajar con los usuarios.
	public UsuarioService(UsuarioRepository repo, PasswordEncoder encoder, HttpServletRequest request) {
		this.repo = repo;
		this.encoder = encoder;
		this.request = request;
	}

	// Busca al usuario y verifica sus datos para permitir el inicio de sesión.
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		// Busca el usuario por su nombre; si no existe, muestra un error.
		Usuario u = repo.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("No encontrado: " + username));

		// Comprueba que la cuenta esté activa antes de permitir el acceso.
		if (!"ACTIVO".equals(u.getEstado()))
			throw new UsernameNotFoundException("Usuario inactivo");

		// Obtiene el perfil seleccionado en el formulario de inicio de sesión.
		String perfilSeleccionado = request.getParameter("perfil");

		// Si se seleccionó un perfil, comprueba que coincida con el rol del usuario.
		if (perfilSeleccionado != null) {
			if (!u.getRol().equalsIgnoreCase(perfilSeleccionado.trim())) {
				throw new BadCredentialsException("El perfil seleccionado no corresponde al usuario.");
			}
		}

		// Prepara los permisos que tendrá el usuario dentro del sistema.
		List<GrantedAuthority> auth = new ArrayList<>();

		// Asigna el rol correspondiente para controlar el acceso a las diferentes secciones.
		auth.add(new SimpleGrantedAuthority("ROLE_" + u.getRol()));

		// Devuelve los datos del usuario junto con su contraseña y permisos.
		return new User(u.getUsername(), u.getPassword(), auth);
	}

	// Obtiene la lista completa de usuarios registrados.
	public List<Usuario> listarTodos() {
		return repo.findAll(); 
	}

	// Busca un usuario por su identificador.
	// Si no existe, genera un mensaje de error.
	public Usuario buscarPorId(Long id) {
		return repo.findById(id).orElseThrow(() -> new RuntimeException("No encontrado"));
	}

	// Busca un usuario por su nombre y devuelve el resultado si existe.
	public Optional<Usuario> buscarPorUsername(String u) {
		return repo.findByUsername(u);
	}

	// Guarda un usuario en la base de datos después de proteger su contraseña.
	public void guardar(Usuario u) {
		u.setPassword(encoder.encode(u.getPassword()));
		repo.save(u);
	}

	// Elimina un usuario utilizando su identificador.
	public void eliminar(Long id) {
		repo.deleteById(id);
	}

	// Comprueba si ya existe un usuario con el nombre indicado.
	public boolean existeUsername(String u) {
		return repo.existsByUsername(u);
	}
}