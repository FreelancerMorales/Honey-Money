package com.honeymoney.Honey_Money.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.honeymoney.Honey_Money.model.CategoriaMovimiento;
import com.honeymoney.Honey_Money.model.MovimientosFinancieros;
import com.honeymoney.Honey_Money.model.TipoMovimiento;
import com.honeymoney.Honey_Money.model.Usuario;
import com.honeymoney.Honey_Money.model.DTO.MovimientoFinancieroDTO;
import com.honeymoney.Honey_Money.repository.CategoriaMovimientoRepository;
import com.honeymoney.Honey_Money.repository.MovimientosFinancierosRepository;
import com.honeymoney.Honey_Money.repository.TipoMovimientoRepository;
import com.honeymoney.Honey_Money.repository.UsuarioRepository;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@Transactional
public class MovimientoFinancieroService {

    @Autowired
    private MovimientosFinancierosRepository movimientoRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MovimientoDiarioService movimientoDiarioService;
    
    @Autowired
    private TipoMovimientoRepository tipoMovimientoRepository;
    
    @Autowired
    private CategoriaMovimientoRepository categoriaMovimientoRepository;

    @Transactional
    public MovimientosFinancieros saveMovimiento(MovimientoFinancieroDTO movimientoDTO) {
        // Validar DTO
        validarMovimientoDTO(movimientoDTO);

        // Crear nueva instancia
        MovimientosFinancieros movimiento = new MovimientosFinancieros();
        
        // Setear datos básicos con validación
        if (movimientoDTO.getMonto() != null) {
            movimiento.setMonto(movimientoDTO.getMonto());
        }
        movimiento.setDescripcion(movimientoDTO.getDescripcion());
        movimiento.setFecha(movimientoDTO.getFecha());
        
        // Buscar y setear Usuario
        Usuario usuario = usuarioRepository.findById(movimientoDTO.getUsuarioId())
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        movimiento.setUsuario(usuario);
        
        // Buscar y setear TipoMovimiento con validación adicional
        TipoMovimiento tipoMovimiento = tipoMovimientoRepository.findById(movimientoDTO.getTipoMovimientoId())
            .orElseThrow(() -> new IllegalArgumentException("Tipo de movimiento no encontrado"));
        if (tipoMovimiento == null) {
            throw new IllegalStateException("TipoMovimiento no puede ser null después de recuperarlo");
        }
        movimiento.setTipoMovimiento(tipoMovimiento);

        // Buscar y setear Categoría
        CategoriaMovimiento categoria = categoriaMovimientoRepository.findById(movimientoDTO.getCategoriaId())
            .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));
        movimiento.setCategoria(categoria);
        
        // Validar movimiento completo
        validarMovimiento(movimiento);
        
        // Guardar movimiento
        MovimientosFinancieros saved = movimientoRepository.save(movimiento);
        
        // Actualizar saldo del usuario
        actualizarSaldo(saved, true);
        
        // Actualizar movimiento diario
        actualizarMovimientoDiarioTemporal(saved);
        
        return saved;
    }

    @Transactional
    public void deleteMovimiento(MovimientosFinancieros movimiento) {
        if (movimiento == null) {
            throw new IllegalArgumentException("El movimiento no puede ser null");
        }
        
        // Revertir el saldo
        actualizarSaldo(movimiento, false);
        
        // Eliminar el movimiento
        movimientoRepository.delete(movimiento);
        
        // Re-calcular movimiento diario
        movimientoDiarioService.actualizarTotalesDiarios(
            movimiento.getUsuario().getId(), 
            movimiento.getFecha()
        );
    }

    private void actualizarMovimientoDiarioTemporal(MovimientosFinancieros movimiento) {
        if (movimiento == null || movimiento.getUsuario() == null || movimiento.getFecha() == null) {
            throw new IllegalArgumentException("Datos insuficientes para actualizar movimiento diario");
        }
        movimientoDiarioService.actualizarTotalesDiarios(
            movimiento.getUsuario().getId(), 
            movimiento.getFecha()
        );
    }

    private void actualizarSaldo(MovimientosFinancieros movimiento, boolean isAdd) {
        if (movimiento == null || movimiento.getTipoMovimiento() == null) {
            throw new IllegalArgumentException("El movimiento o tipo de movimiento no puede ser null");
        }

        Usuario usuario = movimiento.getUsuario();
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser null");
        }

        BigDecimal monto = movimiento.getMonto();
        if (monto == null) {
            throw new IllegalArgumentException("El monto no puede ser null");
        }

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

    private void validarMovimientoDTO(MovimientoFinancieroDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("El DTO no puede ser null");
        }
        // Fix BigDecimal comparison
        if (dto.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a 0");
        }
        if (dto.getFecha() == null) {
            throw new IllegalArgumentException("La fecha es requerida");
        }
        if (dto.getFecha().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("No se pueden registrar movimientos futuros");
        }
    }

    private void validarMovimiento(MovimientosFinancieros movimiento) {
        if (movimiento == null) {
            throw new IllegalArgumentException("El movimiento no puede ser null");
        }
        if (movimiento.getTipoMovimiento() == null) {
            throw new IllegalArgumentException("El tipo de movimiento es requerido");
        }
        if (movimiento.getUsuario() == null) {
            throw new IllegalArgumentException("El usuario es requerido");
        }
        if (movimiento.getMonto() == null) {
            throw new IllegalArgumentException("El monto es requerido");
        }
        if (movimiento.getFecha() == null) {
            throw new IllegalArgumentException("La fecha es requerida");
        }
        if (movimiento.getCategoria() == null) {
            throw new IllegalArgumentException("La categoría es requerida");
        }
    }
}