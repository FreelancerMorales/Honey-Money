package com.honeymoney.Honey_Money.repository;

import com.honeymoney.Honey_Money.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}