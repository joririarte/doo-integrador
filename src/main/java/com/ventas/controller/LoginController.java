package com.ventas.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

import com.ventas.model.AppContext;
import com.ventas.model.Usuario;
import com.ventas.model.Usuario.UsuarioBuilder;

public class LoginController {
    @FXML private TextField usuarioField;
    @FXML private PasswordField contraseñaField;
    @FXML private Label mensajeLabel;

    @FXML
    public void onLogin() {
        String usuario = usuarioField.getText();
        String contraseña = contraseñaField.getText();

        System.out.println("Intentando login con usuario: " + usuario + " y contraseña: " + contraseña);

        Usuario user = UsuarioBuilder.getBuilder()
                                    .conUsername(usuario)
                                    .conPassword(contraseña)
                                    .build();
        user = user.iniciarSesion(); 

        if (user != null) {
            System.out.println("Login exitoso");
            String rol = user.getEmpleado().getCargo();
            System.out.println("Rol detectado: " + rol);

            AppContext.setUsuarioActual(user);

            try {
                if ("Administrador".equals(rol)) {
                    System.out.println("Cargando vista de administrador...");
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
                    Stage stage = (Stage) usuarioField.getScene().getWindow();
                    stage.setScene(new Scene(loader.load()));
                    stage.setTitle("Seleccionar acción Admin");
                } else {
                    String fxml = switch (rol) {
                        case "Cajero" -> "/fxml/main.fxml";
                        default -> null;
                    };

                    System.out.println("FXML para rol: " + fxml);

                    if (fxml != null) {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
                        Stage stage = (Stage) usuarioField.getScene().getWindow();
                        stage.setScene(new Scene(loader.load())); 
                        System.out.println("Vista cargada correctamente.");
                    } else {
                        mensajeLabel.setText("Rol no reconocido.");
                        System.out.println("Rol no reconocido: " + rol);
                    }
                }

            } catch (IOException ex) {
                System.out.println("Error cargando la vista: " + ex.getMessage());
                ex.printStackTrace();
            }

        } else {
            mensajeLabel.setText("Credenciales incorrectas.");
            System.out.println("Usuario no encontrado o credenciales incorrectas.");
        }
    }

}
