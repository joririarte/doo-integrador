package com.ventas.db;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DBInitializer {

    private static final String DB_NAME = "sistema.db";

    public static void main(String[] args) {
        inicializarBaseDeDatos();
    }

    public static void inicializarBaseDeDatos() {
        File dbFile = new File(DB_NAME);
        if (dbFile.exists()) {
            System.out.println("La base de datos ya existe. No se hará nada.");
            return;
        }

        String url = "jdbc:sqlite:" + DB_NAME;

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            String crearUsuarios = "CREATE TABLE IF NOT EXISTS usuarios (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "user TEXT NOT NULL UNIQUE," +
                    "password TEXT NOT NULL," +
                    "rol TEXT NOT NULL)";

            String crearProductos = "CREATE TABLE IF NOT EXISTS productos (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nombre TEXT NOT NULL," +
                    "marca TEXT," +
                    "codigo_barras TEXT," +
                    "precio REAL NOT NULL," +
                    "stock INTEGER NOT NULL)";

            String crearVentas = "CREATE TABLE IF NOT EXISTS ventas (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "fecha TEXT NOT NULL," +
                    "total REAL NOT NULL)";

            String crearDetalleVentas = "CREATE TABLE IF NOT EXISTS detalle_ventas (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "venta_id INTEGER," +
                    "producto_id INTEGER," +
                    "cantidad INTEGER," +
                    "subtotal REAL," +
                    "FOREIGN KEY (venta_id) REFERENCES ventas(id)," +
                    "FOREIGN KEY (producto_id) REFERENCES productos(id))";

            String insertarUsuarios = "INSERT INTO usuarios (user, password, rol) VALUES " +
                    "('admin', 'admin123', 'Admin')," +
                    "('cajero', 'cajero123', 'Cajero')";

            String insertarProductos = "INSERT INTO productos (nombre, marca, codigo_barras, precio, stock) VALUES " +
                    "('Coca-Cola', 'Coca-Cola Company', '1234567890123', 250.0, 100)," +
                    "('Fideos', 'Matarazzo', '9876543210987', 180.0, 200)";

            stmt.execute(crearUsuarios);
            stmt.execute(crearProductos);
            stmt.execute(crearVentas);
            stmt.execute(crearDetalleVentas);
            stmt.execute(insertarUsuarios);
            stmt.execute(insertarProductos);

            System.out.println("Base de datos creada con tablas y datos iniciales.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
