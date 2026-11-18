package com.example.demo.config;

// Importamos las herramientas necesarias para configurar la seguridad del sistema.
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.service.SimulatedPthService;
import com.example.demo.model.Usuario;

// Indica que esta clase contiene las configuraciones de seguridad de la aplicación.
@Configuration
public class SecurityConfig {

    // Variable que permite utilizar el servicio de autenticación de prueba.
    private final SimulatedPthService pthService;

    // Constructor que recibe el servicio necesario para la autenticación.
    public SecurityConfig(SimulatedPthService pthService) {
        this.pthService = pthService;
    }

    // Configura el método que se utilizará para proteger las contraseñas.
    @Bean
    public PasswordEncoder passwordEncoder() {
        // Utiliza BCrypt para guardar las contraseñas de forma segura.
        return new BCryptPasswordEncoder();
    }

    // Define las reglas de seguridad y los permisos para acceder al sistema.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            // Desactiva la protección CSRF para las solicitudes de la aplicación.
            .csrf(csrf -> csrf.disable())

            // Define qué usuarios pueden acceder a cada sección del sistema.
            .authorizeHttpRequests(auth -> auth

                // Permite acceder al inicio de sesión y a los archivos de diseño
                // sin necesidad de iniciar sesión.
                .requestMatchers(
                    "/login",
                    "/css/**",
                    "/js/**",
                    "/img/**"
                ).permitAll()

                // Permite que estos tres tipos de usuarios ingresen al panel principal.
                .requestMatchers("/dashboard")
                .hasAnyRole(
                    Usuario.ADMINISTRADOR,
                    Usuario.ENCARGADO_ALMACEN,
                    Usuario.GERENCIA
                )

                // Solo el administrador puede realizar estas acciones sobre los productos.
                .requestMatchers(
                    "/productos/nuevo/**",
                    "/productos/guardar/**",
                    "/productos/editar/**",
                    "/productos/eliminar/**"
                )
                .hasRole(Usuario.ADMINISTRADOR)

                // Permite consultar la sección de productos a los usuarios autorizados.
                .requestMatchers("/productos/**")
                .hasAnyRole(
                    Usuario.ADMINISTRADOR,
                    Usuario.ENCARGADO_ALMACEN,
                    Usuario.GERENCIA
                )

                // Solo el administrador puede registrar, editar o eliminar proveedores.
                .requestMatchers(
                    "/proveedores/nuevo/**",
                    "/proveedores/guardar/**",
                    "/proveedores/editar/**",
                    "/proveedores/eliminar/**"
                )
                .hasRole(Usuario.ADMINISTRADOR)

                // Permite acceder a la sección de proveedores al administrador
                // y al encargado de almacén.
                .requestMatchers("/proveedores/**")
                .hasAnyRole(
                    Usuario.ADMINISTRADOR,
                    Usuario.ENCARGADO_ALMACEN
                )

                // Restringe el acceso a las categorías únicamente al administrador.
                .requestMatchers("/categorias/**")
                .hasRole(Usuario.ADMINISTRADOR)

                // Permite consultar y gestionar los movimientos al administrador
                // y al encargado de almacén.
                .requestMatchers("/movimientos/**")
                .hasAnyRole(
                    Usuario.ADMINISTRADOR,
                    Usuario.ENCARGADO_ALMACEN
                )

                // Permite acceder a las alertas solamente al encargado de almacén.
                .requestMatchers("/alertas/**")
                .hasRole(Usuario.ENCARGADO_ALMACEN)

                // Permite consultar el historial de movimientos a estos usuarios.
                .requestMatchers("/kardex/**")
                .hasAnyRole(
                    Usuario.ENCARGADO_ALMACEN,
                    Usuario.GERENCIA
                )

                // Permite acceder a los reportes al administrador y a gerencia.
                .requestMatchers("/reportes/**")
                .hasAnyRole(
                    Usuario.ADMINISTRADOR,
                    Usuario.GERENCIA
                )

                // Restringe la administración de usuarios únicamente al administrador.
                .requestMatchers("/usuarios/**")
                .hasRole(Usuario.ADMINISTRADOR)

                // Cualquier otra dirección requiere que el usuario haya iniciado sesión.
                .anyRequest().authenticated()
            )

            // Configura el inicio de sesión mediante un formulario.
            .formLogin(login -> login
                // Establece la dirección donde se encuentra el formulario de acceso.
                .loginPage("/login")

                // Después de iniciar sesión correctamente, dirige al panel principal.
                .defaultSuccessUrl("/dashboard", true)

                // Permite que cualquier persona pueda ingresar a la página de login.
                .permitAll()
            )

            // Indica qué página se mostrará cuando un usuario no tenga permisos
            // para acceder a una sección.
            .exceptionHandling(ex -> ex
                    .accessDeniedPage("/acceso-denegado")
                )

            // PROTECCIÓN CONTRA SESSION HIJACKING / SESSION FIXATION
            // Configura medidas para proteger la sesión del usuario.
            .sessionManagement(session -> session

                // Cambia el identificador de sesión después de iniciar sesión
                // para evitar que se utilice una sesión anterior.
                .sessionFixation(sessionFixation ->
                    sessionFixation.changeSessionId()
                )

                // Permite que cada usuario tenga como máximo una sesión activa.
                .maximumSessions(1)

                // Permite iniciar una nueva sesión aunque ya exista otra activa.
                .maxSessionsPreventsLogin(false)
            )

            // Configura las acciones que se realizan cuando el usuario cierra sesión.
            .logout(logout -> logout

                // Después de cerrar sesión, regresa a la página de inicio de sesión.
                .logoutSuccessUrl("/login")

                // Finaliza la sesión actual del usuario.
                .invalidateHttpSession(true)

                // Elimina la cookie que identifica la sesión del usuario.
                .deleteCookies("JSESSIONID")

                // Permite cerrar sesión sin restricciones de acceso.
                .permitAll()
            );

        // Agrega un filtro personalizado para revisar la autenticación de prueba
        // antes de que se realice la autenticación normal del usuario.
        http.addFilterBefore(
            new LaboratorioPthAuthenticationFilter(pthService),
            UsernamePasswordAuthenticationFilter.class
        );

        // Construye y devuelve la configuración de seguridad del sistema.
        return http.build();
    }

    // Configura el administrador encargado de gestionar la autenticación.
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {

        // Obtiene y devuelve el administrador de autenticación configurado.
        return config.getAuthenticationManager();
    }
}