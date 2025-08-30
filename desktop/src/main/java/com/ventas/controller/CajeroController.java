package com.ventas.controller;

import com.ventas.factories.FabricaDescuentoRecargo;
import com.ventas.model.*;
import com.ventas.model.Cliente.ClienteBuilder;
import com.ventas.model.DetalleVenta.DetalleVentaBuilder;
import com.ventas.model.Empleado.EmpleadoBuilder;
import com.ventas.model.MedioPago.MedioPagoBuilder;
import com.ventas.model.Producto.ProductoBuilder;
import com.ventas.model.Venta.VentaBuilder;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.*;
import javafx.collections.transformation.FilteredList;
import javafx.util.StringConverter;


import java.util.*;
import java.util.stream.Collectors;

public class CajeroController {

    @FXML private ComboBox<Producto> comboProductos;
    @FXML private ComboBox<Cliente> comboClientes;

    @FXML private TextField txtCodigoBarras;
    @FXML private Spinner<Integer> cantidadSpinner;
    @FXML private TextField nombreField;
    @FXML private TextField marcaField;
    @FXML private TextField precioField;
    @FXML private TextField stockField;

    @FXML private TableView<DetalleVenta> tablaVenta;
    @FXML private TableColumn<DetalleVenta, String> productoColumn;
    @FXML private TableColumn<DetalleVenta, String> marcaColumn;
    @FXML private TableColumn<DetalleVenta, Float> precioColumn;
    @FXML private TableColumn<DetalleVenta, Integer> cantidadColumn;
    @FXML private TableColumn<DetalleVenta, Float> subtotalColumn;

    @FXML private Label lblTotal;
    @FXML private TextField txtMontoPagado;
    @FXML private ComboBox<MedioPago> cbxMedioPago;
    @FXML private ComboBox<DescuentoRecargo> cbxDescuentoRecargo;
    @FXML private Button btnConfirmarVenta;
    @FXML private Button btnCancelarVenta;
    @FXML private Button agregarButton;

    @FXML private Label lblNombreCliente;

    private Venta ventaActual;
    private ObservableList<DetalleVenta> detallesObservable;

    private Empleado empleadoLogueado = obtenerEmpleadoSesion();

    private Producto productoSeleccionado;

