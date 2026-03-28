package com.hotel.service;

import com.hotel.dao.HuespedDAO;
import com.hotel.model.Huesped;

import java.sql.SQLException;
import java.util.List;

public class HuespedService {

    private final HuespedDAO huespedDAO;

    public HuespedService() {
        this.huespedDAO = new HuespedDAO();
    }

    public boolean nombreValido(Huesped huesped) {
        return huesped.getNombre() != null && !huesped.getNombre().trim().isEmpty();
    }

    public boolean apellido1Valido(Huesped huesped) {
        return huesped.getApellido1() != null && !huesped.getApellido1().trim().isEmpty();
    }

    public String validarHuesped(Huesped huesped) {
        if (!nombreValido(huesped)) {
            return "El nombre es obligatorio.";
        }

        if (!apellido1Valido(huesped)) {
            return "El primer apellido es obligatorio.";
        }

        return null;
    }

    public int guardarHuesped(Huesped huesped) throws SQLException {
        return huespedDAO.insertar(huesped);
    }

    public boolean actualizarHuesped(Huesped huesped) throws SQLException {
        return huespedDAO.actualizar(huesped);
    }

    public boolean eliminarHuesped(int id) throws SQLException {
        return huespedDAO.eliminarPorId(id);
    }

    public List<Huesped> listarTodos() throws SQLException {
        return huespedDAO.listarTodos();
    }

    public List<Huesped> listarParaCombo() throws SQLException {
        return huespedDAO.listarParaCombo();
    }
}

