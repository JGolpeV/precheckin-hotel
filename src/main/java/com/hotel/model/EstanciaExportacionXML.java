package com.hotel.model;

import java.util.ArrayList;
import java.util.List;

public class EstanciaExportacionXML {
    private Estancia estancia;
    private Habitacion habitacion;
    private List<HuespedConDocumento> huespedes = new ArrayList<>();

    public Estancia getEstancia() {
        return estancia;
    }

    public void setEstancia(Estancia estancia) {
        this.estancia = estancia;
    }

    public Habitacion getHabitacion() {
        return habitacion;
    }

    public void setHabitacion(Habitacion habitacion) {
        this.habitacion = habitacion;
    }

    public List<HuespedConDocumento> getHuespedes() {
        return huespedes;
    }

    public void setHuespedes(List<HuespedConDocumento> huespedes) {
        this.huespedes = huespedes;
    }
}