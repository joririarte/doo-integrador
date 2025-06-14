package com.ventas.model;

import java.util.*;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;

import com.ventas.dto.DescuentoRecargoDto;
import com.ventas.dto.MedioPagoDto;
import com.ventas.factories.FabricaDao;
import com.ventas.factories.FabricaDescuentoRecargo;
import com.ventas.model.Descuento.DescuentoBuilder;
import com.ventas.model.Recargo.RecargoBuilder;

public class MedioPago extends Modelo {
    private String nombre;
    private String codigoMedioPago;
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
        this.setCodigoMedioPago(builder.codigoMedioPago);
    }

    //#endregion

    //#region MedioPagoBuilder

    public static class MedioPagoBuilder {
        private String nombre;
        private String codigoMedioPago;
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

        public MedioPagoBuilder conCodigoMedioPago(String codigoMedioPago){
            this.codigoMedioPago = codigoMedioPago;
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

    public String getCodigoMedioPago() {
        return codigoMedioPago;
    }

    public void setCodigoMedioPago(String codigoMedioPago) {
        this.codigoMedioPago = codigoMedioPago;
    }
    
    //#endregion
    
    //#region Busines Methods
    
    public void deshabilitar() {}
    
    public void habilitar() {}
    
    public void checkFechaInicio() {}

    public List<MedioPago> listarMedioPagos(){
        try{
            List<MedioPagoDto> listado = this.dao.listarTodos();
            if(!listado.isEmpty()){
                List<MedioPago> resultado = this.mapearMedioPagos(listado);
                return resultado;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<MedioPago> consultarMedioPago(List<String> params){ 
        try{
            MedioPagoDto medioPagoDto = this.mapper.map(this, MedioPagoDto.class);
            List<MedioPagoDto> medioPagosDto = this.dao.buscar(medioPagoDto, params);
            if(!medioPagosDto.isEmpty()){
                List<MedioPago> resultado = this.mapearMedioPagos(medioPagosDto);
                return resultado;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
        }
        return null;
    }
    
    public MedioPago mapearMedioPago(MedioPagoDto mpDto){
        MedioPago mp = MedioPagoBuilder.getBuilder()
                                           .conNombre(mpDto.nombre)
                                           .conCodigoMedioPago(mpDto.codigoMedioPago)
                                           .conHabilitado(mpDto.habilitado)
                                           .conFechaHabilitadoDesde(mpDto.fechaHabilitadoDesde)
                                           .conFechaHabilitadoHasta(mpDto.fechaHabilitadoHasta)
                                           .build();                                            
        List<DescuentoRecargo> listaDescuentoRecargo = mapearPoliticas(mpDto.descuentoRecargo);
        mp.setDescuentoRecargo(listaDescuentoRecargo);
        return mp;
    }
    
    private List<MedioPago> mapearMedioPagos(List<MedioPagoDto> listaMedioPagoDtos){
        List<MedioPago> resultado = new ArrayList<>();
        for(MedioPagoDto mpDto : listaMedioPagoDtos){
            MedioPago mp = mapearMedioPago(mpDto);
            resultado.add(mp);
        }
        return resultado;
    }

    private List<DescuentoRecargo> mapearPoliticas(List<DescuentoRecargoDto> descuentoRecargoDtos){
        List<DescuentoRecargo> descuentoRecargo = new ArrayList<>();
        for(DescuentoRecargoDto drDto : descuentoRecargoDtos){
            DescuentoRecargo dr = FabricaDescuentoRecargo.fabricar(drDto);
            descuentoRecargo.add(dr);
        }
        return descuentoRecargo;
    }
    //#endregion
}