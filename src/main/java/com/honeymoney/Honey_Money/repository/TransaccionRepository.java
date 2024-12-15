package com.honeymoney.Honey_Money.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.honeymoney.Honey_Money.model.Transaccion;

public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {
}