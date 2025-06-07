package com.ventas.dto;

import java.util.List;

public class ProductoDto {
    public int id;
    public String nombre;
    public String marca;
    public String codigoBarras;
    public List<PrecioDto> precio;
    public List<StockDto> stock;
}