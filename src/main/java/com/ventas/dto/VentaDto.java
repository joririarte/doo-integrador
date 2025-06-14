package com.ventas.dto;

import java.util.*;
public class VentaDto {
    public int ventaId;
    public String codigoVenta;
    public int vendedorId;
    public EmpleadoDto vendedor;
    public Date fecha;
    public String estado;
    public float montoPagado;
    public int medioPagoId;
    public MedioPagoDto medioPago;
    public String codigoDescuentoRecargo;
    public DescuentoRecargoDto descuentoRecargo;
    public int clienteId;
    public ClienteDto cliente;
    public List<DetalleVentaDto> detalleVenta;
}