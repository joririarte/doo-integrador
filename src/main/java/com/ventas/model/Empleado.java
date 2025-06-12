package com.ventas.model;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;

import com.ventas.dto.EmpleadoDto;
import com.ventas.factories.FabricaDao;

public class Empleado extends Persona{
    private String cargo;
    private String legajo;

    //#region Constructors

    public Empleado(){
        this.dao = FabricaDao.fabricar("EmpleadoDao");
        this.mapper = new ModelMapper();
        mapper.getConfiguration().setFieldMatchingEnabled(true).setFieldAccessLevel(AccessLevel.PRIVATE);
    }

    public Empleado(EmpleadoBuilder builder) {
        this();
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
        this.setLegajo(builder.legajo);
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
        private String legajo;

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

        public EmpleadoBuilder conLegajo(String legajo) {
            this.legajo = legajo;
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

    public String getLegajo() {
        return legajo;
    }

    public void setLegajo(String legajo) {
        this.legajo = legajo;
    }
    
    //#endregion
    
    //#region Business Methods
    
    public void generarInformeVentaDiaria() {}
    public void generarInformeVentaCajero(Empleado c) {}
    public void generarInformeStockGeneral() {}
    public void generarInformeStockProducto(Producto p) {}

    public List<Empleado> listarEmpleados(){
        try{
            List<EmpleadoDto> listado = this.dao.listarTodos();
            if(!listado.isEmpty())
                return Arrays.asList(this.mapper.map(listado, Empleado[].class));
        }
        catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<Empleado> consultarEmpleado(List<String> params){
        try{
            EmpleadoDto empleadoDto = this.mapper.map(this, EmpleadoDto.class);
            List<EmpleadoDto> empleadosDto = this.dao.buscar(empleadoDto, params);
            if(!empleadosDto.isEmpty())
            return Arrays.asList(this.mapper.map(empleadosDto, Empleado[].class));
        }
        catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    public Empleado registrarEmpleado(){
        try {
            EmpleadoDto empleadoDto = this.mapper.map(this, EmpleadoDto.class);
            empleadoDto = (EmpleadoDto) this.dao.actualizar(empleadoDto,null);
            if(empleadoDto != null)
                return this.mapper.map(empleadoDto, Empleado.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    public Empleado actualizararEmpleado(List<String> params) {
        try {
            EmpleadoDto empleadoDto = this.mapper.map(this, EmpleadoDto.class);
            empleadoDto = (EmpleadoDto) this.dao.actualizar(empleadoDto, params);
            if(empleadoDto != null)
                return this.mapper.map(empleadoDto, Empleado.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    public Empleado eliminarEmpleado(){
        try {
            EmpleadoDto empleadoDto = this.mapper.map(this,EmpleadoDto.class);
            empleadoDto = (EmpleadoDto) this.dao.borrar(empleadoDto);
            if(empleadoDto != null)
                return this.mapper.map(empleadoDto, Empleado.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    //#endregion
}