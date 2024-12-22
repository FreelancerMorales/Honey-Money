package com.honeymoney.Honey_Money.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.honeymoney.Honey_Money.model.CategoriaMovimiento;

@Repository
public interface CategoriaMovimientoRepository extends JpaRepository<CategoriaMovimiento, Long> {
    List<CategoriaMovimiento> findByUsuarioId(Long usuarioId);
}