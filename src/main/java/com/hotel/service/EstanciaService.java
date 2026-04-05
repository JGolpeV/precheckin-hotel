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

    public int guardarEstanciaConHuespedes(Estancia estancia, int huespedTitularId, java.util.List<Integer> acompanantesIds) throws SQLException {
        return estanciaDAO.insertarConHuespedes(estancia, huespedTitularId, acompanantesIds);
    }

    public com.hotel.model.EstanciaDetalle obtenerDetalleEstancia(int estanciaId) throws SQLException {
        return estanciaDAO.obtenerDetallePorId(estanciaId);
    }

    public boolean actualizarEstanciaConHuespedes(com.hotel.model.Estancia estancia,
                                                  int huespedTitularId,
                                                  java.util.List<Integer> acompanantesIds) throws SQLException {
        return estanciaDAO.actualizarConHuespedes(estancia, huespedTitularId, acompanantesIds);
    }

    public List<Habitacion> buscarHabitacionesDisponiblesExcluyendoEstancia(String fechaEntrada, String fechaSalida, int estanciaIdExcluir) throws SQLException {
        return habitacionDAO.listarDisponiblesExcluyendoEstancia(fechaEntrada, fechaSalida, estanciaIdExcluir);
    }
}
