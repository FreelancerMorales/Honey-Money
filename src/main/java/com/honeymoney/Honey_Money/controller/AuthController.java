package com.honeymoney.Honey_Money.controller;

import com.honeymoney.Honey_Money.model.Usuario;
import com.honeymoney.Honey_Money.repository.UsuarioRepository;
import com.honeymoney.Honey_Money.util.JwtUtil;
import com.honeymoney.Honey_Money.util.BCryptHasher.BCryptHasher;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final UsuarioRepository usuarioRepository;

    public AuthController(JwtUtil jwtUtil, UsuarioRepository usuarioRepository) {
        this.jwtUtil = jwtUtil;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
        // Buscar usuario por email
        Optional<Usuario> optionalUsuario = usuarioRepository.findByEmail(email);

        if (optionalUsuario.isEmpty()) {
            return ResponseEntity.status(401).body("Usuario no encontrado");
        }

        Usuario usuario = optionalUsuario.get();

        // Verificar la contraseña
        if (!BCryptHasher.checkPassword(password, usuario.getPassword())) {
            return ResponseEntity.status(401).body("Contraseña incorrecta");
        }
        
        // Generar token
        String token = jwtUtil.generateToken(usuario.getEmail());

        return ResponseEntity.ok().body(token);
    }
}