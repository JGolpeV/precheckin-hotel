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

    public int insertarConHuespedes(Estancia estancia, int huespedTitularId, java.util.List<Integer> acompanantesIds) throws SQLException {
        String sqlEstancia = """
            INSERT INTO estancia (fecha_entrada, fecha_salida, habitacion_id, estado, observaciones)
            VALUES (?, ?, ?, ?, ?)
            """;

        String sqlRelacion = """
            INSERT INTO estancia_huesped (estancia_id, huesped_id, rol)
            VALUES (?, ?, ?)
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

            try (PreparedStatement psRelacion = conn.prepareStatement(sqlRelacion)) {

                // Titular
                psRelacion.setInt(1, estanciaId);
                psRelacion.setInt(2, huespedTitularId);
                psRelacion.setString(3, "TITULAR");
                psRelacion.executeUpdate();

                // Acompañantes
                if (acompanantesIds != null) {
                    for (Integer acompananteId : acompanantesIds) {
                        psRelacion.setInt(1, estanciaId);
                        psRelacion.setInt(2, acompananteId);
                        psRelacion.setString(3, "ACOMPAÑANTE");
                        psRelacion.executeUpdate();
                    }
                }
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

    public com.hotel.model.EstanciaDetalle obtenerDetallePorId(int estanciaId) throws SQLException {
        String sqlEstancia = """
            SELECT id, fecha_entrada, fecha_salida, habitacion_id, estado, observaciones
            FROM estancia
            WHERE id = ?
            """;

        String sqlHuespedes = """
            SELECT h.id, h.nombre, h.apellido1, h.apellido2, h.nacionalidad, eh.rol
            FROM estancia_huesped eh
            JOIN huesped h ON h.id = eh.huesped_id
            WHERE eh.estancia_id = ?
            ORDER BY eh.rol DESC, h.apellido1, h.apellido2, h.nombre
            """;

        com.hotel.model.EstanciaDetalle detalle = new com.hotel.model.EstanciaDetalle();

        try (Connection conn = ConexionSQLite.conectar()) {

            try (PreparedStatement ps = conn.prepareStatement(sqlEstancia)) {
                ps.setInt(1, estanciaId);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        com.hotel.model.Estancia estancia = new com.hotel.model.Estancia();
                        estancia.setId(rs.getInt("id"));
                        estancia.setFechaEntrada(rs.getString("fecha_entrada"));
                        estancia.setFechaSalida(rs.getString("fecha_salida"));
                        estancia.setHabitacionId(rs.getInt("habitacion_id"));
                        estancia.setEstado(rs.getString("estado"));
                        estancia.setObservaciones(rs.getString("observaciones"));
                        detalle.setEstancia(estancia);
                    } else {
                        return null;
                    }
                }
            }

            java.util.List<com.hotel.model.Huesped> acompanantes = new java.util.ArrayList<>();

            try (PreparedStatement ps = conn.prepareStatement(sqlHuespedes)) {
                ps.setInt(1, estanciaId);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        com.hotel.model.Huesped h = new com.hotel.model.Huesped();
                        h.setId(rs.getInt("id"));
                        h.setNombre(rs.getString("nombre"));
                        h.setApellido1(rs.getString("apellido1"));
                        h.setApellido2(rs.getString("apellido2"));
                        h.setNacionalidad(rs.getString("nacionalidad"));

                        String rol = rs.getString("rol");
                        if ("TITULAR".equals(rol)) {
                            detalle.setTitular(h);
                        } else {
                            acompanantes.add(h);
                        }
                    }
                }
            }

            detalle.setAcompanantes(acompanantes);
        }

        return detalle;
    }

    public boolean actualizarConHuespedes(com.hotel.model.Estancia estancia,
                                          int huespedTitularId,
                                          java.util.List<Integer> acompanantesIds) throws SQLException {

        String sqlBuscarHabitacionAnterior = """
            SELECT habitacion_id
            FROM estancia
            WHERE id = ?
            """;

        String sqlActualizarEstancia = """
            UPDATE estancia
            SET fecha_entrada = ?, fecha_salida = ?, habitacion_id = ?, estado = ?, observaciones = ?
            WHERE id = ?
            """;

        String sqlBorrarRelaciones = """
            DELETE FROM estancia_huesped
            WHERE estancia_id = ?
            """;

        String sqlInsertarRelacion = """
            INSERT INTO estancia_huesped (estancia_id, huesped_id, rol)
            VALUES (?, ?, ?)
            """;

        String sqlLiberarHabitacionAnterior = """
            UPDATE habitacion
            SET estado = 'LIBRE'
            WHERE id = ?
            """;

        String sqlOcuparHabitacionNueva = """
            UPDATE habitacion
            SET estado = 'OCUPADA'
            WHERE id = ?
            """;

        Connection conn = null;

        try {
            conn = ConexionSQLite.conectar();
            conn.setAutoCommit(false);

            Integer habitacionAnteriorId = null;

            try (PreparedStatement ps = conn.prepareStatement(sqlBuscarHabitacionAnterior)) {
                ps.setInt(1, estancia.getId());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        habitacionAnteriorId = rs.getInt("habitacion_id");
                    }
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(sqlActualizarEstancia)) {
                ps.setString(1, estancia.getFechaEntrada());
                ps.setString(2, estancia.getFechaSalida());
                ps.setInt(3, estancia.getHabitacionId());
                ps.setString(4, estancia.getEstado());
                ps.setString(5, estancia.getObservaciones());
                ps.setInt(6, estancia.getId());

                int filas = ps.executeUpdate();
                if (filas == 0) {
                    conn.rollback();
                    return false;
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(sqlBorrarRelaciones)) {
                ps.setInt(1, estancia.getId());
                ps.executeUpdate();
            }

            try (PreparedStatement ps = conn.prepareStatement(sqlInsertarRelacion)) {
                // Titular
                ps.setInt(1, estancia.getId());
                ps.setInt(2, huespedTitularId);
                ps.setString(3, "TITULAR");
                ps.executeUpdate();

                // Acompañantes
                if (acompanantesIds != null) {
                    for (Integer acompananteId : acompanantesIds) {
                        ps.setInt(1, estancia.getId());
                        ps.setInt(2, acompananteId);
                        ps.setString(3, "ACOMPAÑANTE");
                        ps.executeUpdate();
                    }
                }
            }

            if (habitacionAnteriorId != null && !habitacionAnteriorId.equals(estancia.getHabitacionId())) {
                try (PreparedStatement ps = conn.prepareStatement(sqlLiberarHabitacionAnterior)) {
                    ps.setInt(1, habitacionAnteriorId);
                    ps.executeUpdate();
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(sqlOcuparHabitacionNueva)) {
                ps.setInt(1, estancia.getHabitacionId());
                ps.executeUpdate();
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

    public java.util.List<Estancia> listarPorFechaEntrada(String fechaEntrada) throws SQLException {
        String sql = """
            SELECT id, fecha_entrada, fecha_salida, habitacion_id, estado, observaciones
            FROM estancia
            WHERE fecha_entrada = ?
            ORDER BY id
            """;

        java.util.List<Estancia> lista = new java.util.ArrayList<>();

        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, fechaEntrada);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Estancia e = new Estancia();
                    e.setId(rs.getInt("id"));
                    e.setFechaEntrada(rs.getString("fecha_entrada"));
                    e.setFechaSalida(rs.getString("fecha_salida"));
                    e.setHabitacionId(rs.getInt("habitacion_id"));
                    e.setEstado(rs.getString("estado"));
                    e.setObservaciones(rs.getString("observaciones"));
                    lista.add(e);
                }
            }
        }

        return lista;
    }

}
