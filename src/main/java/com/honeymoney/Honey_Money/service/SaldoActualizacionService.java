package com.honeymoney.Honey_Money.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.honeymoney.Honey_Money.model.GastoMensual;
import com.honeymoney.Honey_Money.model.Usuario;

import org.springframework.beans.factory.annotation.Autowired;
import com.honeymoney.Honey_Money.repository.UsuarioRepository;

@Service
public class SaldoActualizacionService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Transactional
    public void actualizarSaldoPorGastoMensual(Usuario usuario, GastoMensual gasto) {
        Double saldoActual = usuario.getSaldoActual();
        Double nuevoSaldo = saldoActual - gasto.getMonto().doubleValue();
        
        usuario.setSaldoActual(nuevoSaldo);
        usuarioRepository.save(usuario);
    }
}