    @FXML
    public void initialize() {
        configurarTablaDetalleVenta();
        configurarComboProductos();
        configurarComboClientes();

        cantidadSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, 1));

        ventaActual = VentaBuilder.getBuilder()
                .conVendedor(empleadoLogueado)
                .conFecha(new Date())
                .conEstado("PENDIENTE")
                .conDetalleVenta(new ArrayList<>())
                .build();
        ventaActual.setCodigoVenta(ventaActual.getNextCodigoVenta());

        configurarComboMedioPagos();
        
        
        txtCodigoBarras.setOnAction(event -> cargarDatosProducto());
        agregarButton.setOnAction(event -> agregarProducto());

    }

    private void cargarDatosProducto() {
        String codigo = txtCodigoBarras.getText().trim();
        if (codigo.isEmpty()) return;

        productoSeleccionado = buscarPorCodigoBarras(codigo);

        if (productoSeleccionado != null) {
            nombreField.setText(productoSeleccionado.getNombre());
            marcaField.setText(productoSeleccionado.getMarca());
            precioField.setText(String.valueOf(productoSeleccionado.getLastPrecio()));
            stockField.setText(String.valueOf(productoSeleccionado.getLastStock()));
        } else {
            mostrarAlerta("Producto no encontrado");
            limpiarCamposProducto();
        }
    }

    private void configurarComboMedioPagos(){
        List<MedioPago> listadoMedioPagos = MedioPagoBuilder.getBuilder()
                                                            .conHabilitado(true)
                                                            .build()
                                                            .listar();

        ObservableList<MedioPago> mediosPago = FXCollections.observableArrayList(listadoMedioPagos);
        cbxMedioPago.setItems(mediosPago);
        
        cbxMedioPago.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(MedioPago item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNombre());
            }
        });
        cbxMedioPago.setButtonCell(cbxMedioPago.getCellFactory().call(null));
        cbxMedioPago.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                ventaActual.setMedioPago(newVal);
                configurarComboDescuentoRecargo(newVal);
            } else {
                cbxDescuentoRecargo.getItems().clear();
            }
        });

    }

    private void configurarComboDescuentoRecargo(MedioPago mp){
        ObservableList<DescuentoRecargo> descuentoRecargos = FXCollections.observableArrayList(mp.getDescuentoRecargo());
        cbxDescuentoRecargo.setItems(descuentoRecargos);
        
        cbxDescuentoRecargo.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(DescuentoRecargo item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNombre());
            }
        });
        cbxDescuentoRecargo.setButtonCell(cbxDescuentoRecargo.getCellFactory().call(null));
        cbxDescuentoRecargo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                ventaActual.setDescuentoRecargo(newVal);
                actualizarTotal();
            } else {
                cbxDescuentoRecargo.getItems().clear();
            }
        });
    }

    public void agregarProducto() {
        if (productoSeleccionado == null) {
            mostrarAlerta("Debe buscar y seleccionar un producto");
            return;
        }

        int cantidad = cantidadSpinner.getValue();
        if (cantidad <= 0 || cantidad > productoSeleccionado.getLastStock()) {
            mostrarAlerta("Cantidad inválida o supera el stock disponible");
            return;
        }

        DetalleVenta detalle = DetalleVentaBuilder.getBuilder()
                .conProducto(productoSeleccionado)
                .conCantidad(cantidad)
                .conNombre(productoSeleccionado.getNombre())
                .conPrecioVenta(productoSeleccionado.getLastPrecio())
                .build();

        ventaActual.agregarDetalleVenta(detalle);
        detallesObservable.add(detalle);

        actualizarTotal();
        limpiarCamposProducto();
    }

    private void limpiarCamposProducto() {
        txtCodigoBarras.clear();
        nombreField.clear();
        marcaField.clear();
        precioField.clear();
        stockField.clear();
        cantidadSpinner.getValueFactory().setValue(1);
        productoSeleccionado = null;
        comboProductos.getSelectionModel().clearSelection();
    }

    @FXML
    public void confirmarVenta() {
        try {
            ventaActual.setEstado("EN COBRO");
            if (ventaActual.getMedioPago() == null) {
                mostrarAlerta("Seleccione un medio de pago");
                return;
            }
        
            if (ventaActual.getDescuentoRecargo() == null && obtenerDescuentoRecargoDesdeCombo() != null) {
                mostrarAlerta("Seleccione Descuento/ Recargo");
                return;
            }
            
            float montoPagado = Float.parseFloat(txtMontoPagado.getText());
            
            if (ventaActual.cobrar(montoPagado)) {
                ventaActual.setEstado("PAGADA");
                ventaActual.setMontoPagado(montoPagado);
                float vuelto = ventaActual.calcularVuelto(ventaActual.calcularMontoTotal());
                ventaActual.setEstado("CONFIRMADA");

                Venta ventaFinal = ventaActual.registrar();

                if (ventaFinal != null) {
                    mostrarAlerta("Venta registrada con éxito. Vuelto: $" + vuelto);
                    limpiarFormulario();
                } else {
                    mostrarAlerta("Error al registrar la venta en base de datos");
                }

            } else {
                ventaActual.setEstado("CANCELADA");
                if(ventaActual.getMedioPago().getNombre().equals("Efectivo"))
                    mostrarAlerta("El monto pagado es insuficiente");
                else
                    mostrarAlerta("Ocurrio un error en el cobro, por favor intente de nuevo");
            }

        } catch (NumberFormatException ex) {
            mostrarAlerta("Monto pagado inválido");
        } catch (Exception ex) {
            mostrarAlerta("Error inesperado: " + ex.getMessage());
        }
    }

    @FXML
    public void cancelarVenta() {
        ventaActual.cancelar();
        mostrarAlerta("Venta cancelada");
        limpiarFormulario();
    }

    private void actualizarTotal() {
        float total = ventaActual.calcularMontoTotal();
        lblTotal.setText(String.format("Total: $%.2f", total));
    }

    private MedioPago obtenerMedioPagoDesdeCombo() {
        return cbxMedioPago.getValue();
    }

    private DescuentoRecargo obtenerDescuentoRecargoDesdeCombo() {
        return cbxDescuentoRecargo.getValue();
    }

    private void limpiarFormulario() {
        comboClientes.getSelectionModel().clearSelection();
        txtCodigoBarras.clear();
        nombreField.clear();
        marcaField.clear();
        precioField.clear();
        stockField.clear();
        cantidadSpinner.getValueFactory().setValue(1);
        txtMontoPagado.clear();
        cbxMedioPago.getSelectionModel().clearSelection();
        cbxDescuentoRecargo.getSelectionModel().clearSelection();
        detallesObservable.clear();
        lblTotal.setText("Total: $0.00");
        lblNombreCliente.setText("Cliente: -");

        ventaActual = VentaBuilder.getBuilder()
                .conVendedor(empleadoLogueado)
                .conFecha(new Date())
                .conEstado("pendiente")
                .conCliente(null)
                .conDetalleVenta(new ArrayList<>())
                .build();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private Empleado obtenerEmpleadoSesion() {
        Usuario user = AppContext.getUsuarioActual();
        if (user != null && user.getEmpleado() != null) {
            Empleado empleado = EmpleadoBuilder.getBuilder()
                    .conLegajo(user.getEmpleado().getLegajo())
                    .build();
            List<Empleado> resultado = empleado.buscar(List.of("legajo"));
            if (resultado != null && !resultado.isEmpty()) {
                return resultado.getFirst();
            }
        }
        mostrarAlerta("No se pudo recuperar el empleado logueado.");
        return null;
    }

    private Producto buscarPorCodigoBarras(String codigoBarras) {
        Producto p = ProductoBuilder.getBuilder()
                .conCodigoBarras(codigoBarras)
                .build();
        List<Producto> listado = p.buscar(List.of("codigoBarras"));
        return (listado != null && !listado.isEmpty()) ? listado.getFirst() : null;
    }

    private void configurarTablaDetalleVenta() {
        detallesObservable = FXCollections.observableArrayList();
        tablaVenta.setItems(detallesObservable);

        // Configurar columnas de la tabla
        productoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        marcaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProducto().getMarca()));
        precioColumn.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getPrecioVenta()).asObject());
        cantidadColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getCantidad()).asObject());
        subtotalColumn.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().calcularSubtotal()).asObject());
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
        tablaVenta.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void configurarComboProductos() {
        Producto clienteService = ProductoBuilder.getBuilder().build();
        List<Producto> todosProductos = clienteService.listar();
        comboProductos.setItems(FXCollections.observableArrayList(todosProductos));

        comboProductos.setCellFactory(lv -> new ListCell<Producto>() {
            @Override
            protected void updateItem(Producto item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNombre() + " (" + item.getMarca() + ") $" + item.getLastPrecio());
            }
        });

        comboProductos.setButtonCell(comboProductos.getCellFactory().call(null));

        comboProductos.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                txtCodigoBarras.setText(newVal.getCodigoBarras());
                cargarDatosProducto();
            } else {
                cbxDescuentoRecargo.getItems().clear();
            }
        });
    }

    private void configurarComboClientes() {
        Cliente clienteService = ClienteBuilder.getBuilder().build();
        List<Cliente> todosClientes = clienteService.listar();
        comboClientes.setItems(FXCollections.observableArrayList(todosClientes));

        comboClientes.setCellFactory(lv -> new ListCell<Cliente>() {
            @Override
            protected void updateItem(Cliente item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNombreApellido() + " (" + item.getCUIT() + ")");
            }
        });

        comboClientes.setButtonCell(comboClientes.getCellFactory().call(null));

        comboClientes.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                lblNombreCliente.setText("Cliente: " + newVal.getNroCliente());
                ventaActual.setCliente(newVal);
            } else {
                cbxDescuentoRecargo.getItems().clear();
            }
        });
    }
}
