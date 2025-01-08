package com.honeymoney.Honey_Money.listener;

import com.honeymoney.Honey_Money.model.MovimientosFinancieros;
import com.honeymoney.Honey_Money.model.Usuario;
import jakarta.persistence.*;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class MovimientoFinancieroListener {
    
    @PersistenceContext
    private EntityManager em;

    @PrePersist
    public void onPrePersist(MovimientosFinancieros movimiento) {
        actualizarSaldo(movimiento, true);
    }

    @PreRemove
    public void onPreRemove(MovimientosFinancieros movimiento) {
        actualizarSaldo(movimiento, false);
    }

    private void actualizarSaldo(MovimientosFinancieros movimiento, boolean isAdd) {
        Usuario usuario = movimiento.getUsuario();
        BigDecimal monto = movimiento.getMonto();
        
        if (movimiento.getTipoMovimiento().getId() == 2L) {
            monto = monto.negate();
        }

        Double nuevoSaldo = usuario.getSaldoActual() + (isAdd ? monto.doubleValue() : -monto.doubleValue());
        
        if (nuevoSaldo < 0) {
            throw new IllegalStateException("Saldo insuficiente");
        }

        usuario.setSaldoActual(nuevoSaldo);
        em.merge(usuario);
    }
}