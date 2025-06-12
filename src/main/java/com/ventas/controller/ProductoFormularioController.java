package com.ventas.controller;

import java.util.Date;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.ventas.model.Precio;
import com.ventas.model.Producto;
import com.ventas.model.Producto.ProductoBuilder;
import com.ventas.model.Stock;

public class ProductoFormularioController {

    @FXML private TextField nombreField, marcaField, codigoBarrasField, precioField, stockField;

    private Producto productoExistente = null;

    public void setProducto(Producto producto) {
        this.productoExistente = producto;

        if (producto != null) {
            System.out.println("[DEBUG] Modo edición. Producto existente: " + producto);

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

        Producto producto;
        boolean esNuevo = (productoExistente == null);
        System.out.println("[DEBUG] ¿Es nuevo? " + esNuevo);

        if (!esNuevo) {
            producto = productoExistente;

            Precio precioExistente = !producto.getPrecio().isEmpty() ? producto.getPrecio().get(0) : null;
            Stock stockExistente = !producto.getStock().isEmpty() ? producto.getStock().get(0) : null;

            System.out.println("[DEBUG] Actualizando producto existente con ID: " + producto.getCodigoBarras());

            Precio precioActualizado = Precio.PrecioBuilder.getBuilder()
                .conMonto(precioValor)
                .conFecha(new Date())
                .conMonto(precioExistente != null ? precioExistente.getMonto() : null)
                .build();

            Stock stockActualizado = Stock.StockBuilder.getBuilder()
                .conCantidad(stockCantidad)
                .conFecha(new Date())
                .conCantidad(stockExistente != null ? stockExistente.getCantidad() : null)
                .build();

            producto.setNombre(nombre);
            producto.setMarca(marca);
            producto.setCodigoBarras(codigoBarras);
            producto.setPrecio(Arrays.asList(precioActualizado));
            producto.setStock(Arrays.asList(stockActualizado));

        } else {
            System.out.println("[DEBUG] Registrando nuevo producto...");
            producto = new Producto();
            producto.setNombre(nombre);
            producto.setMarca(marca);
            producto.setCodigoBarras(codigoBarras);

            Precio nuevoPrecio = Precio.PrecioBuilder.getBuilder()
                .conMonto(precioValor)
                .conFecha(new Date())
                .build();

            Stock nuevoStock = Stock.StockBuilder.getBuilder()
                .conCantidad(stockCantidad)
                .conFecha(new Date())
                .build();

            producto.setPrecio(Arrays.asList(nuevoPrecio));
            producto.setStock(Arrays.asList(nuevoStock));
        }

        Producto resultado = esNuevo
            ? producto.registrarProducto()
            : producto.actualizarProducto(Arrays.asList("nombre", "monto", "cantidad"));

        System.out.println("[DEBUG] Resultado de guardar: " + (resultado != null ? "Éxito" : "Fallo"));

        if (resultado != null) {
            mostrarAlerta("Éxito", esNuevo ? "Producto registrado correctamente." : "Producto actualizado correctamente.");
            Stage stage = (Stage) nombreField.getScene().getWindow();
            stage.close();
        } else {
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

}
