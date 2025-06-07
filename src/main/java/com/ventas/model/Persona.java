package com.ventas.model;

import java.util.*;

public abstract class Persona extends Modelo{
    private String nombreApellido;
    private String tipoDocumento;
    private String nroDocumento;
    private String CUIT;
    private String condicionAfip;
    private String genero;
    private Date fechaNacimiento;
    private String domicilio;
    private String email;
    private List<String> telefonos;
    public String getNombreApellido() {
        return nombreApellido;
    }
    public void setNombreApellido(String nombreApellido) {
        this.nombreApellido = nombreApellido;
    }
    public String getTipoDocumento() {
        return tipoDocumento;
    }
    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }
    public String getNroDocumento() {
        return nroDocumento;
    }
    public void setNroDocumento(String nroDocumento) {
        this.nroDocumento = nroDocumento;
    }
    public String getCUIT() {
        return CUIT;
    }
    public void setCUIT(String cUIT) {
        CUIT = cUIT;
    }
    public String getCondicionAfip() {
        return condicionAfip;
    }
    public void setCondicionAfip(String condicionAfip) {
        this.condicionAfip = condicionAfip;
    }
    public String getGenero() {
        return genero;
    }
    public void setGenero(String genero) {
        this.genero = genero;
    }
    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }
    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
    public String getDomicilio() {
        return domicilio;
    }
    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public List<String> getTelefonos() {
        return telefonos;
    }
    public void setTelefonos(List<String> telefonos) {
        this.telefonos = telefonos;
    }
    public Persona() {
    }
}