package com.example.demo.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SimulatedPthService {

    public String simularIntento(String username, String credencialSimulada) {

        if (username == null || username.isBlank()) {
            return "❌ Usuario no válido.";
        }

        if (credencialSimulada == null || credencialSimulada.isBlank()) {
            return "❌ No se proporcionó una credencial simulada.";
        }

        // Simulación académica:
        // No se utiliza ningún hash real de Windows.
        boolean detectado = credencialSimulada.startsWith("PTH-SIMULADO-");

        if (detectado) {

            return """
                    🚨 ALERTA DE SEGURIDAD

                    Tipo: Pass-the-Hash simulado
                    Usuario objetivo: %s
                    Fecha: %s
                    Estado: BLOQUEADO
                    Acción: Intento rechazado
                    """.formatted(
                    username,
                    LocalDateTime.now()
            );
        }

        return """
                🔵 ACCESO NORMAL

                Usuario: %s
                Fecha: %s
                Estado: PERMITIDO
                """.formatted(
                username,
                LocalDateTime.now()
        );
    }
}