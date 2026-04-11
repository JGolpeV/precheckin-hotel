package com.hotel.dao;

import com.hotel.database.ConexionSQLite;
import com.hotel.model.DocumentoIdentidad;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DocumentoIdentidadDAO {


    public void insertar(DocumentoIdentidad doc) throws SQLException {
        String sql = """
            INSERT INTO documento_identidad (huesped_id, tipo_documento, numero_documento, numero_soporte, fecha_caducidad)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, doc.getHuespedId());
            ps.setString(2, doc.getTipoDocumento());
            ps.setString(3, doc.getNumeroDocumento());
            ps.setString(4, doc.getSoporteDocumento());
            ps.setString(5, doc.getFechaCaducidad());

            ps.executeUpdate();
        }
    }

    public void actualizar(DocumentoIdentidad doc) throws SQLException {
        String sql = """
            UPDATE documento_identidad
            SET tipo_documento = ?, numero_documento = ?, numero_soporte = ?, fecha_caducidad = ?
            WHERE huesped_id = ?
        """;

        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, doc.getTipoDocumento());
            ps.setString(2, doc.getNumeroDocumento());
            ps.setString(3, doc.getSoporteDocumento());
            ps.setString(4, doc.getFechaCaducidad());
            ps.setInt(5, doc.getHuespedId());

            ps.executeUpdate();
        }
    }

    public void guardarOActualizar(DocumentoIdentidad doc) throws SQLException {
        DocumentoIdentidad existente = buscarPorHuespedId(doc.getHuespedId());

        if (existente == null) {
            insertar(doc);
        } else {
            actualizar(doc);
        }
    }

    public DocumentoIdentidad buscarPorHuespedId(int huespedId) throws SQLException {
        String sql = """
            SELECT id, huesped_id, tipo_documento, numero_documento, numero_soporte, fecha_caducidad
            FROM documento_identidad
            WHERE huesped_id = ?
        """;

        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, huespedId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    DocumentoIdentidad doc = new DocumentoIdentidad();
                    doc.setId(rs.getInt("id"));
                    doc.setHuespedId(rs.getInt("huesped_id"));
                    doc.setTipoDocumento(rs.getString("tipo_documento"));
                    doc.setNumeroDocumento(rs.getString("numero_documento"));
                    doc.setSoporteDocumento(rs.getString("numero_soporte"));
                    doc.setFechaCaducidad(rs.getString("fecha_caducidad"));
                    return doc;
                }
            }
        }

        return null;
    }
}