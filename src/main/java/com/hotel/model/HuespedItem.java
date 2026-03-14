package com.hotel.model;

public class HuespedItem {
    private int id;
    private String nombreCompleto;

    public HuespedItem(int id, String nombreCompleto) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return nombreCompleto;
    }
}