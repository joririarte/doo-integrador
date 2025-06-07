package com.ventas.dto;

import java.util.Date;
public class DescuentoRecargoDto {
    public int id;
    public String nombre;
    public String tipo;
    public float monto;
    public Date fechaInicio;
    public Date fechaFin;
    public boolean habilitado;
    public int medioPagoId;
}