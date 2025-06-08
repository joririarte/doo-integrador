package com.ventas.model;

import com.ventas.dto.UsuarioDto;
import com.ventas.factories.FabricaDao;

import java.util.Arrays;
import java.util.Date;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;

public class Usuario extends Modelo{
    private String username;
    private String password;
    private Empleado empleado;
    private Date ultimoAcceso;

    public Usuario(){
        this.dao = FabricaDao.fabricar("UsuarioDao");
        this.mapper = new ModelMapper();
        mapper.getConfiguration().setFieldMatchingEnabled(true).setFieldAccessLevel(AccessLevel.PRIVATE);
    }

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

    public Boolean iniciarSesion(String username, String pass){
        UsuarioDto user = new UsuarioDto();
        
        user.username=username;
        user.password=pass;
        try {
            user = (UsuarioDto) this.dao.buscar(user);
            if(user != null){
                this.username = user.username;
                this.password = user.password;
                this.ultimoAcceso = user.ultimoAcceso;
                this.empleado = this.mapper.map(user.empleado, Empleado.class);

                this.dao.actualizar(user, Arrays.asList("ultimoAcceso"));
                
                return true;
            }
        }
        catch (Exception ex){
            System.out.println(user);
            System.out.println("ocurrio un error");
        }
        return false;
    }
    //#endregion
}