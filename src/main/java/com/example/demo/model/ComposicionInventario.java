package com.example.demo.model;

// Record que permite almacenar información resumida del inventario por categoría.
public record ComposicionInventario(

        // Nombre de la categoría del producto.
        String categoria,

        // Cantidad de productos registrados en esa categoría.
        Long cantidad,

        // Valor del ancho asociado a la composición del inventario.
        Double ancho

) {}
