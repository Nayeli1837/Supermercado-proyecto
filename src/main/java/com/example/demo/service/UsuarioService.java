package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

@Service
public class UsuarioService implements UserDetailsService {

	private final UsuarioRepository repo;
	private final PasswordEncoder encoder;
	private final HttpServletRequest request; 

	public UsuarioService(UsuarioRepository repo, PasswordEncoder encoder, HttpServletRequest request) {
		this.repo = repo;
		this.encoder = encoder;
		this.request = request;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario u = repo.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("No encontrado: " + username));
		if (!"ACTIVO".equals(u.getEstado()))
			throw new UsernameNotFoundException("Usuario inactivo");
		
		String perfilSeleccionado = request.getParameter("perfil");
		if (perfilSeleccionado != null) {
			if (!u.getRol().equalsIgnoreCase(perfilSeleccionado.trim())) {
				throw new BadCredentialsException("El perfil seleccionado no corresponde al usuario.");
			}
		}
		List<GrantedAuthority> auth = new ArrayList<>();
		auth.add(new SimpleGrantedAuthority("ROLE_" + u.getRol()));
		return new User(u.getUsername(), u.getPassword(), auth);
	}

	public List<Usuario> listarTodos() {
		return repo.findAll(); 
	}

	public Usuario buscarPorId(Long id) {
		return repo.findById(id).orElseThrow(() -> new RuntimeException("No encontrado"));
	}

	public Optional<Usuario> buscarPorUsername(String u) {
		return repo.findByUsername(u);
	}

	public void guardar(Usuario u) {
		u.setPassword(encoder.encode(u.getPassword()));
		repo.save(u);
	}

	public void eliminar(Long id) {
		repo.deleteById(id);
	}

	public boolean existeUsername(String u) {
		return repo.existsByUsername(u);
	}
}
