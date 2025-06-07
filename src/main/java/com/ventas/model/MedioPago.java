package com.ventas.model;

import java.util.*;
import java.util.Date;

public class MedioPago {
    private String nombre;
    private List<DescuentoRecargo> descuentoRecargo;
    private boolean habilitado;
    private Date fechaHabilitadoDesde;
    private Date fechaHabilitadoHasta;

    public MedioPago() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<DescuentoRecargo> getDescuentoRecargo() {
        return descuentoRecargo;
    }

    public void setDescuentoRecargo(List<DescuentoRecargo> descuentoRecargo) {
        this.descuentoRecargo = descuentoRecargo;
    }
    
    public boolean isHabilitado() {
        return habilitado;
    }
    
    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }
    
    public Date getFechaHabilitadoDesde() {
        return fechaHabilitadoDesde;
    }
    
    public void setFechaHabilitadoDesde(Date fechaHabilitadoDesde) {
        this.fechaHabilitadoDesde = fechaHabilitadoDesde;
    }
    
    public Date getFechaHabilitadoHasta() {
        return fechaHabilitadoHasta;
    }
    
    public void setFechaHabilitadoHasta(Date fechaHabilitadoHasta) {
        this.fechaHabilitadoHasta = fechaHabilitadoHasta;
    }
    
    public boolean checkFechaFin() { 
        return false; 
    }
    
    public void deshabilitar() {}
    
    public void habilitar() {}
    
    public void checkFechaInicio() {}
}