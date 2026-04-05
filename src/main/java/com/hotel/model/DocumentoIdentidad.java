package com.hotel.model;

public class DocumentoIdentidad {
    private Integer id;
    private Integer huespedId;
    private String tipoDocumento;
    private String numeroDocumento;
    private String soporteDocumento;
    private String fechaCaducidad;

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public Integer getHuespedId() { return huespedId; }

    public void setHuespedId(Integer huespedId) { this.huespedId = huespedId; }

    public String getTipoDocumento() { return tipoDocumento; }

    public void setTipoDocumento(String tipoDocumento) { this.tipoDocumento = tipoDocumento; }

    public String getNumeroDocumento() { return numeroDocumento; }

    public void setNumeroDocumento(String numeroDocumento) { this.numeroDocumento = numeroDocumento; }

    public String getSoporteDocumento() { return soporteDocumento; }

    public void setSoporteDocumento(String soporteDocumento) { this.soporteDocumento = soporteDocumento; }

    public String getFechaCaducidad() { return fechaCaducidad; }

    public void setFechaCaducidad(String fechaCaducidad) { this.fechaCaducidad = fechaCaducidad; }
}
