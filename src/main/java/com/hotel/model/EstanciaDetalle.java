package com.hotel.model;

import java.util.ArrayList;
import java.util.List;

public class EstanciaDetalle {
    private Estancia estancia;
    private Huesped titular;
    private List<Huesped> acompanantes = new ArrayList<>();

    public Estancia getEstancia() {
        return estancia;
    }

    public void setEstancia(Estancia estancia) {
        this.estancia = estancia;
    }

    public Huesped getTitular() {
        return titular;
    }

    public void setTitular(Huesped titular) {
        this.titular = titular;
    }

    public List<Huesped> getAcompanantes() {
        return acompanantes;
    }

    public void setAcompanantes(List<Huesped> acompanantes) {
        this.acompanantes = acompanantes != null ? acompanantes : new ArrayList<>();
    }
}
