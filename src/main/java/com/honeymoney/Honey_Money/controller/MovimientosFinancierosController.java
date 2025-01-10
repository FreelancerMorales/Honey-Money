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
import com.honeymoney.Honey_Money.model.MovimientosFinancieros;
import com.honeymoney.Honey_Money.model.DTO.MovimientoFinancieroDTO;
import com.honeymoney.Honey_Money.repository.MovimientoDiarioRepository;
import com.honeymoney.Honey_Money.repository.MovimientosFinancierosRepository;
import com.honeymoney.Honey_Money.service.MovimientoFinancieroService;

@RestController
@RequestMapping("/api/movimientos")
public class MovimientosFinancierosController {

    @Autowired
    private MovimientosFinancierosRepository movimientosFinancierosRepository;

    @Autowired
    private MovimientoFinancieroService movimientoFinancieroService;

    @Autowired
    private MovimientoDiarioRepository movimientoDiarioRepository;

    // Obtener todos los movimientos financieros por usuario
    @GetMapping("/usuario/{usuarioId}")
    public List<MovimientosFinancieros> obtenerMovimientosPorUsuario(@PathVariable Long usuarioId) {
        return movimientosFinancierosRepository.findByUsuarioId(usuarioId);
    }

        @GetMapping("/totales-diarios/{usuarioId}/{fecha}")
    public MovimientoDiario obtenerTotalesDiarios(
        @PathVariable Long usuarioId,
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return movimientoDiarioRepository
            .findByFechaAndUsuarioIdAndCierreDefinitivoFalse(fecha, usuarioId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<MovimientosFinancieros> crearMovimiento(@RequestBody MovimientoFinancieroDTO movimientoDTO) {
        MovimientosFinancieros movimientoGuardado = movimientoFinancieroService.saveMovimiento(movimientoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(movimientoGuardado);
    }

    // Actualizar un movimiento financiero
    @PutMapping("/{id}")
    public ResponseEntity<MovimientosFinancieros> actualizarMovimiento(@PathVariable Long id, @RequestBody MovimientoFinancieroDTO movimientoDTO) {
        return movimientosFinancierosRepository.findById(id)
            .map(movimiento -> {
                MovimientosFinancieros actualizado = movimientoFinancieroService.saveMovimiento(movimientoDTO);
                return ResponseEntity.ok(actualizado);
            })
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    // Eliminar un movimiento financiero
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminarMovimiento(@PathVariable Long id) {
        return movimientosFinancierosRepository.findById(id)
            .map(movimiento -> {
                movimientoFinancieroService.deleteMovimiento(movimiento);
                return ResponseEntity.noContent().build();
            })
            .orElse(ResponseEntity.notFound().build());
    }
}