package com.honeymoney.Honey_Money.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.honeymoney.Honey_Money.model.GastoMensual;
import com.honeymoney.Honey_Money.model.Usuario;
import com.honeymoney.Honey_Money.model.DTO.GastoMensualDTO;
import com.honeymoney.Honey_Money.repository.GastoMensualRepository;
import com.honeymoney.Honey_Money.repository.UsuarioRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/gastos-mensuales")
public class GastoMensualController {

    @Autowired
    private GastoMensualRepository gastoMensualRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Obtener todos los gastos mensuales por usuario
    @GetMapping("/usuario/{usuarioId}")
    public List<GastoMensual> obtenerGastosMensualesPorUsuario(@PathVariable Long usuarioId) {
        return gastoMensualRepository.findByUsuarioId(usuarioId);
    }

    // Crear un nuevo gasto mensual
    @PostMapping
    public ResponseEntity<GastoMensual> crearGastoMensual(@Valid @RequestBody GastoMensualDTO gastoDTO) {
        GastoMensual gasto = new GastoMensual();
        Usuario usuario = usuarioRepository.findById(gastoDTO.getUsuarioId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        
        gasto.setUsuario(usuario);
        gasto.setMonto(gastoDTO.getMonto());
        gasto.setDescripcion(gastoDTO.getDescripcion());
        gasto.setFechaInicio(gastoDTO.getFechaInicio());
        gasto.setFechaFin(gastoDTO.getFechaFin());
        gasto.setActivo(gastoDTO.getActivo());
        
        return ResponseEntity.status(HttpStatus.CREATED).body(gastoMensualRepository.save(gasto));
    }

    // Actualizar un gasto mensual
    @PutMapping("/{id}")
    public ResponseEntity<GastoMensual> actualizarGastoMensual(
        @PathVariable Long id, 
        @Valid @RequestBody GastoMensualDTO gastoDTO) {
        return gastoMensualRepository.findById(id)
            .map(gasto -> {
                if (gastoDTO.getUsuarioId() != null) {
                    Usuario usuario = usuarioRepository.findById(gastoDTO.getUsuarioId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
                    gasto.setUsuario(usuario);
                }
                gasto.setMonto(gastoDTO.getMonto());
                gasto.setDescripcion(gastoDTO.getDescripcion());
                gasto.setFechaInicio(gastoDTO.getFechaInicio());
                gasto.setFechaFin(gastoDTO.getFechaFin());
                gasto.setActivo(gastoDTO.getActivo());
                
                return ResponseEntity.ok(gastoMensualRepository.save(gasto));
            })
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gasto mensual no encontrado"));
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