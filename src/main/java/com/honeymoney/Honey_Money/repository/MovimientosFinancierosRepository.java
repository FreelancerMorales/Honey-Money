package com.honeymoney.Honey_Money.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.honeymoney.Honey_Money.model.MovimientosFinancieros;

@Repository
public interface MovimientosFinancierosRepository extends JpaRepository<MovimientosFinancieros, Long> {
    List<MovimientosFinancieros> findByUsuarioId(Long usuarioId);
}
