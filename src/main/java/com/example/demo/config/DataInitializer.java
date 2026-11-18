package com.example.demo.config;

// Importamos las herramientas necesarias para ejecutar tareas al iniciar la aplicación.
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;

// Indica que esta clase se ejecutará automáticamente al iniciar el sistema.
@Component
public class DataInitializer implements CommandLineRunner {

    // Variables que permiten trabajar con los usuarios y proteger sus contraseñas.
    private final UsuarioRepository repo;
    private final PasswordEncoder encoder;

    // Constructor que recibe las herramientas necesarias para esta clase.
    public DataInitializer(UsuarioRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    // Este método se ejecuta automáticamente cuando inicia la aplicación.
    @Override
    public void run(String... args) {
        try {
            // Verifica si ya existe un usuario con el nombre "admin".
            if (!repo.existsByUsername("admin")) {

                // Crea un nuevo usuario administrador.
                Usuario u = new Usuario();

                // Asigna los datos iniciales del administrador.
                u.setUsername("admin");

                // Guarda la contraseña de forma protegida.
                u.setPassword(encoder.encode("admin123"));

                // Establece el rol de administrador.
                u.setRol(Usuario.ADMINISTRADOR);

                // Asigna el nombre del administrador.
                u.setNombre("Administrador Sistema");

                // Registra el correo electrónico del administrador.
                u.setEmail("admin@plazaquattro.pe");

                // Establece la cuenta como activa.
                u.setEstado("ACTIVO");

                // Guarda el nuevo usuario en la base de datos.
                repo.save(u);

                // Muestra un mensaje indicando que el administrador fue creado.
                System.out.println(">>> Administrador inicial creado con éxito (usuario: admin / clave: admin123)");

            } else {
                // Si el administrador ya existe, muestra un mensaje y no lo vuelve a crear.
                System.out.println("El usuario 'admin' ya existe en la base de datos. Saltando creación.");
            }

        } catch (org.springframework.dao.DataIntegrityViolationException e) {

            // Captura el error si se intenta registrar un usuario que ya existe.
            System.out.println("Se intentó duplicar el usuario 'admin', pero se interceptó de forma segura.");
        }
    }
}
