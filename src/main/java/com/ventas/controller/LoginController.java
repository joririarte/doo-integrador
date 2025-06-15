package com.ventas.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

import com.ventas.model.Usuario;
import com.ventas.model.Usuario.UsuarioBuilder;

public class LoginController {
    @FXML private TextField usuarioField;
    @FXML private PasswordField passwordField;
    @FXML private Label mensajeLabel;
    @FXML private Button iniciarSesionBtn;

    @FXML
    public void initialize(){
        iniciarSesionBtn.setDisable(false);
        mensajeLabel.setVisible(false);
        usuarioField.setEditable(true);
        passwordField.setEditable(true);
    }

    @FXML
    public void onLogin() {
        String usuario = usuarioField.getText();
        String password = passwordField.getText();
        toggleInputs();
        if(usuario.isEmpty() || password.isEmpty()){
            toggleInputs();
            mostrarAlerta("Debes completar los campos");
            return;
        }
        try{
            Usuario user = UsuarioBuilder.getBuilder()
                                        .conUsername(usuario)
                                        .conPassword(password)
                                        .build();
            user = user.iniciarSesion(); 

            if (user != null) {
                String rol = user.getEmpleado().getCargo();
                AppContext.setUsuarioActual(user);
                cargarVista("/fxml/main.fxml", "Dashboard - " + rol); 
            } 
            else {
                mostrarAlerta("Credenciales incorrectas.");
            }
        } 
        catch (IOException ex) {
            mostrarAlerta("Ocurrio un error");
            System.out.println("Error cargando la vista: " + ex.getMessage());
            ex.printStackTrace();
        }
        catch (Exception ex){
            mostrarAlerta("Ocurrio un error");
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
        finally{
            toggleInputs();
        }
    }

    private void cargarVista(String vista, String titulo) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource(vista));
        Stage stage = (Stage) usuarioField.getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
        stage.setTitle(titulo);
    }

    private void mostrarAlerta(String mensaje){
        mensajeLabel.setVisible(true);
        mensajeLabel.setText(mensaje);
    }

    private void toggleInputs(){
        usuarioField.setDisable(!usuarioField.isDisable());
        passwordField.setDisable(!passwordField.isDisable());
        iniciarSesionBtn.setDisable(!iniciarSesionBtn.isDisable());
    }
}
