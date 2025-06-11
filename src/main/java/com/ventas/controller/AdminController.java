package com.ventas.controller;

import com.ventas.dto.ProductoDto;
import com.ventas.model.Producto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class AdminController {

  @FXML private TableView<Producto> tablaProductos;
  @FXML private TableColumn<Producto, String> nombreColumn;
  @FXML private TableColumn<Producto, String> marcaColumn;
  @FXML private TableColumn<Producto, String> codigoBarrasColumn;
  @FXML private TableColumn<Producto, Double> precioColumn;
  @FXML private TableColumn<Producto, Double> stockColumn;
  @FXML private TableColumn<Producto, Void> accionColumn;
  @FXML private Button btnRegistrarProducto;

  private final Producto productoService = new Producto(); // Instancia para acceder a listarProductos

  @FXML
  public void initialize() {
    configurarColumnas();
    cargarProductos();

    tablaProductos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    btnRegistrarProducto.setOnAction(e -> abrirFormularioProducto());
  }

  private void configurarColumnas() {

    nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
    marcaColumn.setCellValueFactory(new PropertyValueFactory<>("marca"));
    codigoBarrasColumn.setCellValueFactory(new PropertyValueFactory<>("codigoBarras"));
    
     // Mostrar el primer precio de la lista de precios
    precioColumn.setCellValueFactory(cellData -> {
        Producto producto = cellData.getValue();
        if (producto.getPrecio() != null && !producto.getPrecio().isEmpty()) {
            double valor = producto.getPrecio().get(0).getMonto();
            return new javafx.beans.property.SimpleDoubleProperty(valor).asObject();
        }
        return new javafx.beans.property.SimpleDoubleProperty(0).asObject();
    });

    // Mostrar el primer stock de la lista de stock
    stockColumn.setCellValueFactory(cellData -> {
        Producto producto = cellData.getValue();
        if (producto.getStock() != null && !producto.getStock().isEmpty()) {
            double cantidad = producto.getStock().get(0).getCantidad(); // Asegúrate que getCantidad() devuelve int
            return new javafx.beans.property.SimpleDoubleProperty(cantidad).asObject();
        }
        return new javafx.beans.property.SimpleDoubleProperty(0).asObject();
    });
    
    accionColumn.setCellFactory(col -> new TableCell<>() {
      private final Button btnEditar = new Button("Editar");
      {
        btnEditar.setOnAction(event -> {
        Producto producto = getTableView().getItems().get(getIndex());
        abrirFormularioProducto(producto);
        });
      }

      @Override
      protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
          setGraphic(null);
        } else {
          HBox contenedor = new HBox(5, btnEditar);
          setGraphic(contenedor);
        }
      }
    });
  }

  private void cargarProductos() {
    ObservableList<Producto> productos = FXCollections.observableArrayList();

    List<Producto> lista = productoService.listarProductos();

    if (lista != null) {
        productos.addAll(lista);
        for (Producto p : lista) {
            String nombre = p.getNombre();
            String marca = p.getMarca();
            String codigo = p.getCodigoBarras();
            double monto = (p.getPrecio() != null && !p.getPrecio().isEmpty()) ? p.getPrecio().get(0).getMonto() : 0;
            double stock = (p.getStock() != null && !p.getStock().isEmpty()) ? p.getStock().get(0).getCantidad() : 0;

            System.out.printf("[DEBUG] Producto: %s | Marca: %s | Código: %s | Precio: %.2f | Stock: %.2f%n",
                    nombre, marca, codigo, monto, stock);
        }
    } else {
        System.out.println("[WARN] La lista de productos está vacía o ocurrió un error.");
    }

    tablaProductos.setItems(productos);
  }



  private void abrirFormularioProducto() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/producto.fxml"));
      Parent root = loader.load();

      Stage stage = new Stage();
      stage.setScene(new Scene(root));
      stage.setTitle("Registrar Producto");
      stage.initModality(Modality.APPLICATION_MODAL);
      stage.showAndWait();

      cargarProductos();

    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  private void abrirFormularioProducto(Producto producto) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/producto.fxml"));
      Parent root = loader.load();

      ProductoFormularioController controller = loader.getController();
      controller.setProducto(producto);

      Stage stage = new Stage();
      stage.setScene(new Scene(root));
      stage.setTitle(producto == null ? "Registrar Producto" : "Editar Producto");
      stage.initModality(Modality.APPLICATION_MODAL);
      stage.showAndWait();

      cargarProductos();

    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }
}
