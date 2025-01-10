package com.honeymoney.Honey_Money.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.honeymoney.Honey_Money.model.TipoMovimiento;

@Repository
public interface TipoMovimientoRepository extends JpaRepository<TipoMovimiento, Long> {
    @Query("SELECT t FROM TipoMovimiento t WHERE t.id = :id")
    Optional<TipoMovimiento> findByIdWithMovimientos(Long id);
}