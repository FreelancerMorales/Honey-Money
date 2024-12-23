package com.honeymoney.Honey_Money.model.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MovimientoFinancieroDTO {
    private BigDecimal monto;
    private String descripcion;
    private LocalDate fecha;
    private Long categoriaId; // Opcional
    private Long tipoMovimientoId;
    private Long usuarioId;
    
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
