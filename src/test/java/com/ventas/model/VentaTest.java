package com.ventas.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;

public class VentaTest {

    @Test
    void calcularMontoTotal_y_vuelto() {
        Producto leche = Producto.ProductoBuilder.getBuilder()
                .conNombre("Leche Entera")
                .conMarca("Marca A")
                .build();
        DetalleVenta d1 = DetalleVenta.DetalleVentaBuilder.getBuilder()
                .conNombre(leche.getNombre())
                .conCantidad(2)
                .conPrecioVenta(1200f)
                .conProducto(leche)
                .build();

        Producto galletitas = Producto.ProductoBuilder.getBuilder()
                .conNombre("Galletitas Dulces")
                .conMarca("Marca B")
                .build();
        DetalleVenta d2 = DetalleVenta.DetalleVentaBuilder.getBuilder()
                .conNombre(galletitas.getNombre())
                .conCantidad(1)
                .conPrecioVenta(900f)
                .conProducto(galletitas)
                .build();

        List<DetalleVenta> detalle = new ArrayList<>();
        detalle.add(d1);
        detalle.add(d2);

        Venta venta = Venta.VentaBuilder.getBuilder()
                .conCodigoVenta("TEST-001")
                .conVendedor(new Empleado())
                .conCliente(new Cliente())
                .conEstado("Confirmada")
                .conFecha(new Date())
                .conDetalleVenta(detalle)
                .build();

        assertEquals(3300f, venta.calcularMontoTotal(), 0.001);

        venta.setMontoPagado(3500f);
        assertEquals(200f, venta.calcularVuelto(), 0.001);
    }


        @Test
        void calcularMontoTotal_listaNula () {
            Venta venta = Venta.VentaBuilder.getBuilder()
                    .conCodigoVenta("TEST-NULL")
                    .conDetalleVenta(null)
                    .build();

            assertThrows(NullPointerException.class, venta::calcularMontoTotal);
        }

        @Test
        void calcularMontoTotal_conDetalleNulo_en_lista () {
            List<DetalleVenta> detalle = new ArrayList<>();
            detalle.add(null);

            Venta venta = Venta.VentaBuilder.getBuilder()
                    .conCodigoVenta("TEST-NULL-ELEM")
                    .conDetalleVenta(detalle)
                    .build();

            assertThrows(NullPointerException.class, venta::calcularMontoTotal);
        }

        @Test
        void calcularMontoTotal_valor_falso () {
            Producto p = Producto.ProductoBuilder.getBuilder()
                    .conNombre("Test")
                    .build();
            DetalleVenta d = DetalleVenta.DetalleVentaBuilder.getBuilder()
                    .conNombre(p.getNombre())
                    .conCantidad(1)
                    .conPrecioVenta(100f)
                    .conProducto(p)
                    .build();

            List<DetalleVenta> detalle = new ArrayList<>();
            detalle.add(d);

            Venta venta = Venta.VentaBuilder.getBuilder()
                    .conCodigoVenta("TEST-VAL-FALSE")
                    .conDetalleVenta(detalle)
                    .build();

            assertFalse(venta.calcularMontoTotal() == 200f);
        }
    }

}

