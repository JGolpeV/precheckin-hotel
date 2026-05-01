package com.hotel.dao;

import com.hotel.database.ConexionSQLite;
import com.hotel.model.Habitacion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HabitacionDAO {
    public int insertar(Habitacion h) throws SQLException {
        String sql = """
            INSERT INTO habitacion (numero, tipo, capacidad, estado)
            VALUES (?, ?, ?, ?)
        """;

        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, h.getNumero());
            ps.setString(2, h.getTipo());
            ps.setInt(3, h.getCapacidad() != null ? h.getCapacidad() : 1);
            ps.setString(4, h.getEstado() != null ? h.getEstado() : "LIBRE");

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        }
        return -1;
    }

    public boolean actualizar(Habitacion h) throws SQLException {
        String sql = """
            UPDATE habitacion
            SET numero = ?, tipo = ?, capacidad = ?, estado = ?
            WHERE id = ?
        """;

        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, h.getNumero());
            ps.setString(2, h.getTipo());
            ps.setInt(3, h.getCapacidad() != null ? h.getCapacidad() : 1);
            ps.setString(4, h.getEstado());
            ps.setInt(5, h.getId());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminarPorId(int id) throws SQLException {
        String sql = "DELETE FROM habitacion WHERE id = ?";

        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public List<Habitacion> listarTodas() throws SQLException {
        String sql = """
            SELECT id, numero, tipo, capacidad, estado
            FROM habitacion
            ORDER BY numero
        """;

        List<Habitacion> lista = new ArrayList<>();

        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Habitacion h = new Habitacion();
                h.setId(rs.getInt("id"));
                h.setNumero(rs.getString("numero"));
                h.setTipo(rs.getString("tipo"));
                h.setCapacidad(rs.getInt("capacidad"));
                h.setEstado(rs.getString("estado"));
                lista.add(h);
            }
        }
        return lista;
    }

    public List<Habitacion> listarDisponibles(String fechaEntrada, String fechaSalida) throws SQLException {
        String sql = """
            SELECT h.id, h.numero, h.tipo, h.capacidad, h.estado
            FROM habitacion h
            WHERE h.id NOT IN (
                SELECT e.habitacion_id
                FROM estancia e
                WHERE e.fecha_entrada < ?
                  AND e.fecha_salida > ?
            )
            ORDER BY h.numero
            """;

        List<Habitacion> lista = new ArrayList<>();

        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, fechaSalida);
            ps.setString(2, fechaEntrada);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Habitacion h = new Habitacion();
                    h.setId(rs.getInt("id"));
                    h.setNumero(rs.getString("numero"));
                    h.setTipo(rs.getString("tipo"));
                    h.setCapacidad(rs.getInt("capacidad"));
                    h.setEstado(rs.getString("estado"));
                    lista.add(h);
                }
            }
        }

        return lista;
    }

    // Permite que la habitación actual siga apareciendo como disponible al editar una estancia
    public List<Habitacion> listarDisponiblesExcluyendoEstancia(String fechaEntrada, String fechaSalida, int estanciaIdExcluir) throws SQLException {
        String sql = """
            SELECT h.id, h.numero, h.tipo, h.capacidad, h.estado
            FROM habitacion h
            WHERE h.id NOT IN (
                SELECT e.habitacion_id
                FROM estancia e
                WHERE e.id <> ?
                  AND e.fecha_entrada < ?
                  AND e.fecha_salida > ?
            )
            ORDER BY h.numero
            """;

        List<Habitacion> lista = new ArrayList<>();

        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, estanciaIdExcluir);
            ps.setString(2, fechaSalida);
            ps.setString(3, fechaEntrada);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Habitacion h = new Habitacion();
                    h.setId(rs.getInt("id"));
                    h.setNumero(rs.getString("numero"));
                    h.setTipo(rs.getString("tipo"));
                    h.setCapacidad(rs.getInt("capacidad"));
                    h.setEstado(rs.getString("estado"));
                    lista.add(h);
                }
            }
        }

        return lista;
    }

    public Habitacion buscarPorId(int id) throws SQLException {
        String sql = """
            SELECT id, numero, tipo, capacidad, estado
            FROM habitacion
            WHERE id = ?
            """;

        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Habitacion h = new Habitacion();
                    h.setId(rs.getInt("id"));
                    h.setNumero(rs.getString("numero"));
                    h.setTipo(rs.getString("tipo"));
                    h.setCapacidad(rs.getInt("capacidad"));
                    h.setEstado(rs.getString("estado"));
                    return h;
                }
            }
        }

        return null;
    }

}
