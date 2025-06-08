package com.ventas.model;

import java.util.*;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;

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
    //#region Getters y Setters
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
    //#endregion
    
    //#region Business Methods
    public Boolean buscarProducto(String codigoBarras){
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
            ex.printStackTrace();
        }
        return false;
    }

    public List<Producto> listarProductos(){
        try{
            List<ProductoDto> listado = this.dao.listarTodos();
            if(!listado.isEmpty()){
                List<Producto> listaProducto = Arrays.asList(this.mapper.map(listado, Producto[].class));
                return listaProducto;
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    public Boolean registrarProducto(Producto nuevoProducto){
        try {
            ProductoDto productoDto = this.mapper.map(nuevoProducto, ProductoDto.class);
            productoDto = (ProductoDto) this.dao.actualizar(productoDto, null);

            nuevoProducto = this.mapper.map(productoDto, Producto.class);
            return true;
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Boolean actualizarProducto(Producto producto, List<String> params){
        try {
            ProductoDto productoDto = this.mapper.map(producto, ProductoDto.class);

            productoDto = (ProductoDto) this.dao.actualizar(productoDto, params);

            producto = this.mapper.map(productoDto, Producto.class);
            return true;

        } catch (Exception e) {
            e.printStackTrace();        
        }
        return false;
    }

    public Boolean eliminarProducto(Producto producto){
        try {
            ProductoDto productoDto = this.mapper.map(producto, ProductoDto.class);
            productoDto = (ProductoDto) this.dao.borrar(productoDto);
            producto = this.mapper.map(productoDto, Producto.class);
            return true;

        } catch (Exception e) {
            e.printStackTrace();        
        }
        return false;
    }

    //#endregion
}