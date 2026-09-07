package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Producto;

@Repository

public interface ProductoRepository extends JpaRepository<Producto, Long> {
	List<Producto> findByNombreContainingIgnoreCase(String nombre);
	List<Producto> findByCategoriaId(Long categoriaId);
    @Query("""
            SELECT p
            FROM Producto p
            WHERE p.stock <= p.stockMinimo
            ORDER BY p.stock ASC
        """)
        List<Producto> obtenerAlertasStock();
    @Query("""
    		SELECT p.categoria.nombre, COUNT(p)
    		FROM Producto p
    		GROUP BY p.categoria.nombre
    		ORDER BY COUNT(p) DESC
    		""")
    		List<Object[]> obtenerComposicionInventario();
    		
    		@Query("SELECT COUNT(p) FROM Producto p")
    		long contarProductos();

    		@Query("SELECT COUNT(p) FROM Producto p WHERE p.stock = 0")
    		long contarProductosAgotados();

    		@Query("""
    		       SELECT COUNT(p)
    		       FROM Producto p
    		       WHERE p.stock > 0
    		         AND p.stock <= p.stockMinimo
    		       """)
    		long contarProductosStockBajo();
}