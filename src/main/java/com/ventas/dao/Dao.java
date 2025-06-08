package com.ventas.dao;

import java.util.List;

import com.ventas.dto.ProductoDto;

public interface Dao<T> {
    List<T> listarTodos();
    T buscar(T obj);
    List<T> buscar(T obj, List<String> params);
    T actualizar(T obj, List<String> params);
    T borrar(T obj);
}
