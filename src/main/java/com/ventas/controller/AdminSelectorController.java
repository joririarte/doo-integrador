package com.ventas.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminSelectorController {

    @FXML private Button btnProductos;
    @FXML private Button btnVentas;

    @FXML
    public void initialize() {
        btnProductos.setOnAction(e -> abrirVista("/fxml/admin.fxml", "Administrar Productos"));
        btnVentas.setOnAction(e -> abrirVista("/fxml/venta.fxml", "Ventas"));
    }

    private void abrirVista(String fxmlPath, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            // Obtiene el stage actual desde cualquier botón del selector
            Stage stage = (Stage) btnProductos.getScene().getWindow(); // o btnVentas.getScene().getWindow()
            stage.setScene(new Scene(loader.load()));
            stage.setTitle(titulo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
