package com.hotel.dao;

import com.hotel.database.ConexionSQLite;
import com.hotel.model.Huesped;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HuespedDAO {
    public int insertar(Huesped h) throws SQLException {
            String sql = """
        INSERT INTO huesped
        (nombre, apellido1, apellido2, direccion, codigo_postal, poblacion, municipio, comunidad_autonoma,
         provincia, pais_residencia, parentesco, nacionalidad, fecha_nacimiento, lugar_nacimiento, telefono, email, sexo)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    """;

        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, h.getNombre());
            ps.setString(2, h.getApellido1());
            ps.setString(3, h.getApellido2());
            ps.setString(4, h.getDireccion());
            ps.setString(5, h.getCodigoPostal());
            ps.setString(6, h.getPoblacion());
            ps.setString(7, h.getMunicipio());
            ps.setString(8, h.getComunidadAutonoma());
            ps.setString(9, h.getProvincia());
            ps.setString(10, h.getPaisResidencia());
            ps.setString(11, h.getParentesco());
            ps.setString(12, h.getNacionalidad());
            ps.setString(13, h.getFechaNacimiento());
            ps.setString(14, h.getLugarNacimiento());
            ps.setString(15, h.getTelefono());
            ps.setString(16, h.getEmail());
            ps.setString(17, h.getSexo());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        }
        return -1;
    }

    public List<Huesped> listarTodos() throws SQLException {
        String sql = """
            SELECT id, nombre, apellido1, apellido2, direccion, codigo_postal, poblacion, municipio, comunidad_autonoma,
                   provincia, pais_residencia, parentesco, nacionalidad, fecha_nacimiento, lugar_nacimiento, telefono, email, sexo
            FROM huesped
            ORDER BY id DESC
        """;

        List<Huesped> lista = new ArrayList<>();

        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Huesped h = new Huesped();
                h.setId(rs.getInt("id"));
                h.setNombre(rs.getString("nombre"));
                h.setApellido1(rs.getString("apellido1"));
                h.setApellido2(rs.getString("apellido2"));
                h.setDireccion(rs.getString("direccion"));
                h.setCodigoPostal(rs.getString("codigo_postal"));
                h.setPoblacion(rs.getString("poblacion"));
                h.setMunicipio(rs.getString("municipio"));
                h.setComunidadAutonoma(rs.getString("comunidad_autonoma"));
                h.setProvincia(rs.getString("provincia"));
                h.setPaisResidencia(rs.getString("pais_residencia"));
                h.setParentesco(rs.getString("parentesco"));
                h.setNacionalidad(rs.getString("nacionalidad"));
                h.setFechaNacimiento(rs.getString("fecha_nacimiento"));
                h.setLugarNacimiento(rs.getString("lugar_nacimiento"));
                h.setTelefono(rs.getString("telefono"));
                h.setEmail(rs.getString("email"));
                h.setSexo(rs.getString("sexo"));

                lista.add(h);
            }
        }

        return lista;
    }

    public boolean eliminarPorId(int id) throws SQLException {
        String sql = "DELETE FROM huesped WHERE id = ?";

        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            return filas > 0;
        }
    }

    public boolean actualizar(Huesped h) throws SQLException {
        String sql = """
        UPDATE huesped
        SET nombre = ?, apellido1 = ?, apellido2 = ?, direccion = ?, codigo_postal = ?,
            pais_residencia = ?, nacionalidad = ?, fecha_nacimiento = ?, lugar_nacimiento = ?,
            telefono = ?, email = ?, sexo = ?
        WHERE id = ?
    """;

        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, h.getNombre());
            ps.setString(2, h.getApellido1());
            ps.setString(3, h.getApellido2());
            ps.setString(4, h.getDireccion());
            ps.setString(5, h.getCodigoPostal());
            ps.setString(6, h.getPaisResidencia());
            ps.setString(7, h.getNacionalidad());
            ps.setString(8, h.getFechaNacimiento());
            ps.setString(9, h.getLugarNacimiento());
            ps.setString(10, h.getTelefono());
            ps.setString(11, h.getEmail());
            ps.setInt(12, h.getId());
            ps.setString(13, h.getSexo());

            return ps.executeUpdate() > 0;
        }
    }

    public List<Huesped> listarParaCombo() throws SQLException {
        String sql = """
            SELECT id, nombre, apellido1, apellido2
            FROM huesped
            ORDER BY apellido1, apellido2, nombre
            """;

        List<Huesped> lista = new ArrayList<>();

        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Huesped h = new Huesped();
                h.setId(rs.getInt("id"));
                h.setNombre(rs.getString("nombre"));
                h.setApellido1(rs.getString("apellido1"));
                h.setApellido2(rs.getString("apellido2"));
                lista.add(h);
            }
        }

        return lista;
    }

    public Huesped buscarPorId(int id) throws SQLException {
        String sql = """
        SELECT id, nombre, apellido1, apellido2, direccion, codigo_postal, pais_residencia,
               nacionalidad, fecha_nacimiento, lugar_nacimiento, telefono, email, sexo
        FROM huesped
        WHERE id = ?
    """;

        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Huesped h = new Huesped();
                    h.setId(rs.getInt("id"));
                    h.setNombre(rs.getString("nombre"));
                    h.setApellido1(rs.getString("apellido1"));
                    h.setApellido2(rs.getString("apellido2"));
                    h.setDireccion(rs.getString("direccion"));
                    h.setCodigoPostal(rs.getString("codigo_postal"));
                    h.setPaisResidencia(rs.getString("pais_residencia"));
                    h.setNacionalidad(rs.getString("nacionalidad"));
                    h.setFechaNacimiento(rs.getString("fecha_nacimiento"));
                    h.setLugarNacimiento(rs.getString("lugar_nacimiento"));
                    h.setTelefono(rs.getString("telefono"));
                    h.setEmail(rs.getString("email"));
                    h.setSexo(rs.getString("sexo"));
                    return h;
                }
            }
        }

        return null;
    }
}

