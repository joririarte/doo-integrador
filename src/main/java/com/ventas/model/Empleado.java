package com.ventas.model;

public class Empleado extends Persona{
    private String cargo;

    public Empleado() {
        super();
    }
    public String getCargo() {
        return cargo;
    }
    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
    public void generarInformeVentaDiaria() {}
    public void generarInformeVentaCajero(Empleado c) {}
    public void generarInformeStockGeneral() {}
    public void generarInformeStockProducto(Producto p) {}
}