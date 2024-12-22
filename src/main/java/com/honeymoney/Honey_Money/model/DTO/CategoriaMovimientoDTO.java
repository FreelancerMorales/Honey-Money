package com.honeymoney.Honey_Money.model.DTO;

import jakarta.validation.constraints.NotNull;

public class CategoriaMovimientoDTO {
    private String nombre;
    
    private String tipo;

    @NotNull(message = "El ID del usuario no puede ser nulo")
    private Long usuarioId;
    
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public Long getUsuarioId() {
        return usuarioId;
    }
    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
}