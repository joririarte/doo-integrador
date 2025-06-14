package com.ventas.controller;

import com.ventas.model.DescuentoRecargo;
import com.ventas.model.DetalleVenta;
import com.ventas.model.Venta;
import com.ventas.model.Venta.VentaBuilder;
import com.ventas.util.CommonUtils;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;


public class VerVentasController {

    @FXML private TableView<Venta> tablaVentas;
    @FXML private TableColumn<Venta, String> codigoColumn;
    @FXML private TableColumn<Venta, String> vendedorColumn;
    @FXML private TableColumn<Venta, String> fechaColumn;
    @FXML private TableColumn<Venta, String> estadoColumn;
    @FXML private TableColumn<Venta, Float> montoPagadoColumn;
    @FXML private TableColumn<Venta, Float> totalColumn;
    @FXML private TableColumn<Venta, Float> vueltoColumn;
    @FXML private TableColumn<Venta, String> medioPagoColumn;
    @FXML private TableColumn<Venta, String> descuentoRecargoColumn;
    @FXML private TableColumn<Venta, String> clienteColumn;

    @FXML private TableView<DetalleVenta> tablaDetalleVenta;
    @FXML private TableColumn<DetalleVenta, String> productoColumn;
    @FXML private TableColumn<DetalleVenta, String> marcaColumn;
    @FXML private TableColumn<DetalleVenta, Float> precioColumn;
    @FXML private TableColumn<DetalleVenta, Integer> cantidadColumn;
    @FXML private TableColumn<DetalleVenta, Float> subtotalColumn;

    @FXML
    public void initialize() {
        configurarTablaVentas();
        inicializarTablaVentas();
        configurarTablaDetalleVenta();
    }

    @FXML
    public void seleccionarVenta(MouseEvent event){
        Venta ventaSeleccionada = tablaVentas.getSelectionModel().getSelectedItem();
        if (event.getClickCount() == 2 && ventaSeleccionada != null) {
            inicializarTablaDetalleVentas(ventaSeleccionada);
        }
    }
    
    private void inicializarTablaVentas() {
        codigoColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCodigoVenta()));
        vendedorColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getVendedor().getLegajo()));
        fechaColumn.setCellValueFactory(data -> {
            Date fecha = data.getValue().getFecha();
            return new SimpleStringProperty(CommonUtils.dateToString(fecha));
        });
        estadoColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEstado()));
        medioPagoColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMedioPago().getNombre()));
        descuentoRecargoColumn.setCellValueFactory(data -> new SimpleStringProperty(
            data.getValue().getDescuentoRecargo() != null ? 
                data.getValue().getDescuentoRecargo().getNombre() : 
                "SIN D/R"
            )
        );
        clienteColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCliente().getNroCliente()));

        montoPagadoColumn.setCellValueFactory(
            data -> new SimpleFloatProperty(
                data.getValue()
                    .getMontoPagado()
            ).asObject()
        );
        
        totalColumn.setCellValueFactory(
            data ->  new SimpleFloatProperty(
                data.getValue()
                    .calcularMontoTotal()
            ).asObject()
        );
        
        vueltoColumn.setCellValueFactory(
            data -> new SimpleFloatProperty(
                data.getValue()
                    .calcularVuelto(
                        data.getValue()
                            .calcularMontoTotal()
                    )
            ).asObject()
        );

        // Cargar datos
        Venta ventaService = VentaBuilder.getBuilder().build();
        List<Venta> ventas = ventaService.listarVentas();
        tablaVentas.setItems(FXCollections.observableArrayList(ventas));
    }

    private void configurarTablaVentas() {
        // Distribuir ancho proporcional
        tablaVentas.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Habilitar redimensionamiento manual
        codigoColumn.setResizable(true);
        vendedorColumn.setResizable(true);
        fechaColumn.setResizable(true);
        estadoColumn.setResizable(true);
        montoPagadoColumn.setResizable(true);
        medioPagoColumn.setResizable(true);
        descuentoRecargoColumn.setResizable(true);
        clienteColumn.setResizable(true);
    }

    private void configurarTablaDetalleVenta() {
        // Establecer anchos preferidos para cada columna
        productoColumn.setPrefWidth(150);
        marcaColumn.setPrefWidth(100);
        precioColumn.setPrefWidth(80);
        cantidadColumn.setPrefWidth(80);
        subtotalColumn.setPrefWidth(100);

        // Permitir que el usuario pueda redimensionar cada columna
        productoColumn.setResizable(true);
        marcaColumn.setResizable(true);
        precioColumn.setResizable(true);
        cantidadColumn.setResizable(true);
        subtotalColumn.setResizable(true);

        // Hacer que las columnas se ajusten proporcionalmente al ancho total de la tabla
        tablaDetalleVenta.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void inicializarTablaDetalleVentas(Venta venta){
        tablaDetalleVenta.setItems(FXCollections.observableArrayList(venta.getDetalleVenta()));
        // Configurar columnas de la tabla
        productoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        marcaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProducto().getMarca()));
        precioColumn.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getPrecioVenta()).asObject());
        cantidadColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getCantidad()).asObject());
        subtotalColumn.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().calcularSubtotal()).asObject());

    }

}