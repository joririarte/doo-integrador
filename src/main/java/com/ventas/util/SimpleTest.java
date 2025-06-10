package com.ventas.util;

import java.util.Arrays;
import java.util.List;

import com.ventas.dto.UsuarioDto;
import com.ventas.model.Producto;
import com.ventas.model.Venta;
import com.ventas.model.Producto.ProductoBuilder;
import com.ventas.model.Usuario;
import com.ventas.model.Usuario.UsuarioBuilder;
import com.ventas.model.Venta.VentaBuilder;

public class SimpleTest {
    
    public static void main(String[] args) {
        
        System.out.println("-------------------- VENTA ------------------------");
        Venta venta = VentaBuilder.getBuilder()
                                  .conCodigoVenta("C")
                                  .build();

        List<Venta> listado = venta.consultarVenta(Arrays.asList("codigoVenta"));

        System.out.println(listado.getFirst().getEstado());
        System.out.println(listado.getFirst().getCliente().getNombreApellido());
        
        System.out.println("-------------------- PRODUCTO ------------------------");
        Producto p = ProductoBuilder.getBuilder()
                                    .conNombre("Galletitas Dulces").build();
        List<Producto> lista = p.buscarProducto(Arrays.asList("nombre"));

        System.out.println(lista.getFirst().getMarca());
        System.out.println(lista.getFirst().getPrecio().getLast().getMonto());

        System.out.println("-------------------- USUARIO ------------------------");
        Usuario user = UsuarioBuilder.getBuilder().conUsername("admin").conPassword("admin123").build();
        user.iniciarSesion();
        System.out.println(user.getUltimoAcceso());
        System.out.println(user.getEmpleado().getCUIT());
    }
}
