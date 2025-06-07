package com.ventas.dto;

import java.util.*;
public class VentaDto {
    public int id;
    public EmpleadoDto vendedor;
    public int empleadoId;
    public Date fecha;
    public String estado;
    public float montoPagado;
    public MedioPagoDto medioPago;
    public int medioPagoId;
    public ClienteDto cliente;
    public int clienteId;
    public List<DetalleVentaDto> detalleVenta;
}