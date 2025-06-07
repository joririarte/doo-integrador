package com.ventas.model;

import java.util.Date;

public class Stock {
    private float cantidad;
    private Date fecha;
    
    public Stock() {
        this.cantidad = 0;
        this.fecha = new Date();
    }

    public void setStock(float cantidad){
        this.cantidad = cantidad;
        this.fecha = new Date();
    }
    
    public float getStock() { 
        return cantidad; 
    }

    public float getStock(Date fecha) { 
        return cantidad; 
    }
}