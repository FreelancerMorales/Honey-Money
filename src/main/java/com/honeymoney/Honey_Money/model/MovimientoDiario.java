package com.honeymoney.Honey_Money.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.honeymoney.Honey_Money.validator.MovimientoDiarioValidator;

import jakarta.validation.constraints.NotNull;
import jakarta.persistence.*;

@Entity
@Table(name = "movimiento_diario")
public class MovimientoDiario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @NotNull
    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fecha;

    @Column(nullable = false)
    private double total;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonBackReference(value = "usuario-movimientosDiarios")
    private Usuario usuario;

    @OneToMany(mappedBy = "movimientoDiario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "diario-movimientos")
    private List<MovimientosFinancieros> movimientosFinancieros = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "tipo_movimiento_id", nullable = false)
    private TipoMovimiento tipoMovimiento;

    @PrePersist
    @PreUpdate
    public void validarTotales() {
        MovimientoDiarioValidator validator = new MovimientoDiarioValidator();
        if (!validator.validarTotales(this)) {
            throw new IllegalStateException("El total no coincide con la suma de movimientos");
        }
        validator.actualizarTotal(this);
        validarSaldoSuficiente();
    }

    private void validarSaldoSuficiente() {
        if (tipoMovimiento.getId() == 2L && total > usuario.getSaldoActual()) {
            throw new IllegalStateException("Saldo insuficiente para realizar la operación");
        }
    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<MovimientosFinancieros> getMovimientosFinancieros() {
        return movimientosFinancieros;
    }

    public void setMovimientosFinancieros(List<MovimientosFinancieros> movimientosFinancieros) {
        this.movimientosFinancieros = movimientosFinancieros;
    }

    public TipoMovimiento getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(TipoMovimiento tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }
}