package com.hotel.service;

import com.hotel.dao.HabitacionDAO;
import com.hotel.model.Habitacion;

import java.sql.SQLException;
import java.util.List;

public class HabitacionService {

    private final HabitacionDAO habitacionDAO;

    public HabitacionService() {
        this.habitacionDAO = new HabitacionDAO();
    }

    public void aplicarCapacidadSegunTipo(Habitacion habitacion) {
        if (habitacion.getTipo() == null) {
            return;
        }

        switch (habitacion.getTipo()) {
            case "Individual":
                habitacion.setCapacidad(1);
                break;
            case "Doble":
                habitacion.setCapacidad(2);
                break;
            case "Triple":
                habitacion.setCapacidad(3);
                break;
        }
    }

    public int guardarHabitacion(Habitacion habitacion) throws SQLException {
        aplicarCapacidadSegunTipo(habitacion);
        return habitacionDAO.insertar(habitacion);
    }

    public boolean actualizarHabitacion(Habitacion habitacion) throws SQLException {
        aplicarCapacidadSegunTipo(habitacion);
        return habitacionDAO.actualizar(habitacion);
    }

    public boolean eliminarHabitacion(int id) throws SQLException {
        return habitacionDAO.eliminarPorId(id);
    }

    public List<Habitacion> listarTodas() throws SQLException {
        return habitacionDAO.listarTodas();
    }

}
