package com.hotel.dao;

import com.hotel.database.ConexionSQLite;
import com.hotel.model.Estancia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class EstanciaDAO {

    public int insertar(Estancia estancia, int huespedId) throws SQLException {
        String sqlEstancia = """
                INSERT INTO estancia (fecha_entrada, fecha_salida, habitacion_id, estado, observaciones)
                VALUES (?, ?, ?, ?, ?)
                """;

        String sqlEstanciaHuesped = """
                INSERT INTO estancia_huesped (estancia_id, huesped_id, rol)
                VALUES (?, ?, 'TITULAR')
                """;

        String sqlActualizarHabitacion = """
                UPDATE habitacion
                SET estado = 'OCUPADA'
                WHERE id = ?
                """;

        Connection conn = null;

        try {
            conn = ConexionSQLite.conectar();
            conn.setAutoCommit(false);

            int estanciaId;

            try (PreparedStatement psEstancia = conn.prepareStatement(sqlEstancia, Statement.RETURN_GENERATED_KEYS)) {
                psEstancia.setString(1, estancia.getFechaEntrada());
                psEstancia.setString(2, estancia.getFechaSalida());
                psEstancia.setInt(3, estancia.getHabitacionId());
                psEstancia.setString(4, estancia.getEstado());
                psEstancia.setString(5, estancia.getObservaciones());

                psEstancia.executeUpdate();

                try (ResultSet rs = psEstancia.getGeneratedKeys()) {
                    if (rs.next()) {
                        estanciaId = rs.getInt(1);
                    } else {
                        throw new SQLException("No se pudo obtener el ID de la estancia.");
                    }
                }
            }

            try (PreparedStatement psRelacion = conn.prepareStatement(sqlEstanciaHuesped)) {
                psRelacion.setInt(1, estanciaId);
                psRelacion.setInt(2, huespedId);
                psRelacion.executeUpdate();
            }

            try (PreparedStatement psHabitacion = conn.prepareStatement(sqlActualizarHabitacion)) {
                psHabitacion.setInt(1, estancia.getHabitacionId());
                psHabitacion.executeUpdate();
            }

            conn.commit();
            return estanciaId;

        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }
}
