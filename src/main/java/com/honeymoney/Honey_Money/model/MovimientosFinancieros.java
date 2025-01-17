package com.honeymoney.Honey_Money.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "movimientos_financieros")
public class MovimientosFinancieros {
    
    @Version
    private Long version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private CategoriaMovimiento categoria;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tipo_movimiento_id", nullable = false)
    private TipoMovimiento tipoMovimiento;

    @Transient
    private Long tipoMovimientoId;

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
    @JsonBackReference(value = "diario-movimientos")
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
        return this.tipoMovimiento;
    }

    public void setTipoMovimiento(TipoMovimiento tipoMovimiento) {
        if (tipoMovimiento == null) {
            throw new IllegalArgumentException("TipoMovimiento cannot be null");
        }
        // Ensure we have a valid ID
        if (tipoMovimiento.getId() == null) {
            throw new IllegalArgumentException("TipoMovimiento must have an ID");
        }
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
