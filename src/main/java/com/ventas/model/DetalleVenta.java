package com.ventas.model;

public class DetalleVenta {
    private String nombre;
    private int cantidad;
    private float descuentoRecargo;
    private float precioVenta;
    private Producto producto;

    public DetalleVenta() {
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setDescuentoRecargo(float descuentoRecargo) {
        this.descuentoRecargo = descuentoRecargo;
    }

    public void setPrecioVenta(float precioVenta) {
        this.precioVenta = precioVenta;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public float getDescuentoRecargo() {
        return descuentoRecargo;
    }

    public float getPrecioVenta() {
        return precioVenta;
    }

    public Producto getProducto() {
        return producto;
    }

    public float calcularSubtotal() { return 0; }
}