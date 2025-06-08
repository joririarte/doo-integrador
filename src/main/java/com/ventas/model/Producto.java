package com.ventas.model;

import java.util.*;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;

import com.ventas.dao.ProductoDao;
import com.ventas.dto.ProductoDto;
import com.ventas.factories.FabricaDao;

public class Producto extends Modelo {
    private String nombre;
    private List<Stock> stock;
    private List<Precio> precio;
    private String marca;
    private String codigoBarras;
    
    public Producto() {
        this.dao = FabricaDao.fabricar("ProductoDao");
        this.mapper = new ModelMapper();
        mapper.getConfiguration().setFieldMatchingEnabled(true).setFieldAccessLevel(AccessLevel.PRIVATE);

    }

    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public List<Stock> getStock() {
        return stock;
    }
    
    public void setStock(List<Stock> stock) {
        this.stock = stock;
    }
    
    public List<Precio> getPrecio() {
        return precio;
    }
    
    public void setPrecio(List<Precio> precio) {
        this.precio = precio;
    }
    
    public String getMarca() {
        return marca;
    }
    
    public void setMarca(String marca) {
        this.marca = marca;
    }
    
    public String getCodigoBarras() {
        return codigoBarras;
    }
    
    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public Boolean obtenerProducto(String codigoBarras){
        try{
            ProductoDto productoDto = new ProductoDto();
            productoDto.codigoBarras = codigoBarras;

            List<ProductoDto> p = this.dao.buscar(productoDto, Arrays.asList("codigoBarras"));
            if(!p.isEmpty()){
                productoDto = p.getFirst();
                this.nombre = productoDto.nombre;
                this.marca = productoDto.marca;
                this.codigoBarras = productoDto.codigoBarras;
                this.stock = Arrays.asList(this.mapper.map(productoDto.stock, Stock[].class));
                this.precio = Arrays.asList(this.mapper.map(productoDto.precio, Precio[].class));
            }        
        }
        catch (Exception ex){

        }

        return false;
    }
}