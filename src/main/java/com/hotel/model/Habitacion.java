package com.hotel.model;

public class Habitacion {
    private Integer id;
    private String numero;
    private String tipo;
    private Integer capacidad;
    private String estado; // LIBRE u OCUPADA

    public Habitacion() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Habitacion{id=" + id + ", numero='" + numero + "', tipo='" + tipo + "', capacidad=" + capacidad + ", estado='" + estado + "'}";
    }
}