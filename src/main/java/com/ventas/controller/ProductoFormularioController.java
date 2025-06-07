package com.ventas.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import com.ventas.dto.ProductoDto;

public class ProductoFormularioController {

    @FXML private TextField nombreField, marcaField, codigoBarrasField, precioField, stockField;

    private ProductoDto productoExistente = null;

    public void setProducto(ProductoDto producto) {
        this.productoExistente = producto;

        if (producto != null) {
            // Modo edición: precargar los campos
            nombreField.setText(producto.getNombre());
            marcaField.setText(producto.getMarca());
            codigoBarrasField.setText(producto.getCodigoBarras());
            precioField.setText(String.valueOf(producto.getPrecio()));
            stockField.setText(String.valueOf(producto.getStock()));
        }
    }

    @FXML
    private void guardarProducto() {
        String nombre = nombreField.getText().trim();
        String marca = marcaField.getText().trim();
        String codigoBarras = codigoBarrasField.getText().trim();
        String precioStr = precioField.getText().trim();
        String stockStr = stockField.getText().trim();

        if (nombre.isEmpty() || marca.isEmpty() || codigoBarras.isEmpty() || precioStr.isEmpty() || stockStr.isEmpty()) {
            mostrarAlerta("Error", "Por favor complete todos los campos.");
            return;
        }

        double precio;
        int stock;

        try {
            precio = Double.parseDouble(precioStr);
            stock = Integer.parseInt(stockStr);
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Precio y stock deben ser números válidos.");
            return;
        }

        String url = "jdbc:sqlite:sistema.db";

        try (Connection conn = DriverManager.getConnection(url)) {
            PreparedStatement pstmt;

            if (productoExistente == null) {
                // Registro nuevo
                String sql = "INSERT INTO productos (nombre, marca, codigo_barras, precio, stock) VALUES (?, ?, ?, ?, ?)";
                pstmt = conn.prepareStatement(sql);
            } else {
                // Edición existente
                String sql = "UPDATE productos SET nombre=?, marca=?, codigo_barras=?, precio=?, stock=? WHERE id=?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(6, productoExistente.getId());
            }

            pstmt.setString(1, nombre);
            pstmt.setString(2, marca);
            pstmt.setString(3, codigoBarras);
            pstmt.setDouble(4, precio);
            pstmt.setInt(5, stock);
            pstmt.executeUpdate();

            mostrarAlerta("Éxito", productoExistente == null ? "Producto registrado correctamente." : "Producto actualizado correctamente.");

            // Cerrar la ventana
            Stage stage = (Stage) nombreField.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo guardar el producto.");
        }
    }

    @FXML
    private void cancelar() {
        Stage stage = (Stage) nombreField.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void volverAlSelector() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin_selector.fxml"));
            Stage stage = (Stage) nombreField.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Selector Admin");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
