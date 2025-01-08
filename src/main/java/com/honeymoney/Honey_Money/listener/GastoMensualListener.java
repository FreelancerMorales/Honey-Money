package com.honeymoney.Honey_Money.listener;

import jakarta.persistence.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.honeymoney.Honey_Money.model.GastoMensual;
import com.honeymoney.Honey_Money.model.MovimientosFinancieros;

import java.time.LocalDate;

@Component
public class GastoMensualListener {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Scheduled(cron = "0 0 0 * * *") // Ejecutar diariamente a medianoche
    public void procesarGastosMensuales() {
        LocalDate today = LocalDate.now();
        
        // Buscar gastos mensuales activos que apliquen hoy
        String jpql = "SELECT g FROM GastoMensual g WHERE g.activo = true " +
                        "AND g.fechaInicio <= :today AND g.fechaFin >= :today";
        // Procesar cada gasto mensual
        entityManager.createQuery(jpql, GastoMensual.class)
            .setParameter("today", today)
            .getResultList()
            .forEach(this::generarMovimiento);
    }
    
    private void generarMovimiento(GastoMensual gasto) {
        // Crear movimiento financiero automático
        MovimientosFinancieros movimiento = new MovimientosFinancieros();
        movimiento.setMonto(gasto.getMonto());
        movimiento.setDescripcion("Gasto mensual: " + gasto.getDescripcion());
        movimiento.setFecha(LocalDate.now());
        movimiento.setUsuario(gasto.getUsuario());
        // ... configurar otros campos necesarios
        
        entityManager.persist(movimiento);
    }
}