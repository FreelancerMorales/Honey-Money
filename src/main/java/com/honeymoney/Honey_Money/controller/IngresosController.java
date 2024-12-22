package com.honeymoney.Honey_Money.controller;

import java.util.List;

import org.springframework.http.HttpStatus;

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
import org.springframework.web.server.ResponseStatusException;

import com.honeymoney.Honey_Money.model.CategoriaMovimiento;
import com.honeymoney.Honey_Money.model.Ingresos;
import com.honeymoney.Honey_Money.model.Usuario;
import com.honeymoney.Honey_Money.model.DTO.IngresoDTO;
import com.honeymoney.Honey_Money.repository.CategoriaMovimientoRepository;
import com.honeymoney.Honey_Money.repository.IngresosRepository;
import com.honeymoney.Honey_Money.repository.UsuarioRepository;

@RestController
@RequestMapping("/api/ingresos")
public class IngresosController {

    @Autowired
    private IngresosRepository ingresosRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CategoriaMovimientoRepository categoriaMovimientoRepository;

    // Obtener todos los ingresos por usuario
    @GetMapping("/usuario/{usuarioId}")
    public List<Ingresos> obtenerIngresosPorUsuario(@PathVariable Long usuarioId) {
        return ingresosRepository.findByUsuarioId(usuarioId);
    }

    // Crear un nuevo ingreso
    @PostMapping
    public ResponseEntity<Ingresos> crearIngreso(@RequestBody IngresoDTO ingresoDTO) {
        // Buscar el usuario
        Usuario usuario = usuarioRepository.findById(ingresoDTO.getUsuarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        // Buscar la categoría
        CategoriaMovimiento categoria = categoriaMovimientoRepository.findById(ingresoDTO.getCategoriaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada"));

        // Construir el ingreso
        Ingresos ingreso = new Ingresos();
        ingreso.setMonto(ingresoDTO.getMonto());
        ingreso.setDescripcion(ingresoDTO.getDescripcion());
        ingreso.setFecha(ingresoDTO.getFecha());
        ingreso.setUsuario(usuario);
        ingreso.setCategoria(categoria);

        // Guardar el ingreso
        Ingresos ingresoGuardado = ingresosRepository.save(ingreso);
        return ResponseEntity.status(HttpStatus.CREATED).body(ingresoGuardado);
    }

    // Actualizar un ingreso
    @PutMapping("/{id}")
    public ResponseEntity<Ingresos> actualizarIngreso(@PathVariable Long id, @RequestBody Ingresos detallesIngreso) {
        return ingresosRepository.findById(id)
                .map(ingreso -> {
                    ingreso.setMonto(detallesIngreso.getMonto());
                    ingreso.setDescripcion(detallesIngreso.getDescripcion());
                    ingreso.setFecha(detallesIngreso.getFecha());
                    return ResponseEntity.ok(ingresosRepository.save(ingreso));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Eliminar un ingreso
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarIngreso(@PathVariable Long id) {
        if (ingresosRepository.existsById(id)) {
            ingresosRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}