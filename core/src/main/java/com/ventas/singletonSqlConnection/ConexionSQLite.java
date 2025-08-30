package com.ventas.singletonSqlConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionSQLite {
    private Connection conn;
    private static ConexionSQLite instance;
    private String URL;

    private ConexionSQLite() {
        URL = "jdbc:sqlite:../SistemaVentas.db";
        try {
            conn = DriverManager.getConnection(URL);
        } catch (SQLException e) {
            e.printStackTrace();
            conn = null; 
        }
    }

    public static ConexionSQLite getInstance() {
        if (instance == null) {
            instance = new ConexionSQLite();
        }
        return instance;
    }

    public Connection getConnection() {
        return conn;
    }

    public void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Conexión cerrada correctamente.");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // También podrías hacer log del error
            System.out.println("Error al cerrar la conexión.");
        }
    }

}
