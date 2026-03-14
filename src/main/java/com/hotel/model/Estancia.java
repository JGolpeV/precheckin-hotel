package com.hotel.model;

public class Estancia {
    private Integer id;
    private String fechaEntrada;
    private String fechaSalida;
    private Integer habitacionId;
    private String estado;
    private String observaciones;

    public Estancia() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(String fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public String getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(String fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public Integer getHabitacionId() {
        return habitacionId;
    }

    public void setHabitacionId(Integer habitacionId) {
        this.habitacionId = habitacionId;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
