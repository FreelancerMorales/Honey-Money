package com.honeymoney.Honey_Money.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "movimientos_financieros")
public class MovimientosFinancieros {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonBackReference
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private CategoriaMovimiento categoria;

    @ManyToOne
    @JoinColumn(name = "tipo_movimiento_id", nullable = false)
    private TipoMovimiento tipoMovimiento;

    @Column(name = "monto", nullable = false)
    private BigDecimal monto;

    @NotNull(message = "La descripción no puede ser nula")
    @Size(min = 3, max = 255, message = "La descripción debe tener entre 3 y 255 caracteres")
    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @ManyToOne
    @JoinColumn(name = "movimiento_diario_id") // Nombre de la columna en la base de datos
    private MovimientoDiario movimientoDiario;

    public MovimientosFinancieros() {
        // Default constructor
    }

    public MovimientosFinancieros(Usuario usuario, CategoriaMovimiento categoria, TipoMovimiento tipoMovimiento, BigDecimal monto, String descripcion, LocalDate fecha) {
        this.usuario = usuario;
        this.categoria = categoria;
        this.tipoMovimiento = tipoMovimiento;
        this.monto = monto;
        this.descripcion = descripcion;
        this.fecha = fecha;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public CategoriaMovimiento getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaMovimiento categoria) {
        this.categoria = categoria;
    }

    public TipoMovimiento getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(TipoMovimiento tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
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

    public MovimientoDiario getMovimientoDiario() {
        return movimientoDiario;
    }

    public void setMovimientoDiario(MovimientoDiario movimientoDiario) {
        this.movimientoDiario = movimientoDiario;
    }
}
