package com.ventas.model;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;

import com.ventas.dto.PrecioDto;
import com.ventas.factories.FabricaDao;

public class Precio extends Modelo<Precio> {
    private float monto;
    private Date fecha;

    //#region Constructors

    public Precio() {
        this.dao = FabricaDao.fabricar("PrecioDao");
        this.mapper = new ModelMapper();
        mapper.getConfiguration().setFieldMatchingEnabled(true).setFieldAccessLevel(AccessLevel.PRIVATE);
    }

    public Precio(PrecioBuilder builder) {
        this(); // Llama al constructor por defecto
        this.setMonto(builder.monto);
        this.setFecha(builder.fecha);
    }

    //#endregion Constructors
    
    //#region PrecioBuilder

    public static class PrecioBuilder {
        private float monto;
        private Date fecha;

        public static PrecioBuilder getBuilder() {
            return new PrecioBuilder();
        }

        public PrecioBuilder conMonto(float monto) {
            this.monto = monto;
            return this;
        }

        public PrecioBuilder conFecha(Date fecha) {
            this.fecha = fecha;
            return this;
        }

        public Precio build() {
            return new Precio(this);
        }
    }

    //#endregion PrecioBuilder

    //#region Getters y Setters
     public float getMonto() {
        return monto;
    }

    public void setMonto(float monto) {
        this.monto = monto;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    //#endregion Getters y Setters

    //#region Business Methods

    @Override
    protected List<Precio> listar(){
        try{
            List<PrecioDto> listado = this.dao.listarTodos();
            if(!listado.isEmpty())
                return Arrays.asList(this.mapper.map(listado, Precio[].class));
        }
        catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    protected List<Precio> buscar(List<String> params){ 
        try{
            PrecioDto precioDto = this.mapper.map(this, PrecioDto.class);
            List<PrecioDto> preciosDto = this.dao.buscar(precioDto, params);
            if(!preciosDto.isEmpty())
                return Arrays.asList(this.mapper.map(preciosDto, Precio[].class));
        }
        catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    protected Precio registrar(){
        try {
            PrecioDto precioDto = this.mapper.map(this, PrecioDto.class);
            precioDto = (PrecioDto) this.dao.actualizar(precioDto,null);
            if(precioDto != null)
                return this.mapper.map(precioDto, Precio.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    protected Precio actualizar(List<String> params) {
        try {
            PrecioDto precioDto = this.mapper.map(this, PrecioDto.class);
            precioDto = (PrecioDto) this.dao.actualizar(precioDto, params);
            if(precioDto != null)
                return this.mapper.map(precioDto, Precio.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    protected Precio eliminar(){
        try {
            PrecioDto precioDto = this.mapper.map(this,PrecioDto.class);
            precioDto = (PrecioDto) this.dao.borrar(precioDto);
            if(precioDto != null)
                return this.mapper.map(precioDto, Precio.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    //#endregion Business Methods
}