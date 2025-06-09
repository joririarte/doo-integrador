package com.ventas.model;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;

import com.ventas.dto.DescuentoRecargoDto;
import com.ventas.factories.FabricaDao;

public class DescuentoRecargo extends Modelo {
    private String nombre;
    private String tipo;
    private float monto;
    private Date fechaInicio;
    private Date fechaFin;
    private boolean habilitado;
    
    //#region Constructors
    
    public DescuentoRecargo() {
        this.dao = FabricaDao.fabricar("DescuentoRecargoDao");
        this.mapper = new ModelMapper();
        mapper.getConfiguration().setFieldMatchingEnabled(true).setFieldAccessLevel(AccessLevel.PRIVATE);
    }

    public DescuentoRecargo(DescuentoRecargoBuilder builder) {
        this(); 
        this.setNombre(builder.nombre);
        this.setTipo(builder.tipo);
        this.setMonto(builder.monto);
        this.setFechaInicio(builder.fechaInicio);
        this.setFechaFin(builder.fechaFin);
        this.setHabilitado(builder.habilitado);
    }

    //#endregion

    //#region DescuentoRecargoBuilder

    public static class DescuentoRecargoBuilder {
        private String nombre;
        private String tipo;
        private float monto;
        private Date fechaInicio;
        private Date fechaFin;
        private boolean habilitado;

        public static DescuentoRecargoBuilder getBuilder() {
            return new DescuentoRecargoBuilder();
        }

        public DescuentoRecargoBuilder conNombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public DescuentoRecargoBuilder conTipo(String tipo) {
            this.tipo = tipo;
            return this;
        }

        public DescuentoRecargoBuilder conMonto(float monto) {
            this.monto = monto;
            return this;
        }

        public DescuentoRecargoBuilder conFechaInicio(Date fechaInicio) {
            this.fechaInicio = fechaInicio;
            return this;
        }

        public DescuentoRecargoBuilder conFechaFin(Date fechaFin) {
            this.fechaFin = fechaFin;
            return this;
        }

        public DescuentoRecargoBuilder conHabilitado(boolean habilitado) {
            this.habilitado = habilitado;
            return this;
        }

        public DescuentoRecargo build() {
            return new DescuentoRecargo(this);
        }
    }


    //#endregion

    //#region Getters y Setters
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getTipo() {
        return tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    
    }
    public float getMonto() {
        return monto;
    }
    
    public void setMonto(float monto) {
        this.monto = monto;
    }
    
    public Date getFechaInicio() {
        return fechaInicio;
    }
    
    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
    
    public Date getFechaFin() {
        return fechaFin;
    }
    
    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }
    
    public boolean isHabilitado() {
        return habilitado;
    }
    
    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }

    //#endregion

    //#region Business Methods

    public List<DescuentoRecargo> listarDescuentoRecargos(){
        try{
            List<DescuentoRecargoDto> listado = this.dao.listarTodos();
            if(!listado.isEmpty())
                return Arrays.asList(this.mapper.map(listado, DescuentoRecargo[].class));
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    public List<DescuentoRecargo> consultarDescuentoRecargo(List<String> params){ 
        try{
            DescuentoRecargoDto descuentoRecargoDto = this.mapper.map(this, DescuentoRecargoDto.class);
            List<DescuentoRecargoDto> descuentoRecargosDto = this.dao.buscar(descuentoRecargoDto, params);
            if(!descuentoRecargosDto.isEmpty())
                return Arrays.asList(this.mapper.map(descuentoRecargosDto, DescuentoRecargo[].class));
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    public DescuentoRecargo registrarDescuentoRecargo(){
        try {
            DescuentoRecargoDto descuentoRecargoDto = this.mapper.map(this, DescuentoRecargoDto.class);
            descuentoRecargoDto = (DescuentoRecargoDto) this.dao.actualizar(descuentoRecargoDto,null);
            if(descuentoRecargoDto != null)
                return this.mapper.map(descuentoRecargoDto, DescuentoRecargo.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public DescuentoRecargo eliminarDescuentoRecargo(){
        try {
            DescuentoRecargoDto descuentoRecargoDto = this.mapper.map(this,DescuentoRecargoDto.class);
            descuentoRecargoDto = (DescuentoRecargoDto) this.dao.borrar(descuentoRecargoDto);
            if(descuentoRecargoDto != null)
                return this.mapper.map(descuentoRecargoDto, DescuentoRecargo.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //#endregion

}