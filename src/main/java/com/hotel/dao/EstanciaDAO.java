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

    public java.util.List<String[]> listarResumen() throws SQLException {
        String sql = """
        SELECT e.id,
               h.nombre || ' ' || h.apellido1 || COALESCE(' ' || h.apellido2, '') AS huesped,
               hab.numero AS habitacion,
               e.fecha_entrada,
               e.fecha_salida,
               e.estado
        FROM estancia e
        JOIN estancia_huesped eh ON eh.estancia_id = e.id AND eh.rol = 'TITULAR'
        JOIN huesped h ON h.id = eh.huesped_id
        JOIN habitacion hab ON hab.id = e.habitacion_id
        ORDER BY e.fecha_entrada DESC, e.id DESC
    """;

        java.util.List<String[]> lista = new java.util.ArrayList<>();

        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new String[]{
                        String.valueOf(rs.getInt("id")),
                        rs.getString("huesped"),
                        rs.getString("habitacion"),
                        rs.getString("fecha_entrada"),
                        rs.getString("fecha_salida"),
                        rs.getString("estado")
                });
            }
        }

        return lista;
    }

    public boolean eliminarPorId(int estanciaId) throws SQLException {
        String sqlBuscarHabitacion = """
            SELECT habitacion_id
            FROM estancia
            WHERE id = ?
            """;

        String sqlEliminarEstancia = """
            DELETE FROM estancia
            WHERE id = ?
            """;

        String sqlLiberarHabitacion = """
            UPDATE habitacion
            SET estado = 'LIBRE'
            WHERE id = ?
            """;

        Connection conn = null;

        try {
            conn = ConexionSQLite.conectar();
            conn.setAutoCommit(false);

            Integer habitacionId = null;

            try (PreparedStatement psBuscar = conn.prepareStatement(sqlBuscarHabitacion)) {
                psBuscar.setInt(1, estanciaId);
                try (ResultSet rs = psBuscar.executeQuery()) {
                    if (rs.next()) {
                        habitacionId = rs.getInt("habitacion_id");
                    } else {
                        conn.rollback();
                        return false;
                    }
                }
            }

            try (PreparedStatement psEliminar = conn.prepareStatement(sqlEliminarEstancia)) {
                psEliminar.setInt(1, estanciaId);
                int filas = psEliminar.executeUpdate();

                if (filas == 0) {
                    conn.rollback();
                    return false;
                }
            }

            if (habitacionId != null) {
                try (PreparedStatement psLiberar = conn.prepareStatement(sqlLiberarHabitacion)) {
                    psLiberar.setInt(1, habitacionId);
                    psLiberar.executeUpdate();
                }
            }

            conn.commit();
            return true;

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
