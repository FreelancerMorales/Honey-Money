package com.honeymoney.Honey_Money.model.DTO;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class MovimientoDiarioDTO {
    @NotNull(message = "La fecha no puede ser nula")
    private LocalDate fecha;

    @DecimalMin(value = "0.0", inclusive = false, message = "El total debe ser mayor a 0")
    private double total;

    @NotEmpty(message = "Debe incluir al menos un movimiento financiero")
    private List<MovimientoFinancieroDTO> movimientosFinancieros;

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<MovimientoFinancieroDTO> getMovimientosFinancieros() {
        return movimientosFinancieros;
    }

    public void setMovimientosFinancieros(List<MovimientoFinancieroDTO> movimientosFinancieros) {
        this.movimientosFinancieros = movimientosFinancieros;
    }
}