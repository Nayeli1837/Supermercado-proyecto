package com.example.demo.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository repo;
    private final PasswordEncoder encoder;

    public DataInitializer(UsuarioRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        try {
            if (!repo.existsByUsername("admin")) {
                Usuario u = new Usuario();
                u.setUsername("admin");
                u.setPassword(encoder.encode("admin123"));
                u.setRol(Usuario.ADMINISTRADOR);
                u.setNombre("Administrador Sistema");
                u.setEmail("admin@plazaquattro.pe");
                u.setEstado("ACTIVO");
                
                repo.save(u);
                System.out.println(">>> Administrador inicial creado con éxito (usuario: admin / clave: admin123)");
            } else {
                System.out.println("El usuario 'admin' ya existe en la base de datos. Saltando creación.");
            }
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            System.out.println("Se intentó duplicar el usuario 'admin', pero se interceptó de forma segura.");
        }
    }
}
