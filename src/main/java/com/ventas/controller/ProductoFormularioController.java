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
            // Modo edición: precargar los campos con datos del producto
            nombreField.setText(producto.getNombre());
            marcaField.setText(producto.getMarca());
            codigoBarrasField.setText(producto.getCodigoBarras());

            if (producto.getPrecio() != null && !producto.getPrecio().isEmpty()) {
                precioField.setText(String.valueOf(producto.getPrecio().get(0).getMonto()));
            } else {
                precioField.setText("");
            }

            if (producto.getStock() != null && !producto.getStock().isEmpty()) {
                stockField.setText(String.valueOf(producto.getStock().get(0).getCantidad()));
            } else {
                stockField.setText("");
            }
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

        float precioValor;
        float stockCantidad;

        try {
            precioValor = Float.parseFloat(precioStr);
            stockCantidad = Float.parseFloat(stockStr);
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Precio y stock deben ser números válidos.");
            return;
        }

        // Buscar producto existente
        Producto productoExistenteDB = this.buscarPorCodigoBarras(codigoBarras);
        Producto producto;

        if (productoExistenteDB != null) {
            producto = productoExistenteDB;

            // Extraer precio y stock existentes
            Precio precioExistente = !producto.getPrecio().isEmpty() ? producto.getPrecio().get(0) : null;
            Stock stockExistente = !producto.getStock().isEmpty() ? producto.getStock().get(0) : null;

            // Actualizar precio preservando ID
            Precio precioActualizado = Precio.PrecioBuilder.getBuilder()
                .conMonto(precioValor)
                .conFecha(new Date())
                .conMonto(precioExistente != null ? precioExistente.getMonto() : null)
                .build();

            // Actualizar stock preservando ID y productoId
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
            producto = new Producto();
            producto.setNombre(nombre);
            producto.setMarca(marca);
            producto.setCodigoBarras(codigoBarras);

            // Nuevos
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

        // Registrar o actualizar
        Producto resultado = (productoExistenteDB == null)
            ? producto.registrarProducto()
            : producto.actualizarProducto(Collections.singletonList(codigoBarras));

        if (resultado != null) {
            mostrarAlerta("Éxito", productoExistenteDB == null ? "Producto registrado correctamente." : "Producto actualizado correctamente.");
            Stage stage = (Stage) nombreField.getScene().getWindow();
            stage.close();
        } else {
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


    private Producto buscarPorCodigoBarras(String codigoBarras){
        Producto p = ProductoBuilder.getBuilder()
                                    .conCodigoBarras(codigoBarras)
                                    .build();
        List<Producto> listado = p.buscarProducto(Arrays.asList("codigoBarras"));

        if (listado != null && !listado.isEmpty()){
            p = listado.get(0);
            return p;
        }
        return null;
    }

}
