package com.ventas.controller;

// import javafx.beans.property.SimpleDoubleProperty;
// import javafx.beans.property.SimpleIntegerProperty;
// import javafx.beans.property.SimpleStringProperty;
// import javafx.collections.FXCollections;
// import javafx.collections.ObservableList;
// import javafx.fxml.FXML;
// import javafx.fxml.FXMLLoader;
// import javafx.scene.Scene;
// import javafx.scene.control.*;
// import javafx.scene.control.cell.PropertyValueFactory;
// import javafx.stage.Stage;

// import java.io.IOException;
// import java.sql.*;

// import com.ventas.dto.DetalleVentaDto;
// import com.ventas.dto.ProductoDto;
// import com.ventas.dto.VentaDto;

 public class VerVentasController {

 }

//     @FXML private TableView<VentaDto> tablaVentas;
//     @FXML private TableColumn<VentaDto, Integer> idColumn;
//     @FXML private TableColumn<VentaDto, String> fechaColumn;
//     @FXML private TableColumn<VentaDto, Double> totalColumn;

//     @FXML private TableView<DetalleVentaDto> tablaDetalles;
//     @FXML private TableColumn<DetalleVentaDto, String> productoColumn;
//     @FXML private TableColumn<DetalleVentaDto, String> marcaColumn;
//     @FXML private TableColumn<DetalleVentaDto, Double> precioColumn;
//     @FXML private TableColumn<DetalleVentaDto, Integer> cantidadColumn;
//     @FXML private TableColumn<DetalleVentaDto, Double> subtotalColumn;

//     @FXML private Button btnVolver;

//     private final String DB_URL = "jdbc:sqlite:sistema.db";

//     @FXML
//     public void initialize() {
//         configurarColumnas();
//         cargarVentas();

//         tablaVentas.getSelectionModel().selectedItemProperty().addListener((obs, old, nuevaVenta) -> {
//             if (nuevaVenta != null) {
//                 cargarDetallesVenta(nuevaVenta.getId());
//             }
//         });

//         btnVolver.setOnAction(e -> volverAlSelector());
//     }

//     private void configurarColumnas() {
//         idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
//         fechaColumn.setCellValueFactory(new PropertyValueFactory<>("fecha"));
//         totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));
//         productoColumn.setCellValueFactory(new PropertyValueFactory<>("producto"));
//         marcaColumn.setCellValueFactory(new PropertyValueFactory<>("marca"));
//         precioColumn.setCellValueFactory(new PropertyValueFactory<>("precio"));
//         cantidadColumn.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
//         subtotalColumn.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

//     }

//     private void cargarVentas() {
//         ObservableList<VentaDto> ventas = FXCollections.observableArrayList();

//         String query = "SELECT * FROM ventas ORDER BY fecha DESC";

//         try (Connection conn = DriverManager.getConnection(DB_URL);
//              Statement stmt = conn.createStatement();
//              ResultSet rs = stmt.executeQuery(query)) {

//             while (rs.next()) {
//                 VentaDto venta = new VentaDto(
//                         rs.getInt("id"),
//                         rs.getString("fecha"),
//                         rs.getDouble("total")
//                 );
//                 ventas.add(venta);
//             }

//             tablaVentas.setItems(ventas);

//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//     }

//     private void cargarDetallesVenta(int idVenta) {
//         ObservableList<DetalleVentaDto> detalles = FXCollections.observableArrayList();

//         String query = "SELECT dv.*, p.nombre, p.marca, p.precio " +
//                 "FROM detalle_ventas dv JOIN productos p ON dv.producto_id = p.id " +
//                 "WHERE dv.venta_id = ?";

//         try (Connection conn = DriverManager.getConnection(DB_URL);
//              PreparedStatement pstmt = conn.prepareStatement(query)) {

//             pstmt.setInt(1, idVenta);
//             ResultSet rs = pstmt.executeQuery();

//             while (rs.next()) {
//                 ProductoDto producto = new ProductoDto(
//                         rs.getInt("producto_id"),
//                         rs.getString("nombre"),
//                         rs.getString("marca"),
//                         null,
//                         rs.getDouble("precio"),
//                         0
//                 );

//                 DetalleVentaDto item = new DetalleVentaDto(producto, rs.getInt("cantidad"));
//                 detalles.add(item);
//             }

//             tablaDetalles.setItems(detalles);

//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//     }

//     private void volverAlSelector() {
//         try {
//             FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin_selector.fxml"));
//             Stage stage = (Stage) btnVolver.getScene().getWindow();
//             stage.setScene(new Scene(loader.load()));
//             stage.setTitle("Selector Admin");
//         } catch (IOException ex) {
//             ex.printStackTrace();
//         }
//     }
// }
