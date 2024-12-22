package com.honeymoney.Honey_Money.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.honeymoney.Honey_Money.model.MovimientoDiario;

@Repository
public interface MovimientoDiarioRepository extends JpaRepository<MovimientoDiario, Long> {
    List<MovimientoDiario> findByUsuarioIdAndFecha(Long usuarioId, LocalDate fecha);
}