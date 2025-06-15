package com.ventas.model;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;

import com.ventas.dto.DescuentoRecargoDto;
import com.ventas.factories.FabricaDao;
import com.ventas.model.Descuento.DescuentoBuilder;
import com.ventas.model.Recargo.RecargoBuilder;

public abstract class DescuentoRecargo extends Modelo<DescuentoRecargo> {
    private String codigoDescuentoRecargo;
    private String nombre;
    private String tipo;
    private float monto;
    private Date fechaInicio;
    private Date fechaFin;
    private boolean habilitado;

    //#region Getters y Setters

    public String getCodigoDescuentoRecargo() {
        return codigoDescuentoRecargo;
    }

    public void setCodigoDescuentoRecargo(String codigoDescuentoRecargo) {
        this.codigoDescuentoRecargo = codigoDescuentoRecargo;
    }

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

    public float calcularPolitica(float monto){
        return 0;
    }

    public float aplicarPolitica(float monto){
        return monto;
    }

    @Override
    public List<DescuentoRecargo> listar(){
        try{
            List<DescuentoRecargoDto> listado = this.dao.listarTodos();
            if(!listado.isEmpty())
                return Arrays.asList(this.mapper.map(listado, DescuentoRecargo[].class));
        }
        catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<DescuentoRecargo> buscar(List<String> params){ 
        try{
            DescuentoRecargoDto descuentoRecargoDto = this.mapper.map(this, DescuentoRecargoDto.class);
            List<DescuentoRecargoDto> descuentoRecargosDto = this.dao.buscar(descuentoRecargoDto, params);
            if(!descuentoRecargosDto.isEmpty())
                return Arrays.asList(this.mapper.map(descuentoRecargosDto, DescuentoRecargo[].class));
        }
        catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public DescuentoRecargo registrar(){
        try {
            DescuentoRecargoDto descuentoRecargoDto = this.mapper.map(this, DescuentoRecargoDto.class);
            descuentoRecargoDto = (DescuentoRecargoDto) this.dao.actualizar(descuentoRecargoDto,null);
            if(descuentoRecargoDto != null)
                return this.mapper.map(descuentoRecargoDto, DescuentoRecargo.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public DescuentoRecargo actualizar(List<String> params) {
        try {
            DescuentoRecargoDto descuentoRecargoDto = this.mapper.map(this, DescuentoRecargoDto.class);
            descuentoRecargoDto = (DescuentoRecargoDto) this.dao.actualizar(descuentoRecargoDto, params);
            if(descuentoRecargoDto != null)
                return this.mapper.map(descuentoRecargoDto, DescuentoRecargo.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public DescuentoRecargo eliminar(){
        try {
            DescuentoRecargoDto descuentoRecargoDto = this.mapper.map(this,DescuentoRecargoDto.class);
            descuentoRecargoDto = (DescuentoRecargoDto) this.dao.borrar(descuentoRecargoDto);
            if(descuentoRecargoDto != null)
                return this.mapper.map(descuentoRecargoDto, DescuentoRecargo.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }
    //#endregion

}