package com.ventas.db;

import java.sql.*;
import java.io.File;
import java.nio.file.*;

public class DBInitializerV2 {
    private static final String DB_NAME = "SistemaVentas.db";
    private static final String SCRIPT_NAME ="src/main/resources/sql/SistemaVentaScript.sql"; 
    public static void main(String[] args) {

        File dbFile = new File(DB_NAME);
        if (!dbFile.exists()) {
            System.out.println("La base de datos no existe. Ejecutá primero DBInitializer para crear usuarios.");
            return;
        }

        String url = "jdbc:sqlite:" + DB_NAME;

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                // Leer todo el archivo SQL como texto
                String script = Files.readString(Path.of(SCRIPT_NAME));

                // Separar sentencias por ';' y ejecutar una por una
                Statement stmt = conn.createStatement();
                for (String sentencia : script.split(";")) {
                    sentencia = sentencia.trim();
                    if (!sentencia.isEmpty()) {
                        stmt.execute(sentencia);
                    }
                }

                System.out.println("Script ejecutado con éxito.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
