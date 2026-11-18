package com.example.demo.config;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.service.SimulatedPthService;

public class LaboratorioPthAuthenticationFilter extends OncePerRequestFilter {

    private final SimulatedPthService pthService;

    public LaboratorioPthAuthenticationFilter(SimulatedPthService pthService) {
        this.pthService = pthService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        if ("/login".equals(request.getRequestURI())
                && "POST".equalsIgnoreCase(request.getMethod())) {

            String simulacionPth = request.getParameter("simulacionPth");
            String username = request.getParameter("username");
            String credencialSimulada =
                    request.getParameter("credencialSimulada");

            if ("true".equalsIgnoreCase(simulacionPth)) {

                String resultado = pthService.simularIntento(
                        username,
                        credencialSimulada
                );

                request.getSession().setAttribute(
                        "pthResultado",
                        resultado
                );

                response.sendRedirect("/login?pth=true");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}