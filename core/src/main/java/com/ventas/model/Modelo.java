package com.ventas.model;

import java.util.List;
import org.modelmapper.ModelMapper;
import com.ventas.dao.Dao;

public abstract class Modelo<T> {
    protected Dao dao;
    protected ModelMapper mapper;

    protected abstract List<T> listar();
    protected abstract List<T> buscar(List<String> params);
    protected abstract T registrar();
    protected abstract T actualizar(List<String> params);
    protected abstract T eliminar();

}
