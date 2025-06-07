package com.ventas.db;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DBVentasInitializer {

    private static final String DB_NAME = "sistema.db";

    public static void main(String[] args) {
        inicializarBaseDeDatosVentas();
    }

    public static void inicializarBaseDeDatosVentas() {
        File dbFile = new File(DB_NAME);
        if (!dbFile.exists()) {
            System.out.println("La base de datos no existe. Ejecutá primero DBInitializer para crear usuarios.");
            return;
        }

        String url = "jdbc:sqlite:" + DB_NAME;

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            // Tabla productos
            String crearTablaProducto = "CREATE TABLE IF NOT EXISTS producto (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nombre TEXT NOT NULL," +
                    "marca TEXT NOT NULL," +
                    "precio REAL NOT NULL," +
                    "stock INTEGER NOT NULL," +
                    "codigo_barras TEXT UNIQUE NOT NULL)";

            // Tabla ventas
            String crearTablaVenta = "CREATE TABLE IF NOT EXISTS venta (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "fecha TEXT NOT NULL," +
                    "medio_pago TEXT NOT NULL)";

            // Tabla detalle_venta
            String crearTablaDetalle = "CREATE TABLE IF NOT EXISTS detalle_venta (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "id_venta INTEGER NOT NULL," +
                    "id_producto INTEGER NOT NULL," +
                    "cantidad INTEGER NOT NULL," +
                    "subtotal REAL NOT NULL," +
                    "FOREIGN KEY(id_venta) REFERENCES venta(id)," +
                    "FOREIGN KEY(id_producto) REFERENCES producto(id))";

            // Insertar productos de prueba
            String insertarProductos = "INSERT INTO producto (nombre, marca, precio, stock, codigo_barras) VALUES " +
                    "('Coca-Cola 1.5L', 'Coca-Cola', 2.50, 50, '1234567890123')," +
                    "('Pan Bimbo', 'Bimbo', 1.20, 30, '9876543210987')," +
                    "('Yerba Mate', 'Taragüi', 4.00, 40, '5555555555555')";

            stmt.execute(crearTablaProducto);
            stmt.execute(crearTablaVenta);
            stmt.execute(crearTablaDetalle);
            stmt.execute(insertarProductos);

            System.out.println("Tablas de productos y ventas creadas correctamente con productos iniciales.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
