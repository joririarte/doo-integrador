package com.ventas.factories;

import java.lang.reflect.InvocationTargetException;

import com.ventas.dao.Dao;

public class FabricaDao {
    public static Dao fabricar(String nombreClase) {
        Dao dao = null;
        try {               
            dao = (Dao) Class.forName(Dao.class.getPackage().getName() + "." + nombreClase)
                    .getDeclaredConstructor()
                    .newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
            System.out.println(e.toString());
        }
        return dao;
    }
}
