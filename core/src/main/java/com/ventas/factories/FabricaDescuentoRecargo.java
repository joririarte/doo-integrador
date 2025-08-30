package com.ventas.factories;

import java.lang.reflect.InvocationTargetException;

import com.ventas.dto.DescuentoRecargoDto;
import com.ventas.model.DescuentoRecargo;

public class FabricaDescuentoRecargo {
    public static DescuentoRecargo fabricar(String nombreClase) {
        DescuentoRecargo dr = null;
        try {               
            dr = (DescuentoRecargo) Class.forName(DescuentoRecargo.class.getPackage().getName() + "." + nombreClase)
                    .getDeclaredConstructor()
                    .newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
            System.out.println(e.toString());
        }
        return dr;
    }

    public static DescuentoRecargo fabricar(DescuentoRecargoDto drDto){
        DescuentoRecargo dr = fabricar(drDto.tipo);
        dr.setNombre(drDto.nombre);
        dr.setTipo(drDto.tipo);
        dr.setMonto(drDto.monto);
        dr.setFechaInicio(drDto.fechaInicio);
        dr.setFechaFin(drDto.fechaFin);
        dr.setHabilitado(drDto.habilitado);
        dr.setCodigoDescuentoRecargo(drDto.codigoDescuentoRecargo);
        return dr;
    }
}
