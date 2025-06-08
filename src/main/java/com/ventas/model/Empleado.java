package com.ventas.model;

import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;

import com.ventas.factories.FabricaDao;

public class Empleado extends Persona{
    private String cargo;

    //#region Constructors

    public Empleado(){
        this.dao = FabricaDao.fabricar("EmpleadoDao");
        this.mapper = new ModelMapper();
        mapper.getConfiguration().setFieldMatchingEnabled(true).setFieldAccessLevel(AccessLevel.PRIVATE);
    }

    public Empleado(EmpleadoBuilder builder) {
        this.setNombreApellido(builder.nombreApellido);
        this.setTipoDocumento(builder.tipoDocumento);
        this.setNroDocumento(builder.nroDocumento);
        this.setCUIT(builder.CUIT);
        this.setCondicionAfip(builder.condicionAfip);
        this.setGenero(builder.genero);
        this.setFechaNacimiento(builder.fechaNacimiento);
        this.setDomicilio(builder.domicilio);
        this.setEmail(builder.email);
        this.setTelefonos(builder.telefonos);
        this.setCargo(builder.cargo);
    }

    //#endregion

    //#region EmpleadoBuilder

    public static class EmpleadoBuilder {
        private String nombreApellido;
        private String tipoDocumento;
        private String nroDocumento;
        private String CUIT;
        private String condicionAfip;
        private String genero;
        private Date fechaNacimiento;
        private String domicilio;
        private String email;
        private List<String> telefonos;
        private String cargo;

        public static EmpleadoBuilder getBuilder() {
            return new EmpleadoBuilder();
        }

        public EmpleadoBuilder conNombreApellido(String nombreApellido) {
            this.nombreApellido = nombreApellido;
            return this;
        }

        public EmpleadoBuilder conTipoDocumento(String tipoDocumento) {
            this.tipoDocumento = tipoDocumento;
            return this;
        }

        public EmpleadoBuilder conNroDocumento(String nroDocumento) {
            this.nroDocumento = nroDocumento;
            return this;
        }

        public EmpleadoBuilder conCUIT(String CUIT) {
            this.CUIT = CUIT;
            return this;
        }

        public EmpleadoBuilder conCondicionAfip(String condicionAfip) {
            this.condicionAfip = condicionAfip;
            return this;
        }

        public EmpleadoBuilder conGenero(String genero) {
            this.genero = genero;
            return this;
        }

        public EmpleadoBuilder conFechaNacimiento(Date fechaNacimiento) {
            this.fechaNacimiento = fechaNacimiento;
            return this;
        }

        public EmpleadoBuilder conDomicilio(String domicilio) {
            this.domicilio = domicilio;
            return this;
        }

        public EmpleadoBuilder conEmail(String email) {
            this.email = email;
            return this;
        }

        public EmpleadoBuilder conTelefonos(List<String> telefonos) {
            this.telefonos = telefonos;
            return this;
        }

        public EmpleadoBuilder conCargo(String cargo) {
            this.cargo = cargo;
            return this;
        }

        public Empleado build() {
            return new Empleado(this);
        }
    }


    //#endregion
    
    //#region Getters y Setters
    
    public String getCargo() {
        return cargo;
    }
    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
    
    //#endregion
    
    //#region Business Methods
    
    public void generarInformeVentaDiaria() {}
    public void generarInformeVentaCajero(Empleado c) {}
    public void generarInformeStockGeneral() {}
    public void generarInformeStockProducto(Producto p) {}

    //#endregion
}