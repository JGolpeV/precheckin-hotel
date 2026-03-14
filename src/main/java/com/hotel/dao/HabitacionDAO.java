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

    public List<Habitacion> listarLibres() throws SQLException {
        String sql = """
            SELECT id, numero, tipo, capacidad, estado
            FROM habitacion
            WHERE estado = 'LIBRE'
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
}
