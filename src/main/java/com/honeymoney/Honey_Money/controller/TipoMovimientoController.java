package com.honeymoney.Honey_Money.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.honeymoney.Honey_Money.model.TipoMovimiento;
import com.honeymoney.Honey_Money.repository.TipoMovimientoRepository;

@RestController
@RequestMapping("/api/tipos-movimiento")
public class TipoMovimientoController {

    @Autowired
    private TipoMovimientoRepository tipoMovimientoRepository;

    // Obtener todos los tipos de movimiento
    @GetMapping
    public List<TipoMovimiento> obtenerTodos() {
        return tipoMovimientoRepository.findAll();
    }

    // Crear un nuevo tipo de movimiento
    @PostMapping
    public ResponseEntity<TipoMovimiento> crearTipoMovimiento(@RequestBody TipoMovimiento tipoMovimiento) {
        TipoMovimiento tipoGuardado = tipoMovimientoRepository.save(tipoMovimiento);
        return ResponseEntity.status(HttpStatus.CREATED).body(tipoGuardado);
    }

    // Eliminar un tipo de movimiento
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTipoMovimiento(@PathVariable Long id) {
        if (tipoMovimientoRepository.existsById(id)) {
            tipoMovimientoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
