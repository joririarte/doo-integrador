package com.ventas.model;

import java.util.Date;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;

import com.ventas.factories.FabricaDao;
import com.ventas.model.Descuento.DescuentoBuilder;

public class Recargo extends DescuentoRecargo {
    //#region Constructors
    
    public Recargo() {
        this.dao = FabricaDao.fabricar("DescuentoRecargoDao");
        this.mapper = new ModelMapper();
        mapper.getConfiguration().setFieldMatchingEnabled(true).setFieldAccessLevel(AccessLevel.PRIVATE);
    }

    public Recargo(RecargoBuilder builder) {
        this(); 
        this.setNombre(builder.nombre);
        this.setTipo(builder.tipo);
        this.setMonto(builder.monto);
        this.setFechaInicio(builder.fechaInicio);
        this.setFechaFin(builder.fechaFin);
        this.setHabilitado(builder.habilitado);
        this.setCodigoDescuentoRecargo(builder.codigoRecargo);
    }

    //#endregion

    //#region RecargoBuilder

    public static class RecargoBuilder {
        String codigoRecargo;
        private String nombre;
        private String tipo;
        private float monto;
        private Date fechaInicio;
        private Date fechaFin;
        private boolean habilitado;

        public static RecargoBuilder getBuilder() {
            return new RecargoBuilder();
        }
        
        public RecargoBuilder conCodigoRecargo(String codigoRecargo){
            this.codigoRecargo = codigoRecargo;
            return this;
        }

        public RecargoBuilder conNombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public RecargoBuilder conTipo(String tipo) {
            this.tipo = tipo;
            return this;
        }

        public RecargoBuilder conMonto(float monto) {
            this.monto = monto;
            return this;
        }

        public RecargoBuilder conFechaInicio(Date fechaInicio) {
            this.fechaInicio = fechaInicio;
            return this;
        }

        public RecargoBuilder conFechaFin(Date fechaFin) {
            this.fechaFin = fechaFin;
            return this;
        }

        public RecargoBuilder conHabilitado(boolean habilitado) {
            this.habilitado = habilitado;
            return this;
        }

        public Recargo build() {
            return new Recargo(this);
        }
    }
    
    //#endregion

    //#region BusinessMethods
    @Override
    public float aplicarPolitica(float monto) {
        return monto + this.calcularPolitica(monto);
    }

    @Override
    public float calcularPolitica(float monto) {
        return monto * this.getMonto();
    }
    //#endregion
}
