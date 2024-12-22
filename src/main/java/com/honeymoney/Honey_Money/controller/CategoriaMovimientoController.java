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

import com.honeymoney.Honey_Money.model.CategoriaMovimiento;
import com.honeymoney.Honey_Money.model.MovimientoDiario;
import com.honeymoney.Honey_Money.model.Usuario;
import com.honeymoney.Honey_Money.model.DTO.CategoriaMovimientoDTO;
import com.honeymoney.Honey_Money.repository.CategoriaMovimientoRepository;
import com.honeymoney.Honey_Money.repository.UsuarioRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaMovimientoController {

    @Autowired
    private CategoriaMovimientoRepository categoriaMovimientoRepository;

    @Autowired
    private UsuarioRepository UsuarioRepository;

    @GetMapping
    public List<CategoriaMovimiento> obtenerTodasCategorias() {
        return categoriaMovimientoRepository.findAll();
    }

    // Obtener todas las categorías por usuario
    @GetMapping("/usuario/{usuarioId}")
    public List<CategoriaMovimiento> obtenerCategoriasPorUsuario(@PathVariable Long usuarioId) {
        return categoriaMovimientoRepository.findByUsuarioId(usuarioId);
    }

    @PostMapping
    public ResponseEntity<CategoriaMovimiento> crearCategoria(@Valid @RequestBody CategoriaMovimientoDTO categoriaDTO) {

        // Validar que usuarioId no sea nulo
        if (categoriaDTO.getUsuarioId() == null) {
            throw new IllegalArgumentException("El ID del usuario no puede ser nulo");
        }

        // Buscar el usuario asociado
        Usuario usuario = UsuarioRepository.findById(categoriaDTO.getUsuarioId())
        .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // Crear y guardar la nueva categoría
        CategoriaMovimiento categoria = new CategoriaMovimiento();
        categoria.setNombre(categoriaDTO.getNombre());
        try {
        categoria.setTipo(MovimientoDiario.TipoMovimiento.valueOf(categoriaDTO.getTipo().toUpperCase()));
        } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException("El tipo de movimiento debe ser 'Ingreso' o 'Egreso'");
        }
        categoria.setUsuario(usuario);

        return ResponseEntity.ok(categoriaMovimientoRepository.save(categoria));
    }

    // Actualizar una categoría
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaMovimiento> actualizarCategoria(@PathVariable Long id, @RequestBody CategoriaMovimiento detallesCategoria) {
        return categoriaMovimientoRepository.findById(id)
                .map(categoria -> {
                    categoria.setNombre(detallesCategoria.getNombre());
                    categoria.setTipo(detallesCategoria.getTipo());
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
