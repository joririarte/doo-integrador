package com.ventas.controller;

import java.util.Date;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.ventas.model.Precio;
import com.ventas.model.Producto;
import com.ventas.model.Producto.ProductoBuilder;
import com.ventas.model.Stock.StockBuilder;
import com.ventas.model.Stock;
import com.ventas.model.Precio.PrecioBuilder;

public class ProductoFormularioController {

    @FXML private TextField nombreField, marcaField, codigoBarrasField, precioField, stockField;

    private Producto productoExistente = null;
    Boolean modoEdicion = false; // bandera para saber si estoy en modo edicion o modo registro

    public void setProducto(Producto producto) {
        this.productoExistente = producto;
        

        if (producto != null) {
            System.out.println("[DEBUG] Modo edición. Producto existente: " + producto);
            
            this.modoEdicion = true;
            
            nombreField.setText(producto.getNombre());
            marcaField.setText(producto.getMarca());
            codigoBarrasField.setText(producto.getCodigoBarras());

            if (producto.getPrecio() != null && !producto.getPrecio().isEmpty()) {
                System.out.println("[DEBUG] Precio actual: " + producto.getPrecio().get(0).getMonto());
                precioField.setText(String.valueOf(producto.getPrecio().get(0).getMonto()));
            } else {
                precioField.setText("");
            }

            if (producto.getStock() != null && !producto.getStock().isEmpty()) {
                System.out.println("[DEBUG] Stock actual: " + producto.getStock().get(0).getCantidad());
                stockField.setText(String.valueOf(producto.getStock().get(0).getCantidad()));
            } else {
                stockField.setText("");
            }
        } else {
            System.out.println("[DEBUG] Modo registro. Producto nuevo.");
        }
    }

    @FXML
    private void guardarProducto() {
        String nombre = nombreField.getText().trim();
        String marca = marcaField.getText().trim();
        String codigoBarras = codigoBarrasField.getText().trim();
        String precioStr = precioField.getText().trim();
        String stockStr = stockField.getText().trim();

        System.out.println("[DEBUG] Datos ingresados: nombre=" + nombre + ", marca=" + marca +
                           ", codigoBarras=" + codigoBarras + ", precio=" + precioStr + ", stock=" + stockStr);

        if (nombre.isEmpty() || marca.isEmpty() || codigoBarras.isEmpty() || precioStr.isEmpty() || stockStr.isEmpty()) {
            System.out.println("[DEBUG] Campos incompletos.");
            mostrarAlerta("Error", "Por favor complete todos los campos.");
            return;
        }

        float precioValor;
        float stockCantidad;

        try {
            precioValor = Float.parseFloat(precioStr);
            stockCantidad = Float.parseFloat(stockStr);
        } catch (NumberFormatException e) {
            System.out.println("[DEBUG] Error de formato en precio o stock.");
            mostrarAlerta("Error", "Precio y stock deben ser números válidos.");
            return;
        }

        System.out.println("[DEBUG] ¿Es nuevo? " + !this.modoEdicion);
        try{
            Boolean resultado;
            if (this.modoEdicion) {
                System.out.println("[DEBUG] Actualizando producto existente con ID: " + this.productoExistente.getCodigoBarras());
                resultado = this.editarProducto(nombre, marca, precioValor, stockCantidad);   
            } else {
                System.out.println("[DEBUG] Registrando nuevo producto...");
                resultado = this.registrarProducto(nombre, marca, codigoBarras, precioValor, stockCantidad);
            }

            System.out.println("[DEBUG] Resultado de guardar: " + (resultado != null ? "Éxito" : "Fallo"));

            if (this.productoExistente != null) {
                mostrarAlerta("Éxito", !this.modoEdicion ? "Producto registrado correctamente." : "Producto actualizado correctamente.");
                Stage stage = (Stage) nombreField.getScene().getWindow();
                stage.close();
            }
        }
        catch(Exception e){
            mostrarAlerta("Error", "No se pudo guardar el producto.");
        }
    }

    @FXML
    private void cancelar() {
        System.out.println("[DEBUG] Cancelando operación...");
        Stage stage = (Stage) nombreField.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        System.out.println("[DEBUG] Mostrando alerta: " + titulo + " - " + mensaje);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private Producto buscarPorCodigoBarras(String codigoBarras){
        System.out.println("[DEBUG] Buscando producto por código de barras: " + codigoBarras);
        Producto p = ProductoBuilder.getBuilder()
                                    .conCodigoBarras(codigoBarras)
                                    .build();
        List<Producto> listado = p.buscarProducto(Arrays.asList("codigoBarras"));

        if (listado != null && !listado.isEmpty()){
            p = listado.get(0);
            System.out.println("[DEBUG] Producto encontrado: " + p);
            return p;
        }
        System.out.println("[DEBUG] No se encontró producto con ese código de barras.");
        return null;
    }

    private Boolean editarProducto(String nombre, String marca, float precio, float cantidad){
        List<String> params = new ArrayList<>();
        if(!this.productoExistente.getNombre().equals(nombre)){
            this.productoExistente.setNombre(nombre);
            params.add("nombre");
        }
        if(!this.productoExistente.getMarca().equals(marca)){
            this.productoExistente.setMarca(marca);
            params.add("marca");
        }
        if(precio != this.productoExistente.getLastPrecio()){
            //creo un nuevo precio
            //no me interesa la lista anterior de precios quiero insertar uno nuevo
            List<Precio> p = Arrays.asList(PrecioBuilder.getBuilder()
                                                        .conMonto(precio)
                                                        .conFecha(new Date())
                                                        .build());
            this.productoExistente.setPrecio(p);
            params.add("precio");
        }
        if(cantidad != this.productoExistente.getLastStock()){
            //creo un nuevo stock
            //no me interesa la lista anterior de stock quiero insertar uno nuevo
            List<Stock> s = Arrays.asList(StockBuilder.getBuilder()
                                                      .conCantidad(cantidad)
                                                      .conFecha(new Date())
                                                      .build());
            this.productoExistente.setStock(s);
            params.add("stock");
        }

        this.productoExistente = this.productoExistente.actualizarProducto(params);
        return this.productoExistente != null;
    }

    private Boolean registrarProducto(String nombre, String marca,String codigoBarras, float precio, float cantidad){
        Producto producto = ProductoBuilder.getBuilder()
                                           .conNombre(nombre)
                                           .conMarca(marca)
                                           .conCodigoBarras(codigoBarras)
                                           .build();

            Precio nuevoPrecio = PrecioBuilder.getBuilder()
                                                     .conMonto(precio)
                                                     .conFecha(new Date())
                                                     .build();

            Stock nuevoStock = StockBuilder.getBuilder()
                                           .conCantidad(cantidad)
                                           .conFecha(new Date())
                                           .build();

            producto.setPrecio(Arrays.asList(nuevoPrecio));
            producto.setStock(Arrays.asList(nuevoStock));

            this.productoExistente = producto.registrarProducto();
        return producto != null;
    }

}
