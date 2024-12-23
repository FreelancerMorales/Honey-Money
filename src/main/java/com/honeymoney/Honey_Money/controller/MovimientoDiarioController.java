package com.honeymoney.Honey_Money.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.honeymoney.Honey_Money.model.MovimientoDiario;
import com.honeymoney.Honey_Money.repository.MovimientoDiarioRepository;
import com.honeymoney.Honey_Money.repository.MovimientosFinancierosRepository;

@RestController
@RequestMapping("/api/movimientos-diarios")
public class MovimientoDiarioController {

    @Autowired
    private MovimientoDiarioRepository movimientoDiarioRepository;

    @Autowired
    private MovimientosFinancierosRepository movimientosFinancierosRepository;

    // Obtener movimientos diarios por usuario y fecha
    @GetMapping("/usuario/{usuarioId}/fecha/{fecha}")
    public List<MovimientoDiario> obtenerMovimientosPorUsuarioYFecha(
            @PathVariable Long usuarioId, 
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return movimientoDiarioRepository.findByUsuarioIdAndFecha(usuarioId, fecha);
    }

    // Crear un nuevo movimiento diario
    @PostMapping
    public ResponseEntity<MovimientoDiario> crearMovimientoDiario(@RequestBody MovimientoDiario movimiento) {
        // Validar que los movimientos financieros relacionados existan
        if (movimiento.getMovimientosFinancieros() != null) {
            movimiento.getMovimientosFinancieros().forEach(financiero -> {
                if (!movimientosFinancierosRepository.existsById(financiero.getId())) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, 
                        "Movimiento financiero con ID " + financiero.getId() + " no encontrado");
                }
            });
        }
        MovimientoDiario movimientoGuardado = movimientoDiarioRepository.save(movimiento);
        return ResponseEntity.status(HttpStatus.CREATED).body(movimientoGuardado);
    }

    // Actualizar un movimiento diario
    @PutMapping("/{id}")
    public ResponseEntity<MovimientoDiario> actualizarMovimientoDiario(
            @PathVariable Long id, 
            @RequestBody MovimientoDiario detallesMovimiento) {
        return movimientoDiarioRepository.findById(id)
                .map(movimiento -> {
                    movimiento.setFecha(detallesMovimiento.getFecha());
                    movimiento.setTotal(detallesMovimiento.getTotal());
                    movimiento.setMovimientosFinancieros(detallesMovimiento.getMovimientosFinancieros());
                    return ResponseEntity.ok(movimientoDiarioRepository.save(movimiento));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Eliminar un movimiento diario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMovimientoDiario(@PathVariable Long id) {
        if (movimientoDiarioRepository.existsById(id)) {
            movimientoDiarioRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}