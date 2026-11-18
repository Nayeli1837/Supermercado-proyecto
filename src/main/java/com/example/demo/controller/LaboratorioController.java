package com.example.demo.controller;

// Importaciones necesarias
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.service.SimulatedPthService;

// Controlador para gestionar el laboratorio PTH
@Controller
public class LaboratorioController {

    // Servicio encargado de realizar la simulación PTH
    private final SimulatedPthService pthService;

    // Inicializar el servicio de simulación
    public LaboratorioController(SimulatedPthService pthService) {
        this.pthService = pthService;
    }

    // Mostrar la página del laboratorio
    @GetMapping("/laboratorio/pth")
    public String mostrarLaboratorio() {
        return "laboratorio-pth";
    }

    // Procesar la simulación del laboratorio PTH
    @PostMapping("/laboratorio/pth/simular")
    public String simularPth(
            @RequestParam String username,
            @RequestParam String credencialSimulada,
            Model model) {

        // Ejecutar la simulación con los datos ingresados
        String resultado = pthService.simularIntento(
                username,
                credencialSimulada
        );

        // Enviar el resultado y el usuario a la vista
        model.addAttribute("resultado", resultado);
        model.addAttribute("username", username);

        return "laboratorio-pth";
    }
}
