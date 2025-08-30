package com.ventas.dao;

import java.util.List;

public interface Dao<T> {
    List<T> listarTodos();
    T buscar(int id);
    List<T> buscar(T obj, List<String> params);
    T actualizar(T obj, List<String> params);
    T borrar(T obj);
}
