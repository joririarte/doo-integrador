package com.ventas.dto;

import java.util.*;
public class MedioPagoDto {
    public int id;
    public String nombre;
    public List<DescuentoRecargoDto> descuentoRecargo;
    public boolean habilitado;
    public Date fechaHabilitadoDesde;
    public Date fechaHabilitadoHasta;
}