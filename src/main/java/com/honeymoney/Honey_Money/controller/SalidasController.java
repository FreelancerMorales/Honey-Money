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

import com.honeymoney.Honey_Money.model.Salidas;
import com.honeymoney.Honey_Money.repository.SalidasRepository;

@RestController
@RequestMapping("/api/salidas")
public class SalidasController {

    @Autowired
    private SalidasRepository salidasRepository;

    // Obtener todas las salidas por usuario
    @GetMapping("/usuario/{usuarioId}")
    public List<Salidas> obtenerSalidasPorUsuario(@PathVariable Long usuarioId) {
        return salidasRepository.findByUsuarioId(usuarioId);
    }

    // Crear una nueva salida
    @PostMapping
    public Salidas crearSalida(@RequestBody Salidas salida) {
        return salidasRepository.save(salida);
    }

    // Actualizar una salida
    @PutMapping("/{id}")
    public ResponseEntity<Salidas> actualizarSalida(@PathVariable Long id, @RequestBody Salidas detallesSalida) {
        return salidasRepository.findById(id)
                .map(salida -> {
                    salida.setMonto(detallesSalida.getMonto());
                    salida.setDescripcion(detallesSalida.getDescripcion());
                    salida.setFecha(detallesSalida.getFecha());
                    return ResponseEntity.ok(salidasRepository.save(salida));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Eliminar una salida
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarSalida(@PathVariable Long id) {
        if (salidasRepository.existsById(id)) {
            salidasRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}