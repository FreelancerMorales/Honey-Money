package com.honeymoney.Honey_Money.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.honeymoney.Honey_Money.model.MovimientoDiario;
import com.honeymoney.Honey_Money.model.MovimientosFinancieros;
import com.honeymoney.Honey_Money.model.DTO.MovimientoDiarioDTO;
import com.honeymoney.Honey_Money.repository.MovimientoDiarioRepository;
import com.honeymoney.Honey_Money.repository.TipoMovimientoRepository;
import com.honeymoney.Honey_Money.repository.UsuarioRepository;

import jakarta.validation.Valid;

import com.honeymoney.Honey_Money.repository.CategoriaMovimientoRepository;
import com.honeymoney.Honey_Money.model.Usuario;

@RestController
@RequestMapping("/api/movimientos-diarios")
public class MovimientoDiarioController {


    @Autowired
    private MovimientoDiarioRepository movimientoDiarioRepository;

    @Autowired
    private TipoMovimientoRepository tipoMovimientoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CategoriaMovimientoRepository categoriaRepository;

    // Obtener movimientos diarios por usuario y fecha
    @GetMapping("/usuario/{usuarioId}/fecha/{fecha}")
    public List<MovimientoDiario> obtenerMovimientosPorUsuarioYFecha(
            @PathVariable Long usuarioId, 
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return movimientoDiarioRepository.findByUsuarioIdAndFecha(usuarioId, fecha);
    }

    // Crear un nuevo movimiento diario
    @PostMapping(consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
    public ResponseEntity<MovimientoDiario> crearMovimientoDiario(@Valid @RequestBody MovimientoDiarioDTO movimientoDTO) {
    
        MovimientoDiario movimiento = new MovimientoDiario();
        movimiento.setFecha(movimientoDTO.getFecha());
        movimiento.setTotal(movimientoDTO.getTotal());
    
        // Validar y asociar usuario
        Long usuarioId = movimientoDTO.getMovimientosFinancieros().get(0).getUsuarioId();
        if (usuarioId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El ID del usuario no puede ser nulo");
        }
    
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        movimiento.setUsuario(usuario);
        
        movimiento.setTipoMovimiento(tipoMovimientoRepository.findById(movimientoDTO.getTipoMovimientoId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tipo de movimiento no encontrado")));
    
        // Validar y asociar movimientos financieros
        List<MovimientosFinancieros> movimientos = movimientoDTO.getMovimientosFinancieros().stream()
            .map(movimientoFinancieroDTO -> {
                MovimientosFinancieros movimientoFinanciero = new MovimientosFinancieros();
                movimientoFinanciero.setMonto(movimientoFinancieroDTO.getMonto());
                movimientoFinanciero.setDescripcion(movimientoFinancieroDTO.getDescripcion());
                movimientoFinanciero.setFecha(movimientoFinancieroDTO.getFecha());
                movimientoFinanciero.setUsuario(usuario);
                movimientoFinanciero.setCategoria(categoriaRepository.findById(movimientoFinancieroDTO.getCategoriaId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada")));
                movimientoFinanciero.setTipoMovimiento(tipoMovimientoRepository.findById(movimientoFinancieroDTO.getTipoMovimientoId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tipo de movimiento no encontrado")));
                movimientoFinanciero.setMovimientoDiario(movimiento);
                return movimientoFinanciero;
            })
            .collect(Collectors.toList());
        
        movimiento.setMovimientosFinancieros(movimientos);
        
        MovimientoDiario movimientoGuardado = movimientoDiarioRepository.save(movimiento);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(movimientoGuardado);
    }

    // Actualizar un movimiento diario
    @PutMapping("/{id}")
    public ResponseEntity<MovimientoDiario> actualizarMovimientoDiario(
            @PathVariable Long id, 
            @Valid @RequestBody MovimientoDiarioDTO detallesMovimiento) {
        return movimientoDiarioRepository.findById(id)
                .map(movimiento -> {
                    // Validar que el tipo de movimiento no sea nulo
                    if (detallesMovimiento.getTipoMovimientoId() == null) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                            "El tipo de movimiento no puede ser nulo");
                    }
            
                    // Actualizar campos básicos
                    movimiento.setFecha(detallesMovimiento.getFecha());
                    movimiento.setTotal(detallesMovimiento.getTotal());

                    // Actualizar tipo de movimiento
                    movimiento.setTipoMovimiento(tipoMovimientoRepository
                        .findById(detallesMovimiento.getTipoMovimientoId())
                        .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Tipo de movimiento no encontrado")));

                    // Actualizar movimientos financieros
                    if (detallesMovimiento.getMovimientosFinancieros() != null) {
                        // Limpiar movimientos existentes
                        movimiento.getMovimientosFinancieros().clear();

                        // Agregar nuevos movimientos
                        detallesMovimiento.getMovimientosFinancieros().forEach(dto -> {
                            if (dto.getTipoMovimientoId() == null || dto.getCategoriaId() == null 
                                || dto.getUsuarioId() == null) {
                                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                                    "Todos los IDs son requeridos para los movimientos financieros");
                            }

                            MovimientosFinancieros movFinanciero = new MovimientosFinancieros();
                            movFinanciero.setMonto(dto.getMonto());
                            movFinanciero.setDescripcion(dto.getDescripcion());
                            movFinanciero.setFecha(dto.getFecha());
                            movFinanciero.setMovimientoDiario(movimiento);

                            // Establecer tipo de movimiento
                            movFinanciero.setTipoMovimiento(tipoMovimientoRepository
                                .findById(dto.getTipoMovimientoId())
                                .orElseThrow(() -> new ResponseStatusException(
                                    HttpStatus.NOT_FOUND, "Tipo de movimiento no encontrado")));

                            // Establecer categoría
                            movFinanciero.setCategoria(categoriaRepository
                                .findById(dto.getCategoriaId())
                                .orElseThrow(() -> new ResponseStatusException(
                                    HttpStatus.NOT_FOUND, "Categoría no encontrada")));

                            // Establecer usuario
                            movFinanciero.setUsuario(usuarioRepository
                                .findById(dto.getUsuarioId())
                                .orElseThrow(() -> new ResponseStatusException(
                                    HttpStatus.NOT_FOUND, "Usuario no encontrado")));

                            movimiento.getMovimientosFinancieros().add(movFinanciero);
                        });
                    }

                    return ResponseEntity.ok(movimientoDiarioRepository.save(movimiento));
                })
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Movimiento diario no encontrado"));
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