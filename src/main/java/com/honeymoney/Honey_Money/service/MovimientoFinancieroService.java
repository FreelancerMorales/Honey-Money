package com.honeymoney.Honey_Money.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.honeymoney.Honey_Money.model.MovimientosFinancieros;
import com.honeymoney.Honey_Money.model.Usuario;
import com.honeymoney.Honey_Money.repository.MovimientosFinancierosRepository;
import com.honeymoney.Honey_Money.repository.UsuarioRepository;

import java.math.BigDecimal;

@Service
@Transactional
public class MovimientoFinancieroService {

    @Autowired
    private MovimientosFinancierosRepository movimientoRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    public MovimientosFinancieros saveMovimiento(MovimientosFinancieros movimiento) {
        actualizarSaldo(movimiento, true);
        return movimientoRepository.save(movimiento);
    }
    
    public void deleteMovimiento(MovimientosFinancieros movimiento) {
        actualizarSaldo(movimiento, false);
        movimientoRepository.delete(movimiento);
    }

    private void actualizarSaldo(MovimientosFinancieros movimiento, boolean isAdd) {
        Usuario usuario = movimiento.getUsuario();
        BigDecimal monto = movimiento.getMonto();
        
        // Si es gasto (ID 2), el monto se vuelve negativo
        if (movimiento.getTipoMovimiento().getId() == 2L) {
            monto = monto.negate();
        }

        Double nuevoSaldo = usuario.getSaldoActual() + (isAdd ? monto.doubleValue() : -monto.doubleValue());
        
        if (nuevoSaldo < 0) {
            throw new IllegalStateException("Saldo insuficiente");
        }

        usuario.setSaldoActual(nuevoSaldo);
        usuarioRepository.save(usuario);
    }
}