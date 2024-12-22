package com.honeymoney.Honey_Money.controller;

import com.honeymoney.Honey_Money.model.CategoriaMovimiento;
import com.honeymoney.Honey_Money.model.Usuario;
import com.honeymoney.Honey_Money.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Obtener todos los usuarios
    @GetMapping
    public List<Usuario> obtenerUsuarios() {
        return usuarioRepository.findAll();
    }

    // Obtener un usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUsuarioPorId(@PathVariable Long id) {
        return usuarioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Crear un nuevo usuario
    @PostMapping
    public Usuario crearUsuario(@RequestBody Usuario usuario) {
        // No encriptamos aquí; confiamos en que el modelo maneje la encriptación
        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        System.out.println("Contraseña ingresada por el cliente: " + usuario.getPassword());
        System.out.println("Contraseña encriptada almacenada: " + usuarioGuardado.getPassword());

        return usuarioGuardado;
    }

    // Actualizar un usuario
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario detallesUsuario) {
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    if (detallesUsuario.getName() != null) usuario.setName(detallesUsuario.getName());
                    if (detallesUsuario.getEmail() != null) usuario.setEmail(detallesUsuario.getEmail());
                    if (detallesUsuario.getPassword() != null) usuario.setPassword(detallesUsuario.getPassword());
                    if (detallesUsuario.getSaldoActual() != null) usuario.setSaldoActual(detallesUsuario.getSaldoActual());
                    return ResponseEntity.ok(usuarioRepository.save(usuario));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Eliminar un usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Método adicional: Obtener categorías de un usuario
    @GetMapping("/{id}/categorias")
    public ResponseEntity<List<CategoriaMovimiento>> obtenerCategoriasPorUsuario(@PathVariable Long id) {
        return usuarioRepository.findById(id)
                .map(usuario -> ResponseEntity.ok(usuario.getCategorias()))
                .orElse(ResponseEntity.notFound().build());
    }
}