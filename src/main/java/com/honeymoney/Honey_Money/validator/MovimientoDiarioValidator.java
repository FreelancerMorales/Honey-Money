package com.honeymoney.Honey_Money.validator;

import com.honeymoney.Honey_Money.model.MovimientoDiario;
import com.honeymoney.Honey_Money.model.MovimientosFinancieros;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class MovimientoDiarioValidator {

    public boolean validarTotales(MovimientoDiario movimientoDiario) {
        BigDecimal totalCalculado = BigDecimal.ZERO;
        
        for (MovimientosFinancieros movimiento : movimientoDiario.getMovimientosFinancieros()) {
            if (movimiento.getTipoMovimiento().getId().equals(movimientoDiario.getTipoMovimiento().getId())) {
                totalCalculado = totalCalculado.add(movimiento.getMonto());
            }
        }
        
        return Math.abs(totalCalculado.doubleValue() - movimientoDiario.getTotal()) < 0.01;
    }

    @Transactional
    public void actualizarTotal(MovimientoDiario movimientoDiario) {
        BigDecimal nuevoTotal = movimientoDiario.getMovimientosFinancieros().stream()
            .filter(m -> m.getTipoMovimiento().getId().equals(movimientoDiario.getTipoMovimiento().getId()))
            .map(MovimientosFinancieros::getMonto)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
            
        movimientoDiario.setTotal(nuevoTotal.doubleValue());
    }
}