package com.honeymoney.Honey_Money.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.honeymoney.Honey_Money.model.MovimientosFinancieros;

@Repository
public interface MovimientosFinancierosRepository extends JpaRepository<MovimientosFinancieros, Long> {
    List<MovimientosFinancieros> findByUsuarioId(Long usuarioId);
    
    @Query("SELECT DISTINCT m FROM MovimientosFinancieros m " +
            "JOIN FETCH m.tipoMovimiento tm " +
            "JOIN FETCH m.usuario u " +
            "LEFT JOIN FETCH m.categoria c " +
            "WHERE m.usuario.id = :usuarioId " +
            "AND m.fecha = :fecha " +
            "AND m.tipoMovimiento IS NOT NULL")
    List<MovimientosFinancieros> findByUsuarioIdAndFecha(
        @Param("usuarioId") Long usuarioId, 
        @Param("fecha") LocalDate fecha
    );
}