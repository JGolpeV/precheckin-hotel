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
        poblacion = ?, municipio = ?, comunidad_autonoma = ?, provincia = ?,
        pais_residencia = ?, parentesco = ?, nacionalidad = ?, fecha_nacimiento = ?,
        lugar_nacimiento = ?, telefono = ?, email = ?, sexo = ?
    WHERE id = ?
    """;

        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

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
            ps.setInt(18, h.getId());

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

    public java.util.List<com.hotel.model.HuespedConDocumento> listarPorEstancia(int estanciaId) throws SQLException {
        String sql = """
            SELECT h.id, h.nombre, h.apellido1, h.apellido2, h.nacionalidad,
                   h.fecha_nacimiento, h.lugar_nacimiento, h.direccion, h.codigo_postal,
                   h.pais_residencia, h.telefono, h.email, h.sexo,
                   eh.rol
            FROM estancia_huesped eh
            JOIN huesped h ON h.id = eh.huesped_id
            WHERE eh.estancia_id = ?
            ORDER BY eh.rol DESC, h.apellido1, h.apellido2, h.nombre
            """;

        java.util.List<com.hotel.model.HuespedConDocumento> lista = new java.util.ArrayList<>();
        DocumentoIdentidadDAO documentoDAO = new DocumentoIdentidadDAO();

        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, estanciaId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Huesped h = new Huesped();
                    h.setId(rs.getInt("id"));
                    h.setNombre(rs.getString("nombre"));
                    h.setApellido1(rs.getString("apellido1"));
                    h.setApellido2(rs.getString("apellido2"));
                    h.setNacionalidad(rs.getString("nacionalidad"));
                    h.setFechaNacimiento(rs.getString("fecha_nacimiento"));
                    h.setLugarNacimiento(rs.getString("lugar_nacimiento"));
                    h.setDireccion(rs.getString("direccion"));
                    h.setCodigoPostal(rs.getString("codigo_postal"));
                    h.setPaisResidencia(rs.getString("pais_residencia"));
                    h.setTelefono(rs.getString("telefono"));
                    h.setEmail(rs.getString("email"));
                    h.setSexo(rs.getString("sexo"));

                    com.hotel.model.HuespedConDocumento item = new com.hotel.model.HuespedConDocumento();
                    item.setHuesped(h);
                    item.setRol(rs.getString("rol"));
                    item.setDocumentoIdentidad(documentoDAO.buscarPorHuespedId(h.getId()));

                    lista.add(item);
                }
            }
        }

        return lista;
    }
}

