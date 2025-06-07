package com.ventas.model;

import java.util.*;

public class Producto {
    private String nombre;
    private List<Stock> Stock;
    private List<Precio> precio;
    private String Marca;
    private String codigoBarras;
    
    public Producto() {
    }

    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public List<Stock> getStock() {
        return Stock;
    }
    
    public void setStock(List<Stock> stock) {
        Stock = stock;
    }
    
    public List<Precio> getPrecio() {
        return precio;
    }
    
    public void setPrecio(List<Precio> precio) {
        this.precio = precio;
    }
    
    public String getMarca() {
        return Marca;
    }
    
    public void setMarca(String marca) {
        Marca = marca;
    }
    
    public String getCodigoBarras() {
        return codigoBarras;
    }
    
    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }
}