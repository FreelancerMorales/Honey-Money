package com.honeymoney.Honey_Money.model.DTO;

import jakarta.validation.constraints.NotNull;

public class CategoriaMovimientoDTO {
    private String nombre;

    @NotNull(message = "El tipo de movimiento no puede ser nulo")
    private Long tipoMovimientoId;

    @NotNull(message = "El ID del usuario no puede ser nulo")
    private Long usuarioId;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getTipoMovimientoId() {
        return tipoMovimientoId;
    }

    public void setTipoMovimientoId(Long tipoMovimientoId) {
        this.tipoMovimientoId = tipoMovimientoId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
}
