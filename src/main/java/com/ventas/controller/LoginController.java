package com.ventas.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

import com.ventas.model.Usuario;

public class LoginController {
    @FXML private TextField usuarioField;
    @FXML private PasswordField contraseñaField;
    @FXML private Label mensajeLabel;

    @FXML
    public void onLogin() {
        String usuario = usuarioField.getText();
        String contraseña = contraseñaField.getText();

        Usuario user = new Usuario();

        if(user.iniciarSesion(usuario, contraseña)){

            String rol = user.getEmpleado().getCargo();

            try{
            if ("Administrador".equals(rol)) {
                // Abrir selector admin (productos o ventas)
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin_selector.fxml"));
                Stage stage = (Stage) usuarioField.getScene().getWindow();
                stage.setScene(new Scene(loader.load()));
                stage.setTitle("Seleccionar acción Admin");
            } else {
                String fxml = switch (rol) {
                case "Cajero" -> "/fxml/cajero.fxml";
                    default -> null;
                };

                if (fxml != null) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
                    Stage stage = (Stage) usuarioField.getScene().getWindow();
                    stage.setScene(new Scene(loader.load())); 
                } else {
                    mensajeLabel.setText("Rol no reconocido.");
                }
            }

        }
        catch(IOException ex){}
        }
        else {
            System.out.println("usuario no encontrado");
        }
    }
}
