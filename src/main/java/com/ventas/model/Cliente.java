package com.ventas.model;

public class Cliente extends Persona {
    private String nroCliente;

    public Cliente() {
    }

    public String getNro_cliente() {
        return nroCliente;
    }

    public void setNro_cliente(String nro_cliente) {
        this.nroCliente = nro_cliente;
    }
}