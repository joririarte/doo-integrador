package com.ventas.model;

import java.util.Arrays;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;

import com.ventas.dto.DetalleVentaDto;
import com.ventas.factories.FabricaDao;

public class DetalleVenta extends Modelo{
    private String nombre;
    private int cantidad;
    private float descuentoRecargo;
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
        this.setDescuentoRecargo(builder.descuentoRecargo);
        this.setPrecioVenta(builder.precioVenta);
        this.setProducto(builder.producto);
    }
    
    //#endregion

    //#region DetalleVentaBuilder

    public static class DetalleVentaBuilder {
        private String nombre;
        private int cantidad;
        private float descuentoRecargo;
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

        public DetalleVentaBuilder conDescuentoRecargo(float descuentoRecargo) {
            this.descuentoRecargo = descuentoRecargo;
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

    public void setDescuentoRecargo(float descuentoRecargo) {
        this.descuentoRecargo = descuentoRecargo;
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

    public float getDescuentoRecargo() {
        return descuentoRecargo;
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
    
    public List<DetalleVenta> listarDetalleVentas() {
        try {
            List<DetalleVentaDto> listado = this.dao.listarTodos();
            if (!listado.isEmpty()) {
                List<DetalleVenta> listaDetalleVentas = Arrays.asList(this.mapper.map(listado, DetalleVenta[].class));
                return listaDetalleVentas;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public List<DetalleVenta> consultarDetalleVenta(List<String> params) {
        List<DetalleVenta> listadoDetalleVentas = null;
        try {
            DetalleVentaDto detalleVentaDto = this.mapper.map(this, DetalleVentaDto.class);
            List<DetalleVentaDto> detalleVentasDto = this.dao.buscar(detalleVentaDto, params);
            listadoDetalleVentas = Arrays.asList(this.mapper.map(detalleVentasDto, DetalleVenta[].class));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return listadoDetalleVentas;
    }

    public Boolean registrarDetalleVenta() {
        try {
            DetalleVentaDto detalleVentaDto = this.mapper.map(this, DetalleVentaDto.class);
            detalleVentaDto = (DetalleVentaDto) this.dao.actualizar(detalleVentaDto, null);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Boolean eliminarDetalleVenta() {
        try {
            DetalleVentaDto detalleVentaDto = this.mapper.map(this, DetalleVentaDto.class);
            detalleVentaDto = (DetalleVentaDto) this.dao.borrar(detalleVentaDto);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //#endregion
}