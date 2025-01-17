// Usuario.java
package com.honeymoney.Honey_Money.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.honeymoney.Honey_Money.util.BCryptHasher.BCryptHasher;

@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "id")
@Entity
@Table(name = "usuario")
public class Usuario {
    @Version
    private Long version;
    
    @Column(name = "saldo_actual", nullable = false)
    private Double saldoActual = 0.0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El nombre no puede ser nulo")
    @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull(message = "El email no puede ser nulo")
    @Email(message = "El email debe tener un formato válido")
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @NotNull(message = "La contraseña no puede ser nula")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    @Column(name = "password", nullable = false)
    private String password;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // Serializar las categorías
    private List<CategoriaMovimiento> categorias;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<MovimientosFinancieros> movimientosFinancieros;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "usuario-movimientosDiarios") // Serializar movimientos diarios
    private List<MovimientoDiario> movimientosDiarios;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GastoMensual> gastosMensuales;

    public void setPassword(String rawPassword) {
        if (rawPassword != null && !rawPassword.startsWith("$2a$")) { // Evitar re-encriptar
            this.password = BCryptHasher.hashPassword(rawPassword);
        } else {
            this.password = rawPassword; // Asignar directamente si ya está encriptada
        }
    }

    public Long getVersion() {
        return version;
    }
    
    public void setVersion(Long version) {
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public Double getSaldoActual() {
        return saldoActual;
    }

    public void setSaldoActual(Double saldoActual) {
        this.saldoActual = saldoActual;
    }

    public List<CategoriaMovimiento> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<CategoriaMovimiento> categorias) {
        this.categorias = categorias;
    }

    public List<MovimientosFinancieros> getMovimientosFinancieros() {
        return movimientosFinancieros;
    }

    public void setMovimientosFinancieros(List<MovimientosFinancieros> movimientosFinancieros) {
        this.movimientosFinancieros = movimientosFinancieros;
    }

    public List<MovimientoDiario> getMovimientosDiarios() {
        return movimientosDiarios;
    }

    public void setMovimientosDiarios(List<MovimientoDiario> movimientosDiarios) {
        this.movimientosDiarios = movimientosDiarios;
    }

    public List<GastoMensual> getGastosMensuales() {
        return gastosMensuales;
    }

    public void setGastosMensuales(List<GastoMensual> gastosMensuales) {
        this.gastosMensuales = gastosMensuales;
    }
}