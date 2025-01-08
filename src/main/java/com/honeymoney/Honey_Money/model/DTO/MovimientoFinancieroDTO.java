package com.honeymoney.Honey_Money.model.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class MovimientoFinancieroDTO {

    private Long id;

    @NotNull(message = "El monto no puede ser nulo")
    @DecimalMin(value = "0.01", inclusive = true, message = "El monto debe ser mayor a 0")
    private BigDecimal monto;

    @NotBlank(message = "La descripción no puede estar vacía")
    @Size(min = 3, max = 255, message = "La descripción debe tener entre 3 y 255 caracteres")
    private String descripcion;

    @NotNull(message = "La fecha no puede ser nula")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fecha;

    @NotNull(message = "El ID de la categoría no puede ser nulo")
    private Long categoriaId;

    @NotNull(message = "El ID del tipo de movimiento no puede ser nulo")
    private Long tipoMovimientoId;

    @NotNull(message = "El ID del usuario no puede ser nulo")
    private Long usuarioId;

    public MovimientoFinancieroDTO() {
    }

    public MovimientoFinancieroDTO(Long id, BigDecimal monto, String descripcion, 
    LocalDate fecha, Long categoriaId, Long tipoMovimientoId, Long usuarioId) {
        this.id = id;
        this.monto = monto;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.categoriaId = categoriaId;
        this.tipoMovimientoId = tipoMovimientoId;
        this.usuarioId = usuarioId;
    }
    
    @Override
    public String toString() {
    return "MovimientoFinancieroDTO{" +
        "id=" + id +
        ", monto=" + monto +
        ", descripcion='" + descripcion + '\'' +
        ", fecha=" + fecha +
        ", categoriaId=" + categoriaId +
        ", tipoMovimientoId=" + tipoMovimientoId +
        ", usuarioId=" + usuarioId +
        '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
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
