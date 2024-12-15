package com.honeymoney.Honey_Money.controller;

import com.honeymoney.Honey_Money.model.Transaccion;
import com.honeymoney.Honey_Money.repository.TransaccionRepository;
import com.honeymoney.Honey_Money.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transacciones")
public class TransaccionController {

    @Autowired
    private TransaccionRepository transaccionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Obtener todas las transacciones
    @GetMapping
    public List<Transaccion> obtenerTransacciones() {
        return transaccionRepository.findAll();
    }

    // Obtener transacciones por usuario
    @GetMapping("/usuario/{usuarioId}")
    public List<Transaccion> obtenerTransaccionesPorUsuario(@PathVariable Long usuarioId) {
        return transaccionRepository.findAll()
                .stream()
                .filter(transaccion -> transaccion.getUsuario().getId().equals(usuarioId))
                .toList();
    }

    // Crear una nueva transacción
    @PostMapping
    public ResponseEntity<Transaccion> crearTransaccion(@RequestBody Transaccion transaccion) {
        return usuarioRepository.findById(transaccion.getUsuario().getId())
                .map(usuario -> {
                    transaccion.setUsuario(usuario);
                    return ResponseEntity.ok(transaccionRepository.save(transaccion));
                })
                .orElse(ResponseEntity.badRequest().build());
    }

    // Actualizar una transacción
    @PutMapping("/{id}")
    public ResponseEntity<Transaccion> actualizarTransaccion(@PathVariable Long id, @RequestBody Transaccion detallesTransaccion) {
        return transaccionRepository.findById(id)
                .map(transaccion -> {
                    transaccion.setMonto(detallesTransaccion.getMonto());
                    transaccion.setDescripcion(detallesTransaccion.getDescripcion());
                    transaccion.setFecha(detallesTransaccion.getFecha());
                    transaccion.setTipo(detallesTransaccion.getTipo());
                    return ResponseEntity.ok(transaccionRepository.save(transaccion));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Eliminar una transacción
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTransaccion(@PathVariable Long id) {
        if (transaccionRepository.existsById(id)) {
            transaccionRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}