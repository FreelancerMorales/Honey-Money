package com.honeymoney.Honey_Money.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tipo_movimiento")
public class TipoMovimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    // Agregar constructor de copia
    public TipoMovimiento copy() {
        TipoMovimiento copy = new TipoMovimiento();
        copy.setId(this.id);
        copy.setNombre(this.nombre);
        return copy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        System.out.println("ID original: " + id);
        if (id == null) {
            System.out.println("ID nulo");
            System.out.println("ID original: " + id);
        }
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
