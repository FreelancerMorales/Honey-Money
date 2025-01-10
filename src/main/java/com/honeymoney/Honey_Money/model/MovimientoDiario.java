package com.honeymoney.Honey_Money.model;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Column(nullable = false)
    private BigDecimal totalIngresos = BigDecimal.ZERO;
    
    @Column(nullable = false)
    private BigDecimal totalGastos = BigDecimal.ZERO;
    
    @Column(nullable = false)
    private BigDecimal saldoFinal = BigDecimal.ZERO;
    
    @Column(nullable = false)
    private boolean cerrado = false;

    @Column(name = "ultima_actualizacion")
    private LocalDateTime ultimaActualizacion;

    @Column(name = "cierre_temporal")
    private boolean cierreTemporal = false;

    @Column(name = "cierre_definitivo")
    private boolean cierreDefinitivo = false;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonBackReference(value = "usuario-movimientosDiarios")
    private Usuario usuario;

    @OneToMany(mappedBy = "movimientoDiario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "diario-movimientos")
    private List<MovimientosFinancieros> movimientosFinancieros = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tipo_movimiento_id", nullable = false)  // Restaurar nullable = false
    private TipoMovimiento tipoMovimiento;

    // Constructor por defecto con inicialización segura
    public MovimientoDiario() {
        this.totalIngresos = BigDecimal.ZERO;
        this.totalGastos = BigDecimal.ZERO;
        this.saldoFinal = BigDecimal.ZERO;
        this.cierreTemporal = true;
        this.cierreDefinitivo = false;
        this.ultimaActualizacion = LocalDateTime.now();
        this.movimientosFinancieros = new ArrayList<>();
        // No inicializar tipoMovimiento aquí, se hará en el servicio
    }

    @PrePersist
    @PreUpdate
    public void validarDatos() {
        // Validar datos básicos
        if (fecha == null) {
            throw new IllegalStateException("La fecha es requerida");
        }
        if (usuario == null) {
            throw new IllegalStateException("El usuario es requerido");
        }

        // Validar totales
        MovimientoDiarioValidator validator = new MovimientoDiarioValidator();
        if (!validator.validarTotales(this)) {
            throw new IllegalStateException("El total no coincide con la suma de movimientos");
        }
        validator.actualizarTotal(this);
        
        // Validar saldo
        validarSaldoSuficiente();
    }

    private void validarSaldoSuficiente() {
        if (tipoMovimiento == null) {
            return; // Skip validation if no tipoMovimiento
        }
        
        try {
            if (tipoMovimiento.getId() == 2L && total > usuario.getSaldoActual()) {
                throw new IllegalStateException("Saldo insuficiente para realizar la operación");
            }
        } catch (NullPointerException e) {
            // Log error and continue
            System.err.println("Error validando saldo: " + e.getMessage());
        }
    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public BigDecimal getTotalIngresos() {
        return totalIngresos;
    }

    public void setTotalIngresos(BigDecimal totalIngresos) {
        this.totalIngresos = totalIngresos;
    }

    public BigDecimal getTotalGastos() {
        return totalGastos;
    }

    public void setTotalGastos(BigDecimal totalGastos) {
        this.totalGastos = totalGastos;
    }

    public BigDecimal getSaldoFinal() {
        return saldoFinal;
    }

    public void setSaldoFinal(BigDecimal saldoFinal) {
        this.saldoFinal = saldoFinal;
    }

    public boolean isCerrado() {
        return cerrado;
    }

    public void setCerrado(boolean cerrado) {
        this.cerrado = cerrado;
    }

    public boolean isCierreTemporal() {
        return cierreTemporal;
    }

    public void setCierreTemporal(boolean cierreTemporal) {
        this.cierreTemporal = cierreTemporal;
    }

    public boolean isCierreDefinitivo() {
        return cierreDefinitivo;
    }

    public void setCierreDefinitivo(boolean cierreDefinitivo) {
        this.cierreDefinitivo = cierreDefinitivo;
    }

    public LocalDateTime getUltimaActualizacion() {
        return ultimaActualizacion;
    }

    public void setUltimaActualizacion(LocalDateTime ultimaActualizacion) {
        this.ultimaActualizacion = ultimaActualizacion;
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
        if (tipoMovimiento != null && tipoMovimiento.getId() == null) {
            throw new IllegalArgumentException("TipoMovimiento debe tener un ID válido");
        }
        this.tipoMovimiento = tipoMovimiento;
    }
}