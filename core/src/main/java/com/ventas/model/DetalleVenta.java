package com.ventas.model;

import java.util.Arrays;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;

import com.ventas.dto.DetalleVentaDto;
import com.ventas.factories.FabricaDao;

public class DetalleVenta extends Modelo<DetalleVenta>{
    private String nombre;
    private int cantidad;
    private float precioVenta;
    private Producto producto;

    //#region Constructors

    public DetalleVenta() {
        this.dao = FabricaDao.fabricar("DetalleVentaDao");
        this.mapper = new ModelMapper();
        mapper.getConfiguration().setFieldMatchingEnabled(true).setFieldAccessLevel(AccessLevel.PRIVATE);
    }

    public DetalleVenta(DetalleVentaBuilder builder) {
        this();
        this.setNombre(builder.nombre);
        this.setCantidad(builder.cantidad);
        this.setPrecioVenta(builder.precioVenta);
        this.setProducto(builder.producto);
    }
    
    //#endregion

    //#region DetalleVentaBuilder

    public static class DetalleVentaBuilder {
        private String nombre;
        private int cantidad;
        private float precioVenta;
        private Producto producto;

        public static DetalleVentaBuilder getBuilder() {
            return new DetalleVentaBuilder();
        }

        public DetalleVentaBuilder conNombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public DetalleVentaBuilder conCantidad(int cantidad) {
            this.cantidad = cantidad;
            return this;
        }

        public DetalleVentaBuilder conPrecioVenta(float precioVenta) {
            this.precioVenta = precioVenta;
            return this;
        }

        public DetalleVentaBuilder conProducto(Producto producto) {
            this.producto = producto;
            return this;
        }

        public DetalleVenta build() {
            return new DetalleVenta(this);
        }
    }

    //#endregion

    //#region Getters y Setters
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setPrecioVenta(float precioVenta) {
        this.precioVenta = precioVenta;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public float getPrecioVenta() {
        return precioVenta;
    }

    public Producto getProducto() {
        return producto;
    }

    //#endregion
    
    //#region Business Methods

    public float calcularSubtotal() {
        return this.cantidad * this.getPrecioVenta();
    }
    //#endregion

    //#region AccesMethods
    @Override
    public List<DetalleVenta> listar() {
        try {
            List<DetalleVentaDto> listado = this.dao.listarTodos();
            if (!listado.isEmpty())
                return Arrays.asList(this.mapper.map(listado, DetalleVenta[].class));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<DetalleVenta> buscar(List<String> params) {
        try {
            DetalleVentaDto detalleVentaDto = this.mapper.map(this, DetalleVentaDto.class);
            List<DetalleVentaDto> detalleVentasDto = this.dao.buscar(detalleVentaDto, params);
            if (!detalleVentasDto.isEmpty())
                return Arrays.asList(this.mapper.map(detalleVentasDto, DetalleVenta[].class));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public DetalleVenta registrar() {
        try {
            DetalleVentaDto detalleVentaDto = this.mapper.map(this, DetalleVentaDto.class);
            detalleVentaDto = (DetalleVentaDto) this.dao.actualizar(detalleVentaDto, null);
            if(detalleVentaDto != null)
                return this.mapper.map(detalleVentaDto, DetalleVenta.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public DetalleVenta actualizar(List<String> params) {
        try {
            DetalleVentaDto detalleVentaDto = this.mapper.map(this, DetalleVentaDto.class);
            detalleVentaDto = (DetalleVentaDto) this.dao.actualizar(detalleVentaDto, params);
            if(detalleVentaDto != null)
                return this.mapper.map(detalleVentaDto, DetalleVenta.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public DetalleVenta eliminar() {
        try {
            DetalleVentaDto detalleVentaDto = this.mapper.map(this, DetalleVentaDto.class);
            detalleVentaDto = (DetalleVentaDto) this.dao.borrar(detalleVentaDto);
            if(detalleVentaDto != null)
                return this.mapper.map(detalleVentaDto, DetalleVenta.class);;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    //#endregion
}