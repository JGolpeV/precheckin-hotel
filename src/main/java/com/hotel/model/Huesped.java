package com.hotel.model;

public class Huesped {

    private Integer id;

    // Datos personales
    private String nombre;
    private String apellido1;
    private String apellido2;
    private String sexo;
    private String nacionalidad;
    private String fechaNacimiento;

    // Contacto
    private String telefono;
    private String email;

    // Dirección
    private String direccion;
    private String municipio;
    private String codigoPostal;
    private String paisResidencia;

    public Huesped() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }


    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getPaisResidencia() {
        return paisResidencia;
    }

    public void setPaisResidencia(String paisResidencia) {
        this.paisResidencia = paisResidencia;
    }


    public String getNombreCompleto() {
        StringBuilder sb = new StringBuilder();

        if (nombre != null && !nombre.isBlank()) {
            sb.append(nombre.trim());
        }

        if (apellido1 != null && !apellido1.isBlank()) {
            if (!sb.isEmpty()) sb.append(" ");
            sb.append(apellido1.trim());
        }

        if (apellido2 != null && !apellido2.isBlank()) {
            if (!sb.isEmpty()) sb.append(" ");
            sb.append(apellido2.trim());
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        return getNombreCompleto();
    }
}