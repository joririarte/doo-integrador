package com.ventas.model;

import java.util.*;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;

import com.ventas.dto.VentaDto;
import com.ventas.factories.FabricaDao;
import com.ventas.model.MedioPago.MedioPagoBuilder;

public class Venta extends Modelo {
    private String codigoVenta;
    private Empleado vendedor;
    private Date fecha;
    private String estado;
    private float montoPagado;
    private MedioPago medioPago;
    private Cliente cliente;
    private List<DetalleVenta> detalleVenta;

    //#region Constructors
    public Venta() {
        this.dao = FabricaDao.fabricar("VentaDao");
        this.mapper = new ModelMapper();
        mapper.getConfiguration().setFieldMatchingEnabled(true).setFieldAccessLevel(AccessLevel.PRIVATE);
    }

    public Venta(VentaBuilder builder) {
        this(); // Llama al constructor por defecto
        this.setCodigoVenta(builder.codigoVenta);
        this.setVendedor(builder.vendedor);
        this.setFecha(builder.fecha);
        this.setEstado(builder.estado);
        this.setMontoPagado(builder.montoPagado);
        this.setMedioPago(builder.medioPago);
        this.setCliente(builder.cliente);
        if(builder.detalleVenta != null)
            this.setDetalleVenta(builder.detalleVenta);
        else
            this.detalleVenta = new ArrayList<>();
    }
    //#endregion

    //#region VentaBuilder

    public static class VentaBuilder {
        private String codigoVenta;
        private Empleado vendedor;
        private Date fecha;
        private String estado;
        private float montoPagado;
        private MedioPago medioPago;
        private Cliente cliente;
        private List<DetalleVenta> detalleVenta;

        public static VentaBuilder getBuilder() {
            return new VentaBuilder();
        }

        public VentaBuilder conCodigoVenta(String codigoVenta) {
            this.codigoVenta = codigoVenta;
            return this;
        }

        public VentaBuilder conVendedor(Empleado vendedor) {
            this.vendedor = vendedor;
            return this;
        }

        public VentaBuilder conFecha(Date fecha) {
            this.fecha = fecha;
            return this;
        }

        public VentaBuilder conEstado(String estado) {
            this.estado = estado;
            return this;
        }

        public VentaBuilder conMontoPagado(float montoPagado) {
            this.montoPagado = montoPagado;
            return this;
        }

        public VentaBuilder conMedioPago(MedioPago medioPago) {
            this.medioPago = medioPago;
            return this;
        }

        public VentaBuilder conCliente(Cliente cliente) {
            this.cliente = cliente;
            return this;
        }

        public VentaBuilder conDetalleVenta(List<DetalleVenta> detalleVenta) {
            this.detalleVenta = detalleVenta;
            return this;
        }

        public Venta build() {
            return new Venta(this);
        }
    }


    //#endregion

    //#region Getters y Setters

    public String getCodigoVenta() {
        return codigoVenta;
    }

    public void setCodigoVenta(String codigoVenta) {
        this.codigoVenta = codigoVenta;
    }

    public Empleado getVendedor() {
        return vendedor;
    }
    public void setVendedor(Empleado vendedor) {
        this.vendedor = vendedor;
    }
    public Date getFecha() {
        return fecha;
    }
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public float getMontoPagado() {
        return montoPagado;
    }
    public void setMontoPagado(float montoPagado) {
        this.montoPagado = montoPagado;
    }
    public MedioPago getMedioPago() {
        return medioPago;
    }
    public void setMedioPago(MedioPago medioPago) {
        this.medioPago = medioPago;
    }
    public Cliente getCliente() {
        return cliente;
    }
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    public List<DetalleVenta> getDetalleVenta() {
        return detalleVenta;
    }
    public void setDetalleVenta(List<DetalleVenta> detalleVenta) {
        this.detalleVenta = detalleVenta;
    }

    //#endregion

    //#region Business Methods
    
    public float calcularMontoTotal(DescuentoRecargo descuentoRecargo) { 
        float total = 0;
        for(DetalleVenta dv : this.detalleVenta){
            total += dv.calcularSubtotal();
        }
        if(descuentoRecargo != null){
            total = descuentoRecargo.aplicarPolitica(total);
        }
        return total; 
    }

    public float calcularVuelto(float totalVenta) { 
        return this.montoPagado > totalVenta ? this.montoPagado - totalVenta : 0; 
    }
    
    public void agregarDetalleVenta(DetalleVenta dv) {
        this.detalleVenta.add(dv);
    }
    
    public void obtenerMedioPago(MedioPago mp) { 
        this.setMedioPago(mp); 
    }
    
    public Boolean cobrar(float monto) {
        if(this.checkCobro()){
            this.setMontoPagado(monto);
            return true;
        }
        return false;
    }

    private boolean checkCobro() { 
        try {
            // Simular espera entre 1 y 3 segundos
            int delay = (int) (Math.random() * 2000) + 1000; // de 1000 a 3000 ms
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false; // en caso de interrupción, consideramos fallido
        }

        // Generar resultado aleatorio
        return Math.random() < 0.5;
    }

    public void cancelar() {
        this.estado = "cancelada";
    }

    public List<Venta> listarVentas() {
        try {
            List<VentaDto> listado = this.dao.listarTodos();
            if (!listado.isEmpty())
                return Arrays.asList(this.mapper.map(listado, Venta[].class));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<Venta> consultarVenta(List<String> params) {
        try {
            VentaDto ventaDto = this.mapper.map(this, VentaDto.class);
            List<VentaDto> ventasDto = this.dao.buscar(ventaDto, params);
            if (!ventasDto.isEmpty())
                return Arrays.asList(this.mapper.map(ventasDto, Venta[].class));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    public Venta registrarVenta() {
        try {
            VentaDto ventaDto = this.mapper.map(this, VentaDto.class);
            ventaDto = (VentaDto) this.dao.actualizar(ventaDto, null);
            if(ventaDto != null)
                return this.mapper.map(ventaDto, Venta.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    public Venta actualizararVenta(List<String> params) {
        try {
            VentaDto ventaDto = this.mapper.map(this, VentaDto.class);
            ventaDto = (VentaDto) this.dao.actualizar(ventaDto, params);
            if(ventaDto != null)
                return mapearVenta(ventaDto);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    public Venta eliminarVenta() {
        try {
            VentaDto ventaDto = this.mapper.map(this, VentaDto.class);
            ventaDto = (VentaDto) this.dao.borrar(ventaDto);
            if(ventaDto != null)
                return this.mapper.map(ventaDto, Venta.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    private Venta mapearVenta(VentaDto ventaDto){
        Cliente c = this.mapper.map(ventaDto.cliente, Cliente.class);
        Empleado e = this.mapper.map(ventaDto.vendedor, Empleado.class);
        MedioPago mp = MedioPagoBuilder.getBuilder().build().mapearMedioPago(ventaDto.medioPago);
        List<DetalleVenta> dv = Arrays.asList(this.mapper.map(ventaDto.detalleVenta, DetalleVenta[].class));
        Venta v = VentaBuilder.getBuilder()
                              .conCliente(c)
                              .conVendedor(e)
                              .conMedioPago(mp)
                              .conCodigoVenta(ventaDto.codigoVenta)
                              .conEstado(ventaDto.estado)
                              .conFecha(ventaDto.fecha)
                              .conDetalleVenta(dv)
                              .build();
        return v;
    }
    //#endregion
}