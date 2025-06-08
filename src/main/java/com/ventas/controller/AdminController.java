// package com.ventas.controller;

// import javafx.collections.FXCollections;
// import javafx.collections.ObservableList;
// import javafx.fxml.FXML;
// import javafx.fxml.FXMLLoader;
// import javafx.scene.Parent;
// import javafx.scene.Scene;
// import javafx.scene.control.*;
// import javafx.scene.control.cell.PropertyValueFactory;
// import javafx.scene.layout.HBox;
// import javafx.stage.Modality;
// import javafx.stage.Stage;

// import java.io.IOException;
// import java.sql.*;

// import com.ventas.dto.ProductoDto;

// public class AdminController {

//     @FXML private TableView<ProductoDto> tablaProductos;
//     @FXML private TableColumn<ProductoDto, String> nombreColumn;
//     @FXML private TableColumn<ProductoDto, String> marcaColumn;
//     @FXML private TableColumn<ProductoDto, String> codigoBarrasColumn;
//     @FXML private TableColumn<ProductoDto, Double> precioColumn;
//     @FXML private TableColumn<ProductoDto, Integer> stockColumn;
//     @FXML private TableColumn<ProductoDto, Void> accionColumn;
//     @FXML private Button btnRegistrarProducto;

//     private final String DB_URL = "jdbc:sqlite:sistema.db";

//     @FXML
//     public void initialize() {
//         configurarColumnas();
//         cargarProductos();

//         btnRegistrarProducto.setOnAction(e -> abrirFormularioProducto());
//     }

//     private void configurarColumnas() {
//         nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
//         marcaColumn.setCellValueFactory(new PropertyValueFactory<>("marca"));
//         codigoBarrasColumn.setCellValueFactory(new PropertyValueFactory<>("codigoBarras"));
//         precioColumn.setCellValueFactory(new PropertyValueFactory<>("precio"));
//         stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));

//         // Columna de acciones (botón editar por ahora)
//         accionColumn.setCellFactory(col -> new TableCell<>() {
//             private final Button btnEditar = new Button("Editar");

//             {
//                 btnEditar.setOnAction(event -> {
//                     ProductoDto producto = getTableView().getItems().get(getIndex());
//                     abrirFormularioProducto(producto); 
//                 });
//             }

//             @Override
//             protected void updateItem(Void item, boolean empty) {
//                 super.updateItem(item, empty);
//                 if (empty) {
//                     setGraphic(null);
//                 } else {
//                     HBox contenedor = new HBox(5, btnEditar);
//                     setGraphic(contenedor);
//                 }
//             }
//         });
//     }

//     private void cargarProductos() {
//         ObservableList<ProductoDto> productos = FXCollections.observableArrayList();

//         String query = "SELECT * FROM productos";

//         try (Connection conn = DriverManager.getConnection(DB_URL);
//              Statement stmt = conn.createStatement();
//              ResultSet rs = stmt.executeQuery(query)) {

//             while (rs.next()) {
//                 ProductoDto p = new ProductoDto(
//                         rs.getInt("id"),
//                         rs.getString("nombre"),
//                         rs.getString("marca"),
//                         rs.getString("codigo_barras"),
//                         rs.getDouble("precio"),
//                         rs.getInt("stock")
//                 );
//                 productos.add(p);
//             }

//         } catch (SQLException e) {
//             e.printStackTrace();
//         }

//         tablaProductos.setItems(productos);
//     }

//     private void abrirFormularioProducto() {
//         try {
//             FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/producto.fxml"));

//             Parent root = loader.load();

//             Stage stage = new Stage();
//             stage.setScene(new Scene(root));
//             stage.setTitle("Registrar Producto");
//             stage.initModality(Modality.APPLICATION_MODAL);
//             stage.showAndWait();

//             // Después de cerrar el formulario, recargamos los productos
//             cargarProductos();

//         } catch (IOException ex) {
//             ex.printStackTrace();
//         }
//     }

//     private void abrirFormularioProducto(ProductoDto producto) {
//         try {
//             FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/producto.fxml"));
//             Parent root = loader.load();

//             ProductoFormularioController controller = loader.getController();
//             controller.setProducto(producto); // null para registrar, producto para editar

//             Stage stage = new Stage();
//             stage.setScene(new Scene(root));
//             stage.setTitle(producto == null ? "Registrar Producto" : "Editar Producto");
//             stage.initModality(Modality.APPLICATION_MODAL);
//             stage.showAndWait();

//             cargarProductos(); // refrescar la tabla después del cierre

//         } catch (IOException ex) {
//             ex.printStackTrace();
//         }
//     }

// }
