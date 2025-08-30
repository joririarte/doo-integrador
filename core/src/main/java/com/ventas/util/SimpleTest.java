package com.ventas.util;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.ventas.dto.UsuarioDto;
import com.ventas.model.Cliente;
import com.ventas.model.DescuentoRecargo;
import com.ventas.model.DetalleVenta;
import com.ventas.model.Empleado;
import com.ventas.model.MedioPago;
import com.ventas.model.Producto;
import com.ventas.model.Venta;
import com.ventas.model.Cliente.ClienteBuilder;
import com.ventas.model.DetalleVenta.DetalleVentaBuilder;
import com.ventas.model.Empleado.EmpleadoBuilder;
import com.ventas.model.MedioPago.MedioPagoBuilder;
import com.ventas.model.Producto.ProductoBuilder;
import com.ventas.model.Usuario;
import com.ventas.model.Usuario.UsuarioBuilder;
import com.ventas.model.Venta.VentaBuilder;

public class SimpleTest {
    
    public static void main(String[] args) {

        Venta v = VentaBuilder.getBuilder().build();
        List<Venta> ls = v.listar();
        
        MedioPago mp = MedioPagoBuilder.getBuilder().conHabilitado(true).build();
        List<MedioPago> listadoMedioPagos = mp.listar();
        
        for(MedioPago m : listadoMedioPagos){
            System.out.println(m.getNombre());
            if(m.getDescuentoRecargo() != null && !m.getDescuentoRecargo().isEmpty()){
                System.out.println(m.getDescuentoRecargo().getFirst().getNombre());
            }
        }
        
        Cliente cliente = ClienteBuilder.getBuilder().conNroCliente("CL001").build();
        cliente = cliente.buscar(Arrays.asList("nroCliente")).getFirst();

        Empleado vendedor = EmpleadoBuilder.getBuilder().conLegajo("LEG-002").build();
        vendedor = vendedor.buscar(Arrays.asList("legajo")).getFirst();

        
        System.out.println("-------------------- VENTA ------------------------");
        Venta venta = VentaBuilder.getBuilder()
                                  .conCliente(cliente)
                                  .conVendedor(vendedor)
                                  .conEstado("NUEVA")
                                  .conFecha(new Date())
                                  .conMedioPago(listadoMedioPagos.getFirst())
                                  .build();
        
        
        venta.setCodigoVenta(venta.getNextCodigoVenta());
        System.out.println("CODIGO VENTA: " + venta.getCodigoVenta());
        
        
        Producto p = ProductoBuilder.getBuilder()
                                     .conNombre("Galletitas Dulces").build();
        p = p.buscar(Arrays.asList("nombre")).getFirst();
        
        DetalleVenta detalleVenta = DetalleVentaBuilder.getBuilder()
                                                       .conProducto(p)
                                                       .conCantidad(2)
                                                       .conNombre(p.getNombre())
                                                       .conPrecioVenta(p.getLastPrecio())
                                                       .build();
        venta.agregarDetalleVenta(detalleVenta);

        //Hay que listar los descuento recargo disponibles y elegir uno
        List<DescuentoRecargo> descuentoRecargosDisponibles = venta.getMedioPago().getDescuentoRecargo();
        //elegimos el primer
        DescuentoRecargo descuentoRecargo = descuentoRecargosDisponibles.getFirst();
        venta.setDescuentoRecargo(descuentoRecargo);

        Float total = venta.calcularMontoTotal();
        System.out.println("Total de la venta: " + total);

        //Se setea un monto pagado
        venta.setEstado("EN COBRO");
        boolean result = venta.cobrar(total + 468);
        if(result){
            venta.setEstado("PAGADA");
            //se calcula el vuelto
            float vuelto = venta.calcularVuelto(total);
            System.out.println("Vuelto: " + vuelto);
            venta.setEstado("CONFIRMADA");
            venta = venta.registrar();
        }
        else{
            venta.setEstado("CANCELADA");
            System.out.println("falló el cobro");
        }




        
        System.out.println("-------------------- PRODUCTO ------------------------");
        Producto pr = ProductoBuilder.getBuilder()
                                    .conNombre("Galletitas Dulces").build();
        List<Producto> lista = pr.buscar(Arrays.asList("nombre"));

        System.out.println(lista.getFirst().getMarca());
        System.out.println(lista.getFirst().getStock().getFirst().getCantidad());

        System.out.println("-------------------- USUARIO ------------------------");
        Usuario user = UsuarioBuilder.getBuilder().conUsername("admin").conPassword("admin123").build();
        user = user.iniciarSesion();
        System.out.println(user.getUltimoAcceso());
        System.out.println(user.getEmpleado().getCUIT());
    }
}
