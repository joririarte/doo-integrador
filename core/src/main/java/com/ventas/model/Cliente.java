package com.ventas.model;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;

import com.ventas.dto.ClienteDto;
import com.ventas.factories.FabricaDao;

public class Cliente extends Persona<Cliente> {
    private String nroCliente;

    //#region Constructors

    public Cliente(){
        this.dao = FabricaDao.fabricar("ClienteDao");
        this.mapper = new ModelMapper();
        mapper.getConfiguration().setFieldMatchingEnabled(true).setFieldAccessLevel(AccessLevel.PRIVATE);
        
    }

    public Cliente(ClienteBuilder builder) {
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
        this.setNroCliente(builder.nroCliente);

        
    }

    //#endregion

    //#region ClienteBuilder
    public static class ClienteBuilder {
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
    private String nroCliente;

    public static ClienteBuilder getBuilder() {
        return new ClienteBuilder();
    }

    public ClienteBuilder conNombreApellido(String nombreApellido) {
        this.nombreApellido = nombreApellido;
        return this;
    }

    public ClienteBuilder conTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
        return this;
    }

    public ClienteBuilder conNroDocumento(String nroDocumento) {
        this.nroDocumento = nroDocumento;
        return this;
    }

    public ClienteBuilder conCUIT(String CUIT) {
        this.CUIT = CUIT;
        return this;
    }

    public ClienteBuilder conCondicionAfip(String condicionAfip) {
        this.condicionAfip = condicionAfip;
        return this;
    }

    public ClienteBuilder conGenero(String genero) {
        this.genero = genero;
        return this;
    }

    public ClienteBuilder conFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
        return this;
    }

    public ClienteBuilder conDomicilio(String domicilio) {
        this.domicilio = domicilio;
        return this;
    }

    public ClienteBuilder conEmail(String email) {
        this.email = email;
        return this;
    }

    public ClienteBuilder conTelefonos(List<String> telefonos) {
        this.telefonos = telefonos;
        return this;
    }

    public ClienteBuilder conNroCliente(String nroCliente) {
        this.nroCliente = nroCliente;
        return this;
    }

    public Cliente build() {
        return new Cliente(this);
    }
}

    //#endregion

    //#region Getters y Setters
    public String getNroCliente() {
        return nroCliente;
    }

    public void setNroCliente(String nroCliente) {
        this.nroCliente = nroCliente;
    }
    //#endregion

    //#region Business Methods
    @Override
    public List<Cliente> listar(){
        try{
            List<ClienteDto> listado = this.dao.listarTodos();
            if(!listado.isEmpty())
                return Arrays.asList(this.mapper.map(listado, Cliente[].class));
        }
        catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<Cliente> buscar(List<String> params){ 
        try{
            ClienteDto clienteDto = this.mapper.map(this, ClienteDto.class);
            List<ClienteDto> clientesDto = this.dao.buscar(clienteDto, params);
            if(!clientesDto.isEmpty())
                return Arrays.asList(this.mapper.map(clientesDto, Cliente[].class));
        }
        catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }
    
    @Override
    public Cliente registrar(){
        try {
            ClienteDto clienteDto = this.mapper.map(this, ClienteDto.class);
            clienteDto = (ClienteDto) this.dao.actualizar(clienteDto,null);
            if(clienteDto != null)
                return this.mapper.map(clienteDto, Cliente.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Cliente actualizar(List<String> params) {
        try {
            ClienteDto clienteDto = this.mapper.map(this, ClienteDto.class);
            clienteDto = (ClienteDto) this.dao.actualizar(clienteDto, params);
            if(clienteDto != null)
                return this.mapper.map(clienteDto, Cliente.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Cliente eliminar(){
        try {
            ClienteDto clienteDto = this.mapper.map(this,ClienteDto.class);
            clienteDto = (ClienteDto) this.dao.borrar(clienteDto);
            if(clienteDto != null)
                return this.mapper.map(clienteDto, Cliente.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }
    //#endregion
}