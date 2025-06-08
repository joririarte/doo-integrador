package com.ventas.model;

import java.util.*;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;

import com.ventas.factories.FabricaDao;

public class Venta extends Modelo {
    private Empleado vendedor;
    private Date fecha;
    private String estado;
    private float montoPagado;
    private MedioPago medioPago;
    private Cliente cliente;
    private List<DetalleVenta> detalleVenta;

    //#region Constructors
    public Venta() {
        this.dao = FabricaDao.fabricar("VentaDao");
        this.mapper = new ModelMapper();
        mapper.getConfiguration().setFieldMatchingEnabled(true).setFieldAccessLevel(AccessLevel.PRIVATE);
    }

    public Venta(VentaBuilder builder) {
        this(); // Llama al constructor por defecto
        this.setVendedor(builder.vendedor);
        this.setFecha(builder.fecha);
        this.setEstado(builder.estado);
        this.setMontoPagado(builder.montoPagado);
        this.setMedioPago(builder.medioPago);
        this.setCliente(builder.cliente);
        this.setDetalleVenta(builder.detalleVenta);
    }
    //#endregion

    //#region VentaBuilder

    public static class VentaBuilder {
    private Empleado vendedor;
    private Date fecha;
    private String estado;
    private float montoPagado;
    private MedioPago medioPago;
    private Cliente cliente;
    private List<DetalleVenta> detalleVenta;

    public static VentaBuilder getBuilder() {
        return new VentaBuilder();
    }

    public VentaBuilder conVendedor(Empleado vendedor) {
        this.vendedor = vendedor;
        return this;
    }

    public VentaBuilder conFecha(Date fecha) {
        this.fecha = fecha;
        return this;
    }

    public VentaBuilder conEstado(String estado) {
        this.estado = estado;
        return this;
    }

    public VentaBuilder conMontoPagado(float montoPagado) {
        this.montoPagado = montoPagado;
        return this;
    }

    public VentaBuilder conMedioPago(MedioPago medioPago) {
        this.medioPago = medioPago;
        return this;
    }

    public VentaBuilder conCliente(Cliente cliente) {
        this.cliente = cliente;
        return this;
    }

    public VentaBuilder conDetalleVenta(List<DetalleVenta> detalleVenta) {
        this.detalleVenta = detalleVenta;
        return this;
    }

    public Venta build() {
        return new Venta(this);
    }
}


    //#endregion

    //#region Getters y Setters

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

    //#endregion

    //#region Business Methods
    
    public float calcularMontoTotal() { return 0; }
    public float calcularVuelto() { return 0; }
    public void setCliente() {}
    public void agregarDetalleProducto() {}
    public MedioPago obtenerMedioPago() { return null; }
    public void cobrar() {}
    public boolean checkCobro() { return false; }
    public void cancelar() {}
    
    //#endregion
}