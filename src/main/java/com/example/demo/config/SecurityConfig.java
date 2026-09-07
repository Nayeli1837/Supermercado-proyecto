package com.example.demo.config;

import org.springframework.context.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.demo.model.Usuario;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth


                .requestMatchers("/login", "/css/**", "/js/**", "/img/**").permitAll()


                .requestMatchers("/dashboard")
                .hasAnyRole(
                        Usuario.ADMINISTRADOR,
                        Usuario.ENCARGADO_ALMACEN,
                        Usuario.GERENCIA
                )


                .requestMatchers(
                        "/productos/nuevo/**",
                        "/productos/guardar/**",
                        "/productos/editar/**",
                        "/productos/eliminar/**"
                ).hasRole(Usuario.ADMINISTRADOR)

                .requestMatchers("/productos/**")
                .hasAnyRole(
                        Usuario.ADMINISTRADOR,
                        Usuario.ENCARGADO_ALMACEN,
                        Usuario.GERENCIA
                )

                .requestMatchers(
                        "/proveedores/nuevo/**",
                        "/proveedores/guardar/**",
                        "/proveedores/editar/**",
                        "/proveedores/eliminar/**"
                ).hasRole(Usuario.ADMINISTRADOR)

                .requestMatchers("/proveedores/**")
                .hasAnyRole(
                        Usuario.ADMINISTRADOR,
                        Usuario.ENCARGADO_ALMACEN
                )


                .requestMatchers("/categorias/**")
                .hasRole(Usuario.ADMINISTRADOR)


                .requestMatchers("/movimientos/**")
                .hasAnyRole(
                		Usuario.ADMINISTRADOR,
                		Usuario.ENCARGADO_ALMACEN)

                .requestMatchers("/alertas/**")
                .hasRole(Usuario.ENCARGADO_ALMACEN)

                .requestMatchers("/kardex/**")
                .hasAnyRole(
                        Usuario.ENCARGADO_ALMACEN,
                        Usuario.GERENCIA
                )

                .requestMatchers("/reportes/**")
                .hasAnyRole(
                        Usuario.ADMINISTRADOR,
                        Usuario.GERENCIA
                )

                .requestMatchers("/usuarios/**")
                .hasRole(Usuario.ADMINISTRADOR)

                .anyRequest().authenticated()
            )

            .formLogin(login -> login
                    .loginPage("/login")
                    .defaultSuccessUrl("/dashboard", true)
                    .permitAll()
            )

            .logout(logout -> logout
                    .logoutSuccessUrl("/login")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .permitAll()
            );

        return http.build();
    }
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
