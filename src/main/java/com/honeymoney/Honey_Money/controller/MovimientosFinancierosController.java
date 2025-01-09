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

import com.honeymoney.Honey_Money.model.CategoriaMovimiento;
import com.honeymoney.Honey_Money.model.MovimientosFinancieros;
import com.honeymoney.Honey_Money.model.TipoMovimiento;
import com.honeymoney.Honey_Money.model.Usuario;
import com.honeymoney.Honey_Money.model.DTO.MovimientoFinancieroDTO;
import com.honeymoney.Honey_Money.repository.CategoriaMovimientoRepository;
import com.honeymoney.Honey_Money.repository.MovimientosFinancierosRepository;
import com.honeymoney.Honey_Money.repository.TipoMovimientoRepository;
import com.honeymoney.Honey_Money.repository.UsuarioRepository;
import com.honeymoney.Honey_Money.service.MovimientoFinancieroService;

@RestController
@RequestMapping("/api/movimientos")
public class MovimientosFinancierosController {

    @Autowired
    private MovimientosFinancierosRepository movimientosFinancierosRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CategoriaMovimientoRepository categoriaMovimientoRepository;

    @Autowired
    private TipoMovimientoRepository tipoMovimientoRepository;

    @Autowired
    private MovimientoFinancieroService movimientoFinancieroService;

    // Obtener todos los movimientos financieros por usuario
    @GetMapping("/usuario/{usuarioId}")
    public List<MovimientosFinancieros> obtenerMovimientosPorUsuario(@PathVariable Long usuarioId) {
        return movimientosFinancierosRepository.findByUsuarioId(usuarioId);
    }

    // Crear un nuevo movimiento financiero
    @PostMapping
    public ResponseEntity<MovimientosFinancieros> crearMovimiento(@RequestBody MovimientoFinancieroDTO movimientoDTO) {
        // Buscar el usuario
        Usuario usuario = usuarioRepository.findById(movimientoDTO.getUsuarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        // Buscar la categoría (opcional)
        CategoriaMovimiento categoria = null;
        if (movimientoDTO.getCategoriaId() != null) {
            categoria = categoriaMovimientoRepository.findById(movimientoDTO.getCategoriaId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada"));
        }

        // Buscar el tipo de movimiento
        TipoMovimiento tipoMovimiento = tipoMovimientoRepository.findById(movimientoDTO.getTipoMovimientoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tipo de movimiento no encontrado"));

        // Crear el movimiento financiero
        MovimientosFinancieros movimiento = new MovimientosFinancieros();
        movimiento.setMonto(movimientoDTO.getMonto());
        movimiento.setDescripcion(movimientoDTO.getDescripcion());
        movimiento.setFecha(movimientoDTO.getFecha());
        movimiento.setUsuario(usuario);
        movimiento.setCategoria(categoria);
        movimiento.setTipoMovimiento(tipoMovimiento);

        // Guardar el movimiento
        MovimientosFinancieros movimientoGuardado = movimientoFinancieroService.saveMovimiento(movimiento);
        return ResponseEntity.status(HttpStatus.CREATED).body(movimientoGuardado);
    }

    // Actualizar un movimiento financiero
    @PutMapping("/{id}")
    public ResponseEntity<MovimientosFinancieros> actualizarMovimiento(@PathVariable Long id, @RequestBody MovimientoFinancieroDTO movimientoDTO) {
        return movimientosFinancierosRepository.findById(id)
                .map(movimiento -> {
                    movimiento.setMonto(movimientoDTO.getMonto());
                    movimiento.setDescripcion(movimientoDTO.getDescripcion());
                    movimiento.setFecha(movimientoDTO.getFecha());

                    // Actualizar categoría si existe
                    if (movimientoDTO.getCategoriaId() != null) {
                        CategoriaMovimiento categoria = categoriaMovimientoRepository.findById(movimientoDTO.getCategoriaId())
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada"));
                        movimiento.setCategoria(categoria);
                    }

                    // Actualizar tipo de movimiento si existe
                    if (movimientoDTO.getTipoMovimientoId() != null) {
                        TipoMovimiento tipoMovimiento = tipoMovimientoRepository.findById(movimientoDTO.getTipoMovimientoId())
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tipo de movimiento no encontrado"));
                        movimiento.setTipoMovimiento(tipoMovimiento);
                    }

                    return ResponseEntity.ok(movimientoFinancieroService.saveMovimiento(movimiento));
                })
                .orElse(ResponseEntity.notFound().build());
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