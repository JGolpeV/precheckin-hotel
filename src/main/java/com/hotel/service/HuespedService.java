package com.hotel.service;

import com.hotel.dao.HuespedDAO;
import com.hotel.model.Huesped;
import com.hotel.dao.DocumentoIdentidadDAO;
import com.hotel.model.DocumentoIdentidad;

import java.sql.SQLException;
import java.util.List;

public class HuespedService {

    private final HuespedDAO huespedDAO;
    private final DocumentoIdentidadDAO documentoDAO;

    public HuespedService() {
        this.huespedDAO = new HuespedDAO();
        this.documentoDAO = new DocumentoIdentidadDAO();
    }

    public boolean nombreValido(Huesped huesped) {
        return huesped.getNombre() != null && !huesped.getNombre().trim().isEmpty();
    }

    public boolean apellido1Valido(Huesped huesped) {
        return huesped.getApellido1() != null && !huesped.getApellido1().trim().isEmpty();
    }

    public String validarHuesped(Huesped huesped) {
        if (!nombreValido(huesped)) {
            return "El nombre es obligatorio.";
        }

        if (!apellido1Valido(huesped)) {
            return "El primer apellido es obligatorio.";
        }

        return null;
    }

    public int guardarHuesped(Huesped huesped) throws SQLException {
        return huespedDAO.insertar(huesped);
    }

    public boolean actualizarHuesped(Huesped huesped) throws SQLException {
        return huespedDAO.actualizar(huesped);
    }

    public boolean eliminarHuesped(int id) throws SQLException {
        return huespedDAO.eliminarPorId(id);
    }

    public List<Huesped> listarTodos() throws SQLException {
        return huespedDAO.listarTodos();
    }

    public Huesped buscarPorId(int id) throws SQLException {
        return huespedDAO.buscarPorId(id);
    }

    public int guardarHuespedConDocumento(Huesped huesped, DocumentoIdentidad documento) throws SQLException {
        int huespedId = huespedDAO.insertar(huesped);

        if (documento != null) {
            documento.setHuespedId(huespedId);
            documentoDAO.guardarOActualizar(documento);
        }

        return huespedId;
    }

    public boolean actualizarHuespedConDocumento(Huesped huesped, DocumentoIdentidad documento) throws SQLException {
        boolean ok = huespedDAO.actualizar(huesped);

        if (ok && documento != null) {
            documento.setHuespedId(huesped.getId());
            documentoDAO.guardarOActualizar(documento);
        }

        return ok;
    }

    public DocumentoIdentidad buscarDocumentoPorHuespedId(int huespedId) throws SQLException {
        return documentoDAO.buscarPorHuespedId(huespedId);
    }

    public String validarDocumento(Huesped huesped, DocumentoIdentidad doc) {

        String tipo = doc.getTipoDocumento() != null ? doc.getTipoDocumento().trim() : "";
        String numero = doc.getNumeroDocumento() != null ? doc.getNumeroDocumento().trim() : "";
        String soporte = doc.getSoporteDocumento() != null ? doc.getSoporteDocumento().trim() : "";

        // Regla 1 y 2
        if (!tipo.isEmpty() && numero.isEmpty()) {
            return "Debes introducir el número de documento.";
        }

        if (tipo.isEmpty() && !numero.isEmpty()) {
            return "Debes seleccionar el tipo de documento.";
        }

        // Regla 3
        if (!numero.isEmpty() && soporte.isEmpty()) {
            return "Debes introducir el número de soporte.";
        }

        // Regla 4
        if (esAdulto(huesped) && numero.isEmpty()) {
            return "Los huéspedes adultos deben tener documento de identidad.";
        }

        return null;
    }

    private boolean esAdulto(Huesped h) {
        if (h.getFechaNacimiento() == null || h.getFechaNacimiento().isBlank()) {
            return false;
        }

        try {
            java.time.LocalDate nacimiento = java.time.LocalDate.parse(h.getFechaNacimiento());
            java.time.LocalDate hoy = java.time.LocalDate.now();

            return java.time.Period.between(nacimiento, hoy).getYears() >= 18;

        } catch (Exception e) {
            return false;
        }
    }

    public List<Huesped> listarParaCombo() throws SQLException {
        return huespedDAO.listarParaCombo();
    }
}

