package com.honeymoney.Honey_Money.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.honeymoney.Honey_Money.model.MovimientoDiario;
import com.honeymoney.Honey_Money.model.Usuario;
import com.honeymoney.Honey_Money.repository.MovimientoDiarioRepository;
import com.honeymoney.Honey_Money.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CierreDiarioService {
    
    @Autowired
    private MovimientoDiarioRepository movimientoDiarioRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MovimientoDiarioService movimientoDiarioService;
    
    @Transactional
    public void procesarCierreDiarioUsuario(LocalDate fecha, Usuario usuario) {
        // Reutilizar la lógica de actualización
        movimientoDiarioService.actualizarTotalesDiarios(usuario.getId(), fecha);
        
        // Marcar como definitivo
        MovimientoDiario cierre = movimientoDiarioRepository
            .findByFechaAndUsuarioIdAndCierreDefinitivoFalse(fecha, usuario.getId())
            .orElseThrow();
        cierre.setCierreDefinitivo(true);
        movimientoDiarioRepository.save(cierre);
    }

    @Scheduled(cron = "0 59 23 * * *") // Ejecutar a las 23:59 todos los días
    public void realizarCierreDiario() {
        LocalDate fecha = LocalDate.now();
        List<Usuario> usuarios = usuarioRepository.findAll();
        
        for (Usuario usuario : usuarios) {
            procesarCierreDiarioUsuario(fecha, usuario);
        }
    }
}