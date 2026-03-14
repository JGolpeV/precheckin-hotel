package com.hotel.model;

public class HabitacionItem {
    private int id;
    private String texto;

    public HabitacionItem(int id, String texto) {
        this.id = id;
        this.texto = texto;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return texto;
    }
}
