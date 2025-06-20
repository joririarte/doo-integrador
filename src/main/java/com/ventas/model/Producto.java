package com.ventas.model;

import java.util.*;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;

import com.ventas.dto.ProductoDto;
import com.ventas.factories.FabricaDao;

public class Producto extends Modelo<Producto> {
    private String nombre;
    private List<Stock> stock;
    private List<Precio> precio;
    private String marca;
    private String codigoBarras;

    //#region Constructors

    public Producto() {
        this.dao = FabricaDao.fabricar("ProductoDao");
        this.mapper = new ModelMapper();
        mapper.getConfiguration().setFieldMatchingEnabled(true).setFieldAccessLevel(AccessLevel.PRIVATE);

    }

    public Producto(ProductoBuilder builder) {
        this();
        this.setNombre(builder.nombre);
        this.setStock(builder.stock);
        this.setPrecio(builder.precio);
        this.setMarca(builder.marca);
        this.setCodigoBarras(builder.codigoBarras);
    }

    //#endregion Constructors

    //#region ProductoBuilder
    public static class ProductoBuilder {
        private String nombre;
        private List<Stock> stock;
        private List<Precio> precio;
        private String marca;
        private String codigoBarras;

        public static ProductoBuilder getBuilder() {
            return new ProductoBuilder();
        }

        public ProductoBuilder conNombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public ProductoBuilder conStock(List<Stock> stock) {
            this.stock = stock;
            return this;
        }

        public ProductoBuilder conPrecio(List<Precio> precio) {
            this.precio = precio;
            return this;
        }

        public ProductoBuilder conMarca(String marca) {
            this.marca = marca;
            return this;
        }

        public ProductoBuilder conCodigoBarras(String codigoBarras) {
            this.codigoBarras = codigoBarras;
            return this;
        }

        public Producto build() {
            return new Producto(this);
        }
    }

    //#endregion ProductoBuilder

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

    public float getLastStock(){
        return this.stock.getFirst().getCantidad();
    }
    
    public void setStock(List<Stock> stock) {
        this.stock = stock;
    }
    
    public List<Precio> getPrecio() {
        return precio;
    }
    
    public float getLastPrecio(){
        return this.precio.getFirst().getMonto();
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
    
    //#region Access Methods
    @Override
    public List<Producto> buscar(List<String> params){
        try{
            ProductoDto productoDto = this.mapper.map(this, ProductoDto.class);
            List<ProductoDto> p = this.dao.buscar(productoDto, params);
            if(!p.isEmpty())
                return Arrays.asList(this.mapper.map(p, Producto[].class));        
        }
        catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<Producto> listar(){
        try{
            List<ProductoDto> listado = this.dao.listarTodos();
            if(!listado.isEmpty())
                return Arrays.asList(this.mapper.map(listado, Producto[].class));
        }
        catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Producto registrar(){
        try {
            ProductoDto productoDto = this.mapper.map(this, ProductoDto.class);
            productoDto = (ProductoDto) this.dao.actualizar(productoDto, null);
            if(productoDto != null)
                return this.mapper.map(productoDto, Producto.class);  
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);   
        }
        return null;
    }

    @Override
    public Producto actualizar(List<String> params){
        try {
            ProductoDto productoDto = this.mapper.map(this, ProductoDto.class);
            productoDto = (ProductoDto) this.dao.actualizar(productoDto, params);
            if(productoDto != null)
                return this.mapper.map(productoDto, Producto.class);
        } catch (Exception e) {
            e.printStackTrace(); 
            throw new RuntimeException(e);       
        }
        return null;
    }

    @Override
    public Producto eliminar(){
        try {
            ProductoDto productoDto = this.mapper.map(this, ProductoDto.class);
            productoDto = (ProductoDto) this.dao.borrar(productoDto);
            if(productoDto != null)
                return this.mapper.map(productoDto, Producto.class);
        } catch (Exception e) {
            e.printStackTrace();   
            throw new RuntimeException(e);        
        }
        return null;
    }

    //#endregion
}