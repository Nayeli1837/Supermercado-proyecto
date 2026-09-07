package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Movimiento;
@Repository

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
	void deleteByProductoId(Long productoId);
}

