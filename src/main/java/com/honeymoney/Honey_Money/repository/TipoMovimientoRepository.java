package com.honeymoney.Honey_Money.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.honeymoney.Honey_Money.model.TipoMovimiento;

@Repository
public interface TipoMovimientoRepository extends JpaRepository<TipoMovimiento, Long> {
}
