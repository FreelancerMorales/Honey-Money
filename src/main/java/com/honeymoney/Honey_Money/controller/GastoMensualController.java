package com.honeymoney.Honey_Money.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.honeymoney.Honey_Money.model.GastoMensual;
import com.honeymoney.Honey_Money.repository.GastoMensualRepository;

@RestController
@RequestMapping("/api/gastos-mensuales")
public class GastoMensualController {

    @Autowired
    private GastoMensualRepository gastoMensualRepository;

    // Obtener todos los gastos mensuales por usuario
    @GetMapping("/usuario/{usuarioId}")
    public List<GastoMensual> obtenerGastosMensualesPorUsuario(@PathVariable Long usuarioId) {
        return gastoMensualRepository.findByUsuarioId(usuarioId);
    }

    // Crear un nuevo gasto mensual
    @PostMapping
    public GastoMensual crearGastoMensual(@RequestBody GastoMensual gasto) {
        return gastoMensualRepository.save(gasto);
    }

    // Actualizar un gasto mensual
    @PutMapping("/{id}")
    public ResponseEntity<GastoMensual> actualizarGastoMensual(@PathVariable Long id, @RequestBody GastoMensual detallesGasto) {
        return gastoMensualRepository.findById(id)
                .map(gasto -> {
                    gasto.setMonto(detallesGasto.getMonto());
                    gasto.setDescripcion(detallesGasto.getDescripcion());
                    gasto.setFechaInicio(detallesGasto.getFechaInicio());
                    gasto.setFechaFin(detallesGasto.getFechaFin());
                    gasto.setActivo(detallesGasto.getActivo());
                    return ResponseEntity.ok(gastoMensualRepository.save(gasto));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Eliminar un gasto mensual
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarGastoMensual(@PathVariable Long id) {
        if (gastoMensualRepository.existsById(id)) {
            gastoMensualRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}