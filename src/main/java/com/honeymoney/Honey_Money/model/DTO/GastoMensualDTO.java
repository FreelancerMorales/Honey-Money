package com.honeymoney.Honey_Money.model.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.validation.constraints.*;

public class GastoMensualDTO {
    private Long id;

    @NotNull(message = "El ID del usuario no puede ser nulo")
    private Long usuarioId;

    @NotNull(message = "El monto no puede ser nulo")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    private BigDecimal monto;

    @NotNull(message = "La descripción no puede ser nula")
    @Size(min = 3, max = 255, message = "La descripción debe tener entre 3 y 255 caracteres")
    private String descripcion;

    @NotNull(message = "La fecha de inicio no puede ser nula")
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha fin no puede ser nula")
    private LocalDate fechaFin;

    @NotNull(message = "El estado activo no puede ser nulo")
    private Boolean activo;

    public GastoMensualDTO(Long id, Long usuarioId, BigDecimal monto, String descripcion, LocalDate fechaInicio, LocalDate fechaFin, Boolean activo) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.monto = monto;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "GastoMensualDTO{" +
        "id=" + id +
        ", usuarioId=" + usuarioId +
        ", monto=" + monto +
        ", descripcion='" + descripcion + '\'' +
        ", fechaInicio=" + fechaInicio +
        ", fechaFin=" + fechaFin +
        ", activo=" + activo +
        '}';
    }

    public GastoMensualDTO() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}