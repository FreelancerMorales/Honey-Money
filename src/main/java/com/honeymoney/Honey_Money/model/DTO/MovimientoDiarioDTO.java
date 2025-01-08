package com.honeymoney.Honey_Money.model.DTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;

public class MovimientoDiarioDTO {
    @NotNull(message = "La fecha no puede ser nula")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fecha;

    @DecimalMin(value = "0.0", inclusive = false, message = "El total debe ser mayor a 0")
    private double total;

    @NotEmpty(message = "Debe incluir al menos un movimiento financiero")
    private List<MovimientoFinancieroDTO> movimientosFinancieros = new ArrayList<>();

    public MovimientoDiarioDTO() {
    }

    public MovimientoDiarioDTO(LocalDate fecha, double total, List<MovimientoFinancieroDTO> movimientosFinancieros) {
        this.fecha = fecha;
        this.total = total;
        this.movimientosFinancieros = movimientosFinancieros != null ? 
            movimientosFinancieros : new ArrayList<>();
    }

    @Override
    public String toString() {
        return "MovimientoDiarioDTO{" +
            "fecha=" + fecha +
            ", total=" + total +
            ", movimientosFinancieros=" + movimientosFinancieros +
            '}';
    }

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