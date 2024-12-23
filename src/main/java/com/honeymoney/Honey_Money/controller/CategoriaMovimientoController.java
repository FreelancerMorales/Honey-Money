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
import com.honeymoney.Honey_Money.model.TipoMovimiento;
import com.honeymoney.Honey_Money.model.Usuario;
import com.honeymoney.Honey_Money.model.DTO.CategoriaMovimientoDTO;
import com.honeymoney.Honey_Money.repository.CategoriaMovimientoRepository;
import com.honeymoney.Honey_Money.repository.TipoMovimientoRepository;
import com.honeymoney.Honey_Money.repository.UsuarioRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaMovimientoController {

    @Autowired
    private CategoriaMovimientoRepository categoriaMovimientoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TipoMovimientoRepository tipoMovimientoRepository;

    // Obtener todas las categorías
    @GetMapping
    public List<CategoriaMovimiento> obtenerTodasCategorias() {
        return categoriaMovimientoRepository.findAll();
    }

    // Obtener todas las categorías por usuario
    @GetMapping("/usuario/{usuarioId}")
    public List<CategoriaMovimiento> obtenerCategoriasPorUsuario(@PathVariable Long usuarioId) {
        return categoriaMovimientoRepository.findByUsuarioId(usuarioId);
    }

    // Crear una nueva categoría
    @PostMapping
    public ResponseEntity<CategoriaMovimiento> crearCategoria(@Valid @RequestBody CategoriaMovimientoDTO categoriaDTO) {
        // Validar usuario
        Usuario usuario = usuarioRepository.findById(categoriaDTO.getUsuarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        // Validar tipo de movimiento
        TipoMovimiento tipoMovimiento = tipoMovimientoRepository.findById(categoriaDTO.getTipoMovimientoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tipo de movimiento no encontrado"));

        // Crear y guardar la categoría
        CategoriaMovimiento categoria = new CategoriaMovimiento();
        categoria.setNombre(categoriaDTO.getNombre());
        categoria.setTipoMovimiento(tipoMovimiento);
        categoria.setUsuario(usuario);

        CategoriaMovimiento categoriaGuardada = categoriaMovimientoRepository.save(categoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaGuardada);
    }

    // Actualizar una categoría
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaMovimiento> actualizarCategoria(
            @PathVariable Long id, 
            @RequestBody CategoriaMovimientoDTO detallesCategoria) {
        return categoriaMovimientoRepository.findById(id)
                .map(categoria -> {
                    categoria.setNombre(detallesCategoria.getNombre());

                    if (detallesCategoria.getTipoMovimientoId() != null) {
                        TipoMovimiento tipoMovimiento = tipoMovimientoRepository.findById(detallesCategoria.getTipoMovimientoId())
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tipo de movimiento no encontrado"));
                        categoria.setTipoMovimiento(tipoMovimiento);
                    }

                    return ResponseEntity.ok(categoriaMovimientoRepository.save(categoria));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Eliminar una categoría
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Long id) {
        if (categoriaMovimientoRepository.existsById(id)) {
            categoriaMovimientoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}