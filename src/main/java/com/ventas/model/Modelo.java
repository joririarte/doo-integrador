package com.ventas.model;

import org.modelmapper.ModelMapper;

import com.ventas.dao.Dao;

public abstract class Modelo {
    protected Dao dao;
    protected ModelMapper mapper;
}
