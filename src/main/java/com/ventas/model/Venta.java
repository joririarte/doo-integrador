package com.ventas.model;

import java.util.*;
import java.util.Date;

public class Venta {
    private Empleado vendedor;
    private Date fecha;
    private String estado;
    private float montoPagado;
    private MedioPago medioPago;
    private Cliente cliente;
    private List<DetalleVenta> detalleVenta;

    public Venta() {
    }
    public Empleado getVendedor() {
        return vendedor;
    }
    public void setVendedor(Empleado vendedor) {
        this.vendedor = vendedor;
    }
    public Date getFecha() {
        return fecha;
    }
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public float getMontoPagado() {
        return montoPagado;
    }
    public void setMontoPagado(float montoPagado) {
        this.montoPagado = montoPagado;
    }
    public MedioPago getMedioPago() {
        return medioPago;
    }
    public void setMedioPago(MedioPago medioPago) {
        this.medioPago = medioPago;
    }
    public Cliente getCliente() {
        return cliente;
    }
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    public List<DetalleVenta> getDetalleVenta() {
        return detalleVenta;
    }
    public void setDetalleVenta(List<DetalleVenta> detalleVenta) {
        this.detalleVenta = detalleVenta;
    }
    public float calcularMontoTotal() { return 0; }
    public float calcularVuelto() { return 0; }
    public void setCliente() {}
    public void agregarDetalleProducto() {}
    public MedioPago obtenerMedioPago() { return null; }
    public void cobrar() {}
    public boolean checkCobro() { return false; }
    public void cancelar() {}
}