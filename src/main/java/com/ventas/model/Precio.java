package com.ventas.model;

import java.util.Date;

public class Precio {
    private float monto;
    private Date fecha;

    public Precio() {
        this.monto = 0;
        this.fecha = new Date();
    }

    public void setPrecio(float monto){
        this.monto = monto;
        this.fecha = new Date();
    }
    public float getPrecio() { 
        return monto; 
    }
    
    public float getPrecio(Date fecha) { 
        return monto; 
    }
}