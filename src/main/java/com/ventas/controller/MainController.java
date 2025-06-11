package com.ventas.controller;

import com.ventas.model.AppContext;
import com.ventas.model.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.Node;

import java.io.IOException;

public class MainController {

    @FXML private StackPane contenidoPane;
    @FXML private Label usuarioLabel;

    @FXML
    public void initialize() {
        Usuario user = AppContext.getUsuarioActual();
        if (user != null) {
            System.out.println("[INFO] Usuario recuperado: " + user.getUsername());
            System.out.println("[INFO] Rol del usuario: " + user.getEmpleado().getCargo());
            usuarioLabel.setText("Usuario: " + user.getUsername() + " | Rol: " + user.getEmpleado().getCargo());
        }
        mostrarInicio(); // Por defecto al iniciar
    }

    public void mostrarInicio() {
        cargarVista("/fxml/admin.fxml");
    }

    public void mostrarVentas() {
        if (usuarioTienePermiso( "Administrador")) {
            cargarVista("/fxml/venta.fxml");
        }else {
            mostrarAlertaPermisoDenegado();
        }
    }

    public void mostrarProductos() {
        if (usuarioTienePermiso("Administrador")) {
            cargarVista("/fxml/admin.fxml");
        }else {
            mostrarAlertaPermisoDenegado();
        }
    }

    public void mostrarRegistrarVenta() {
        if (usuarioTienePermiso("Cajero")) {
            cargarVista("/fxml/cajero.fxml");
        }else {
            mostrarAlertaPermisoDenegado();
        }
    }

    public void cerrarSesion() {
        AppContext.setUsuarioActual(null);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            contenidoPane.getScene().setRoot(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cargarVista(String rutaFXML) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFXML));
            Node vista = loader.load();
            contenidoPane.getChildren().setAll(vista);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean usuarioTienePermiso(String... rolesPermitidos) {
        Usuario user = AppContext.getUsuarioActual();
        if (user == null) return false;
        String rol = user.getEmpleado().getCargo();
        for (String permitido : rolesPermitidos) {
            if (permitido.equals(rol)) return true;
        }
        return false;
    }

    private void mostrarAlertaPermisoDenegado() {
        javafx.scene.control.Alert alerta = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
        alerta.setTitle("Acceso Denegado");
        alerta.setHeaderText(null);
        alerta.setContentText("No tienes permiso para acceder a esta sección.");
        alerta.showAndWait();
    }

}
