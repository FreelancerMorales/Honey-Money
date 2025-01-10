package com.honeymoney.Honey_Money.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.honeymoney.Honey_Money.model.MovimientoDiario;

@Repository
public interface MovimientoDiarioRepository extends JpaRepository<MovimientoDiario, Long> {
    List<MovimientoDiario> findByUsuarioIdAndFecha(Long usuarioId, LocalDate fecha);
    Optional<MovimientoDiario> findByFechaAndUsuarioId(LocalDate fecha, Long usuarioId);
    boolean existsByFechaAndUsuarioId(LocalDate fecha, Long usuarioId);

    Optional<MovimientoDiario> findByFechaAndUsuarioIdAndCierreDefinitivoFalse(LocalDate fecha, Long usuarioId);

    List<MovimientoDiario> findByFechaAndCierreDefinitivoFalse(LocalDate fecha);
}