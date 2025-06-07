package com.ventas.dto;

import java.util.Date;

public class UsuarioDto {
    public int id;
    public String username;
    public String password;
    public Date ultimoAcceso;
    public int empleadoId;
    public EmpleadoDto empleado;
}