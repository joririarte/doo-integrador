package com.ventas.model;

import java.util.Date;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;

import com.ventas.factories.FabricaDao;

public class Descuento extends DescuentoRecargo {
    //#region Constructors
    
    public Descuento() {
        this.dao = FabricaDao.fabricar("DescuentoRecargoDao");
        this.mapper = new ModelMapper();
        mapper.getConfiguration().setFieldMatchingEnabled(true).setFieldAccessLevel(AccessLevel.PRIVATE);
    }

    public Descuento(DescuentoBuilder builder) {
        this(); 
        this.setNombre(builder.nombre);
        this.setTipo(builder.tipo);
        this.setMonto(builder.monto);
        this.setFechaInicio(builder.fechaInicio);
        this.setFechaFin(builder.fechaFin);
        this.setHabilitado(builder.habilitado);
        this.setCodigoDescuentoRecargo(builder.codigoDescuento);
    }

    //#endregion

    //#region DescuentoBuilder

    public static class DescuentoBuilder {
        private String codigoDescuento;
        private String nombre;
        private String tipo;
        private float monto;
        private Date fechaInicio;
        private Date fechaFin;
        private boolean habilitado;

        public static DescuentoBuilder getBuilder() {
            return new DescuentoBuilder();
        }

        public DescuentoBuilder conCodigoDescuento(String codigoDescuento){
            this.codigoDescuento = codigoDescuento;
            return this;
        }

        public DescuentoBuilder conNombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public DescuentoBuilder conTipo(String tipo) {
            this.tipo = tipo;
            return this;
        }

        public DescuentoBuilder conMonto(float monto) {
            this.monto = monto;
            return this;
        }

        public DescuentoBuilder conFechaInicio(Date fechaInicio) {
            this.fechaInicio = fechaInicio;
            return this;
        }

        public DescuentoBuilder conFechaFin(Date fechaFin) {
            this.fechaFin = fechaFin;
            return this;
        }

        public DescuentoBuilder conHabilitado(boolean habilitado) {
            this.habilitado = habilitado;
            return this;
        }

        public Descuento build() {
            return new Descuento(this);
        }
    }
    
    //#endregion

    //#region BusinessMethods

    @Override
    public float aplicarPolitica(float monto) {
        return monto - this.calcularPolitica(monto);
    }

    @Override
    public float calcularPolitica(float monto) {
        return monto * this.getMonto();
    }

    //#endregion
}
