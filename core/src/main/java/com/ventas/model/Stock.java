package com.ventas.model;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;

import com.ventas.dto.StockDto;
import com.ventas.factories.FabricaDao;

public class Stock extends Modelo<Stock>{
    private float cantidad;
    private Date fecha;
    
    //#region Constructors
    public Stock() {
        this.dao = FabricaDao.fabricar("StockDao");
        this.mapper = new ModelMapper();
        mapper.getConfiguration().setFieldMatchingEnabled(true).setFieldAccessLevel(AccessLevel.PRIVATE);
    }

    public Stock(StockBuilder builder) {
        this(); // Llama al constructor por defecto
        this.setCantidad(builder.cantidad);
        this.setFecha(builder.fecha);
    }

    //#endregion Constructors

    //#region StockBuilder 

    public static class StockBuilder {
        private float cantidad;
        private Date fecha;

        public static StockBuilder getBuilder() {
            return new StockBuilder();
        }

        public StockBuilder conCantidad(float cantidad) {
            this.cantidad = cantidad;
            return this;
        }

        public StockBuilder conFecha(Date fecha) {
            this.fecha = fecha;
            return this;
        }

        public Stock build() {
            return new Stock(this);
        }
    }

    //#endregion StockBuilder 

    //#region Getters Y Setters

    public float getCantidad() {
        return cantidad;
    }
    public void setCantidad(float cantidad) {
        this.cantidad = cantidad;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    //#endregion Getters Y Setters

    //#region Business Methods
    @Override
    public List<Stock> listar(){
        try{
            List<StockDto> listado = this.dao.listarTodos();
            if(!listado.isEmpty())
                return Arrays.asList(this.mapper.map(listado, Stock[].class));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Stock> buscar(List<String> params){ 
        try{
            StockDto stockDto = this.mapper.map(this, StockDto.class);
            List<StockDto> stocksDto = this.dao.buscar(stockDto, params);
            if(!stocksDto.isEmpty())
                return Arrays.asList(this.mapper.map(stocksDto, Stock[].class));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Stock registrar(){
        try {
            StockDto stockDto = this.mapper.map(this, StockDto.class);
            stockDto = (StockDto) this.dao.actualizar(stockDto,null);
            if(stockDto != null)
                return this.mapper.map(stockDto, Stock.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Stock actualizar(List<String> params) {
        try {
            StockDto stockDto = this.mapper.map(this, StockDto.class);
            stockDto = (StockDto) this.dao.actualizar(stockDto, params);
            if(stockDto != null)
                return this.mapper.map(stockDto, Stock.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Stock eliminar(){
        try {
            StockDto stockDto = this.mapper.map(this,StockDto.class);
            stockDto = (StockDto) this.dao.borrar(stockDto);
            if(stockDto != null)
                return this.mapper.map(stockDto, Stock.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    //#endregion Business Methods
}