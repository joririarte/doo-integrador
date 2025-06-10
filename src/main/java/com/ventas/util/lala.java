package com.ventas.util;

import java.util.Arrays;
import java.util.List;

import com.ventas.model.Producto;
import com.ventas.model.Venta;
import com.ventas.model.Producto.ProductoBuilder;
import com.ventas.model.Venta.VentaBuilder;

public class lala {
    
    public static void main(String[] args) {
        
        Venta venta = VentaBuilder.getBuilder()
                                  .conEstado("Confirmada")
                                  .conFecha(CommonUtils.stringToDate("2025-06-08"))
                                  .build();

        List<Venta> listado = venta.consultarVenta(Arrays.asList("estado"));

        System.out.println(listado.getFirst().getEstado());
        System.out.println(listado.getFirst().getCliente().getNombreApellido());

        // Producto p = ProductoBuilder.getBuilder()
        //                             .conNombre("Galletitas Dulces").build();
        // List<Producto> lista = p.buscarProducto(Arrays.asList("nombre"));

        // System.out.println(lista.getFirst().getMarca());
    }
}
