package com.ventas.model;

import java.util.*;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;

import com.ventas.dto.MedioPagoDto;
import com.ventas.factories.FabricaDao;

public class MedioPago extends Modelo {
    private String nombre;
    private List<DescuentoRecargo> descuentoRecargo;
    private boolean habilitado;
    private Date fechaHabilitadoDesde;
    private Date fechaHabilitadoHasta;

    //#region Constructors
    public MedioPago() {
        this.dao = FabricaDao.fabricar("MedioPagoDao");
        this.mapper = new ModelMapper();
        mapper.getConfiguration().setFieldMatchingEnabled(true).setFieldAccessLevel(AccessLevel.PRIVATE);
    }

    public MedioPago(MedioPagoBuilder builder) {
        this();
        this.setNombre(builder.nombre);
        this.setDescuentoRecargo(builder.descuentoRecargo);
        this.setHabilitado(builder.habilitado);
        this.setFechaHabilitadoDesde(builder.fechaHabilitadoDesde);
        this.setFechaHabilitadoHasta(builder.fechaHabilitadoHasta);
    }

    //#endregion

    //#region MedioPagoBuilder

    public static class MedioPagoBuilder {
        private String nombre;
        private List<DescuentoRecargo> descuentoRecargo;
        private boolean habilitado;
        private Date fechaHabilitadoDesde;
        private Date fechaHabilitadoHasta;

        public static MedioPagoBuilder getBuilder() {
            return new MedioPagoBuilder();
        }

        public MedioPagoBuilder conNombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public MedioPagoBuilder conDescuentoRecargo(List<DescuentoRecargo> descuentoRecargo) {
            this.descuentoRecargo = descuentoRecargo;
            return this;
        }

        public MedioPagoBuilder conHabilitado(boolean habilitado) {
            this.habilitado = habilitado;
            return this;
        }

        public MedioPagoBuilder conFechaHabilitadoDesde(Date fechaHabilitadoDesde) {
            this.fechaHabilitadoDesde = fechaHabilitadoDesde;
            return this;
        }

        public MedioPagoBuilder conFechaHabilitadoHasta(Date fechaHabilitadoHasta) {
            this.fechaHabilitadoHasta = fechaHabilitadoHasta;
            return this;
        }

        public MedioPago build() {
            return new MedioPago(this);
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

    public List<DescuentoRecargo> getDescuentoRecargo() {
        return descuentoRecargo;
    }

    public void setDescuentoRecargo(List<DescuentoRecargo> descuentoRecargo) {
        this.descuentoRecargo = descuentoRecargo;
    }
    
    public boolean isHabilitado() {
        return habilitado;
    }
    
    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }
    
    public Date getFechaHabilitadoDesde() {
        return fechaHabilitadoDesde;
    }
    
    public void setFechaHabilitadoDesde(Date fechaHabilitadoDesde) {
        this.fechaHabilitadoDesde = fechaHabilitadoDesde;
    }
    
    public Date getFechaHabilitadoHasta() {
        return fechaHabilitadoHasta;
    }
    
    public void setFechaHabilitadoHasta(Date fechaHabilitadoHasta) {
        this.fechaHabilitadoHasta = fechaHabilitadoHasta;
    }
    
    public boolean checkFechaFin() { 
        return false; 
    }
    
    //#endregion
    
    //#region Busines Methods
    
    public void deshabilitar() {}
    
    public void habilitar() {}
    
    public void checkFechaInicio() {}

    public List<MedioPago> listarMedioPagos(){
        try{
            List<MedioPagoDto> listado = this.dao.listarTodos();
            if(!listado.isEmpty())
                return Arrays.asList(this.mapper.map(listado, MedioPago[].class));
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    public List<MedioPago> consultarMedioPago(List<String> params){ 
        try{
            MedioPagoDto medioPagoDto = this.mapper.map(this, MedioPagoDto.class);
            List<MedioPagoDto> medioPagosDto = this.dao.buscar(medioPagoDto, params);
            if(!medioPagosDto.isEmpty())
                return Arrays.asList(this.mapper.map(medioPagosDto, MedioPago[].class));
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    public MedioPago registrarMedioPago(){
        try {
            MedioPagoDto medioPagoDto = this.mapper.map(this, MedioPagoDto.class);
            medioPagoDto = (MedioPagoDto) this.dao.actualizar(medioPagoDto,null);
            if(medioPagoDto != null)
                return this.mapper.map(medioPagoDto, MedioPago.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public MedioPago actualizararMedioPago(List<String> params) {
        try {
            MedioPagoDto medioPagoDto = this.mapper.map(this, MedioPagoDto.class);
            medioPagoDto = (MedioPagoDto) this.dao.actualizar(medioPagoDto, params);
            if(medioPagoDto != null)
                return this.mapper.map(medioPagoDto, MedioPago.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public MedioPago eliminarMedioPago(){
        try {
            MedioPagoDto medioPagoDto = this.mapper.map(this,MedioPagoDto.class);
            medioPagoDto = (MedioPagoDto) this.dao.borrar(medioPagoDto);
            if(medioPagoDto != null)
                return this.mapper.map(medioPagoDto, MedioPago.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    //#endregion
}