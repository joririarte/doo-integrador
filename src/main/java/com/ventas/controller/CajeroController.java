// package com.ventas.controller;

// import javafx.beans.property.ReadOnlyObjectWrapper;
// import javafx.beans.property.ReadOnlyStringWrapper;
// import javafx.collections.FXCollections;
// import javafx.collections.ObservableList;
// import javafx.fxml.FXML;
// import javafx.scene.control.*;

// import java.sql.*;
// import java.time.LocalDate;
// import java.util.Optional;

// import com.ventas.model.DetalleVenta;
// import com.ventas.model.Producto;

// public class CajeroController {

//     @FXML private TextField codigoBarrasField;
//     @FXML private TextField nombreField;
//     @FXML private TextField marcaField;
//     @FXML private TextField precioField;
//     @FXML private TextField stockField;
//     @FXML private Spinner<Integer> cantidadSpinner;
//     @FXML private Button agregarButton;
//     @FXML private TableView<DetalleVenta> tablaVenta;
//     @FXML private TableColumn<DetalleVenta, String> productoColumn;
//     @FXML private TableColumn<DetalleVenta, String> marcaColumn;
//     @FXML private TableColumn<DetalleVenta, Double> precioColumn;
//     @FXML private TableColumn<DetalleVenta, Integer> cantidadColumn;
//     @FXML private TableColumn<DetalleVenta, Double> subtotalColumn;
//     @FXML private ComboBox<String> medioPagoComboBox;
//     @FXML private Button confirmarButton;
//     @FXML private Button cancelarButton;

//     private ObservableList<DetalleVenta> listaVenta = FXCollections.observableArrayList();
//     private Producto productoSeleccionado;

//     @FXML
//     public void initialize() {
//         cantidadSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));
//         productoColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getProducto()));
//         marcaColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getMarca()));
//         precioColumn.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getPrecio()));
//         cantidadColumn.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getCantidad()));
//         subtotalColumn.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getSubtotal()));
//         tablaVenta.setItems(listaVenta);

//         medioPagoComboBox.getItems().addAll("Efectivo", "Débito", "Crédito");
//         medioPagoComboBox.getSelectionModel().selectFirst();

//         codigoBarrasField.setOnAction(e -> buscarProducto());
//         agregarButton.setOnAction(e -> agregarProductoAVenta());
//         confirmarButton.setOnAction(e -> confirmarVenta());
//         cancelarButton.setOnAction(e -> cancelarVenta());
//     }

//     private void buscarProducto() {
//         String codigo = codigoBarrasField.getText().trim();
//         if (codigo.isEmpty()) return;
//         try {
//             Producto productoSeleccionado = new Producto();
//             if(productoSeleccionado.obtenerProducto(codigo)){
//                 nombreField.setText(productoSeleccionado.getNombre());
//                 marcaField.setText(productoSeleccionado.getMarca());
//                 precioField.setText(String.valueOf(productoSeleccionado.getPrecio()));
//                 stockField.setText(String.valueOf(productoSeleccionado.getStock()));
//             } else {
//                 mostrarAlerta("Producto no encontrado");
//                 limpiarCamposProducto();
//             }

//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//     }

//     private void agregarProductoAVenta() {
//         if (productoSeleccionado == null) return;

//         int cantidad = cantidadSpinner.getValue();
//         if (cantidad > productoSeleccionado.getStock()) {
//             mostrarAlerta("Stock insuficiente");
//             return;
//         }

//         listaVenta.add(new DetalleVentaDto(productoSeleccionado, cantidad));
//         limpiarCamposProducto();
//     }

//     private void confirmarVenta() {
//         if (listaVenta.isEmpty()) {
//             mostrarAlerta("No hay productos en la venta");
//             return;
//         }

//         try (Connection conn = DriverManager.getConnection(DB_URL)) {
//             conn.setAutoCommit(false);

//             double total = listaVenta.stream().mapToDouble(DetalleVentaDto::getSubtotal).sum();

//             String insertVenta = "INSERT INTO ventas (fecha, total) VALUES (?, ?)";
//             try (PreparedStatement ventaStmt = conn.prepareStatement(insertVenta, Statement.RETURN_GENERATED_KEYS)) {
//                 ventaStmt.setString(1, LocalDate.now().toString());
//                 ventaStmt.setDouble(2, total);
//                 ventaStmt.executeUpdate();

//                 ResultSet rs = ventaStmt.getGeneratedKeys();
//                 if (rs.next()) {
//                     int ventaId = rs.getInt(1);

//                     String insertDetalle = "INSERT INTO detalle_ventas (venta_id, producto_id, cantidad, subtotal) VALUES (?, ?, ?, ?)";
//                     try (PreparedStatement detalleStmt = conn.prepareStatement(insertDetalle)) {
//                         for (DetalleVentaDto item : listaVenta) {
//                             detalleStmt.setInt(1, ventaId);
//                             detalleStmt.setInt(2, item.getProductoObj().getId());
//                             detalleStmt.setInt(3, item.getCantidad());
//                             detalleStmt.setDouble(4, item.getSubtotal());
//                             detalleStmt.addBatch();

//                             Actualizar stock
//                             String updateStock = "UPDATE productos SET stock = stock - ? WHERE id = ?";
//                             try (PreparedStatement stockStmt = conn.prepareStatement(updateStock)) {
//                                 stockStmt.setInt(1, item.getCantidad());
//                                 stockStmt.setInt(2, item.getProductoObj().getId());
//                                 stockStmt.executeUpdate();
//                             }
//                         }
//                         detalleStmt.executeBatch();
//                     }
//                 }
//                 conn.commit();
//                 mostrarAlerta("Venta registrada con éxito.");
//                 listaVenta.clear();
//             } catch (SQLException e) {
//                 conn.rollback();
//                 throw e;
//             }

//         } catch (SQLException e) {
//             e.printStackTrace();
//             mostrarAlerta("Error al registrar la venta.");
//         }
//     }

//     private void cancelarVenta() {
//         listaVenta.clear();
//         limpiarCamposProducto();
//     }

//     private void limpiarCamposProducto() {
//         productoSeleccionado = null;
//         codigoBarrasField.clear();
//         nombreField.clear();
//         marcaField.clear();
//         precioField.clear();
//         stockField.clear();
//     }

//     private void mostrarAlerta(String mensaje) {
//         Alert alert = new Alert(Alert.AlertType.INFORMATION);
//         alert.setContentText(mensaje);
//         alert.showAndWait();
//     }
// }
