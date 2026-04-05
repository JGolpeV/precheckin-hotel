package com.hotel.model;

public class HabitacionItem {
    private int id;
    private String texto;
    private int capacidad;

    public HabitacionItem(int id, String texto, int capacidad) {
        this.id = id;
        this.texto = texto;
        this.capacidad = capacidad;
    }

    public int getId() {
        return id;
    }

    public int getCapacidad() {
        return capacidad;
    }

    @Override
    public String toString() {
        return texto;
    }
}
