package com.hotel.service;

import com.hotel.dao.EstanciaDAO;
import com.hotel.dao.HabitacionDAO;
import com.hotel.model.Estancia;
import com.hotel.model.Habitacion;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class EstanciaService {

    private final EstanciaDAO estanciaDAO;
    private final HabitacionDAO habitacionDAO;

    public EstanciaService() {
        this.estanciaDAO = new EstanciaDAO();
        this.habitacionDAO = new HabitacionDAO();
    }

    public boolean fechasValidas(Date entrada, Date salida) {
        return salida.after(entrada);
    }

    public List<Habitacion> buscarHabitacionesDisponibles(String fechaEntrada, String fechaSalida) throws SQLException {
        return habitacionDAO.listarDisponibles(fechaEntrada, fechaSalida);
    }

    public int guardarEstancia(Estancia estancia, int huespedId) throws SQLException {
        return estanciaDAO.insertar(estancia, huespedId);
    }

    public boolean eliminarEstancia(int estanciaId) throws SQLException {
        return estanciaDAO.eliminarPorId(estanciaId);
    }

    public List<String[]> listarResumenEstancias() throws SQLException {
        return estanciaDAO.listarResumen();
    }

    
}
