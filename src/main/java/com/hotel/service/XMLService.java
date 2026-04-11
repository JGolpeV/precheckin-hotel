package com.hotel.service;

import com.hotel.dao.EstanciaDAO;
import com.hotel.dao.HabitacionDAO;
import com.hotel.dao.HuespedDAO;
import com.hotel.model.DocumentoIdentidad;
import com.hotel.model.Estancia;
import com.hotel.model.EstanciaExportacionXML;
import com.hotel.model.Habitacion;
import com.hotel.model.Huesped;
import com.hotel.model.HuespedConDocumento;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class XMLService {

    private final EstanciaDAO estanciaDAO;
    private final HabitacionDAO habitacionDAO;
    private final HuespedDAO huespedDAO;

    public XMLService() {
        this.estanciaDAO = new EstanciaDAO();
        this.habitacionDAO = new HabitacionDAO();
        this.huespedDAO = new HuespedDAO();
    }

    public List<EstanciaExportacionXML> prepararDatosPorFecha(String fechaEntrada) throws SQLException {
        List<Estancia> estancias = estanciaDAO.listarPorFechaEntrada(fechaEntrada);
        List<EstanciaExportacionXML> resultado = new ArrayList<>();

        for (Estancia e : estancias) {
            EstanciaExportacionXML item = new EstanciaExportacionXML();
            item.setEstancia(e);
            item.setHabitacion(habitacionDAO.buscarPorId(e.getHabitacionId()));
            item.setHuespedes(huespedDAO.listarPorEstancia(e.getId()));
            resultado.add(item);
        }

        return resultado;
    }

    public String generarXMLPVPorFecha(String fechaEntrada, String codigoEstablecimiento) throws SQLException {
        List<EstanciaExportacionXML> datos = prepararDatosPorFecha(fechaEntrada);

        StringBuilder sb = new StringBuilder();

        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<alt:peticion xmlns:alt=\"http://www.servicios.ertzaintza.eus/Ertzaintza/ALOJADOS/A19/comunicacionPV\">\n");
        sb.append("  <solicitud>\n");
        sb.append("    <codigoEstablecimiento>").append(escapeXml(codigoEstablecimiento)).append("</codigoEstablecimiento>\n");

        for (EstanciaExportacionXML item : datos) {
            Estancia estancia = item.getEstancia();
            Habitacion habitacion = item.getHabitacion();
            List<HuespedConDocumento> huespedes = item.getHuespedes();

            sb.append("    <comunicacion>\n");

            // CONTRATO
            sb.append("      <contrato>\n");
            sb.append("        <referencia>").append(generarReferenciaContrato(estancia)).append("</referencia>\n");
            sb.append("        <fechaContrato>").append(estancia.getFechaEntrada()).append("</fechaContrato>\n");
            sb.append("        <fechaEntrada>").append(formatearFechaHoraXML(estancia.getFechaEntrada())).append("</fechaEntrada>\n");
            sb.append("        <fechaSalida>").append(formatearFechaHoraXML(estancia.getFechaSalida())).append("</fechaSalida>\n");
            sb.append("        <numPersonas>").append(huespedes.size()).append("</numPersonas>\n");
            sb.append("        <numHabitaciones>1</numHabitaciones>\n");
            sb.append("        <internet>false</internet>\n");
            sb.append("        <pago>\n");
            sb.append("          <tipoPago>EFECT</tipoPago>\n");
            sb.append("        </pago>\n");
            sb.append("      </contrato>\n");

            // PERSONAS
            for (HuespedConDocumento hcd : huespedes) {
                Huesped h = hcd.getHuesped();
                DocumentoIdentidad doc = hcd.getDocumentoIdentidad();

                sb.append("      <persona>\n");
                sb.append("        <rol>").append(mapearRolXML(hcd.getRol())).append("</rol>\n");
                sb.append("        <nombre>").append(escapeXml(valor(h.getNombre()))).append("</nombre>\n");
                sb.append("        <apellido1>").append(escapeXml(valor(h.getApellido1()))).append("</apellido1>\n");

                if (!vacio(h.getApellido2())) {
                    sb.append("        <apellido2>").append(escapeXml(h.getApellido2())).append("</apellido2>\n");
                }

                if (doc != null && !vacio(doc.getTipoDocumento())) {
                    sb.append("        <tipoDocumento>").append(escapeXml(doc.getTipoDocumento())).append("</tipoDocumento>\n");
                }

                if (doc != null && !vacio(doc.getNumeroDocumento())) {
                    sb.append("        <numeroDocumento>").append(escapeXml(doc.getNumeroDocumento())).append("</numeroDocumento>\n");
                }

                if (doc != null && !vacio(doc.getSoporteDocumento())) {
                    sb.append("        <soporteDocumento>").append(escapeXml(doc.getSoporteDocumento())).append("</soporteDocumento>\n");
                }

                if (!vacio(h.getFechaNacimiento())) {
                    sb.append("        <fechaNacimiento>").append(escapeXml(h.getFechaNacimiento())).append("</fechaNacimiento>\n");
                }

                if (!vacio(h.getNacionalidad())) {
                    sb.append("        <nacionalidad>").append(escapeXml(h.getNacionalidad())).append("</nacionalidad>\n");
                }

                if (!vacio(h.getSexo())) {
                    sb.append("        <sexo>").append(escapeXml(h.getSexo())).append("</sexo>\n");
                }

                sb.append("        <direccion>\n");
                sb.append("          <direccion>").append(escapeXml(valor(h.getDireccion()))).append("</direccion>\n");


                if (!vacio(h.getMunicipio())) {
                    sb.append("          <nombreMunicipio>")
                            .append(escapeXml(h.getMunicipio()))
                            .append("</nombreMunicipio>\n");
                }

                sb.append("          <codigoPostal>").append(escapeXml(valor(h.getCodigoPostal()))).append("</codigoPostal>\n");
                sb.append("          <pais>").append(escapeXml(valor(h.getPaisResidencia()))).append("</pais>\n");
                sb.append("        </direccion>\n");

                if (!vacio(h.getTelefono())) {
                    sb.append("        <telefono>").append(escapeXml(h.getTelefono())).append("</telefono>\n");
                }

                if (!vacio(h.getEmail())) {
                    sb.append("        <correo>").append(escapeXml(h.getEmail())).append("</correo>\n");
                }

                if (!vacio(h.getParentesco())) {
                    sb.append("        <parentesco>").append(escapeXml(h.getParentesco())).append("</parentesco>\n");
                }

                sb.append("      </persona>\n");
            }

            sb.append("    </comunicacion>\n");
        }

        sb.append("  </solicitud>\n");
        sb.append("</alt:peticion>\n");

        return sb.toString();
    }

    public Path guardarXMLPVPorFecha(String fechaEntrada, String codigoEstablecimiento, Path rutaDestino) throws SQLException, IOException {
        String xml = generarXMLPVPorFecha(fechaEntrada, codigoEstablecimiento);
        Files.writeString(rutaDestino, xml, StandardCharsets.UTF_8);
        return rutaDestino;
    }

    private String generarReferenciaContrato(Estancia estancia) {
        return "RES-" + estancia.getId() + "-" + estancia.getFechaEntrada().replace("-", "");
    }

    private String formatearFechaHoraXML(String fecha) {
        // Para XML generamos medianoche
        return fecha + "T00:00:00";
    }

    private String mapearRolXML(String rolBD) {
        if ("TITULAR".equalsIgnoreCase(rolBD)) {
            return "TI";
        }
        return "VI";
    }

    private String escapeXml(String texto) {
        if (texto == null) {
            return "";
        }

        return texto
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }

    private boolean vacio(String valor) {
        return valor == null || valor.isBlank();
    }

    private String valor(String valor) {
        return valor == null ? "" : valor;
    }
}