package com.honeymoney.Honey_Money.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.honeymoney.Honey_Money.model.Ingresos;

@Repository
public interface IngresosRepository extends JpaRepository<Ingresos, Long> {
    List<Ingresos> findByUsuarioId(Long usuarioId);
}