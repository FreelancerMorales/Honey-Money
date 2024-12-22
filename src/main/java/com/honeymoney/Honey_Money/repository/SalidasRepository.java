package com.honeymoney.Honey_Money.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.honeymoney.Honey_Money.model.Salidas;

@Repository
public interface SalidasRepository extends JpaRepository<Salidas, Long> {
    List<Salidas> findByUsuarioId(Long usuarioId);
}