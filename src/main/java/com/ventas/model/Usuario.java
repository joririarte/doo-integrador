package com.ventas.model;

import com.ventas.dto.UsuarioDto;
import com.ventas.factories.FabricaDao;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;

public class Usuario extends Modelo{
    private String username;
    private String password;
    private Empleado empleado;
    private Date ultimoAcceso;

    //#region Constructors
    
    public Usuario(){
        this.dao = FabricaDao.fabricar("UsuarioDao");
        this.mapper = new ModelMapper();
        mapper.getConfiguration().setFieldMatchingEnabled(true).setFieldAccessLevel(AccessLevel.PRIVATE);
    }

    public Usuario(UsuarioBuilder builder) {
        this();
        this.setUsername(builder.username);
        this.setPassword(builder.password);
        this.setEmpleado(builder.empleado);
        this.setUltimoAcceso(builder.ultimoAcceso);
    }
    //#endregion

    //#region UsuarioBuilder

    public static class UsuarioBuilder {
        private String username;
        private String password;
        private Empleado empleado;
        private Date ultimoAcceso;

        public static UsuarioBuilder getBuilder() {
            return new UsuarioBuilder();
        }

        public UsuarioBuilder conUsername(String username) {
            this.username = username;
            return this;
        }

        public UsuarioBuilder conPassword(String password) {
            this.password = password;
            return this;
        }

        public UsuarioBuilder conEmpleado(Empleado empleado) {
            this.empleado = empleado;
            return this;
        }

        public UsuarioBuilder conUltimoAcceso(Date ultimoAcceso) {
            this.ultimoAcceso = ultimoAcceso;
            return this;
        }

        public Usuario build() {
            return new Usuario(this);
        }
    }

    //#endregion

    //#region Getters y Setters

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

     public Date getUltimoAcceso() {
        return ultimoAcceso;
    }

    public void setUltimoAcceso(Date ultimoAcceso) {
        this.ultimoAcceso = ultimoAcceso;
    }
    //#endregion

    //#region Business Methods

    public Usuario iniciarSesion(){
        try {
            UsuarioDto user = this.mapper.map(this, UsuarioDto.class);
            List<UsuarioDto> response = this.dao.buscar(user, Arrays.asList("username","password")); 
            if(!response.isEmpty() && response.size() == 1){
                user = response.getFirst();
                this.ultimoAcceso = user.ultimoAcceso;
                user.ultimoAcceso = new Date();
                this.dao.actualizar(user, Arrays.asList("ultimoAcceso"));
                user.ultimoAcceso = this.ultimoAcceso;
                return this.mapper.map(user, Usuario.class);
            }
        }
        catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }
    //#endregion
}