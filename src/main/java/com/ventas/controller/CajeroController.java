package com.ventas.controller;

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

import java.util.*;

public class CajeroController {

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
    @FXML private Button btnConfirmarVenta;
    @FXML private Button btnCancelarVenta;
    @FXML private Button agregarButton;

    @FXML private TextField txtNroCliente;
    @FXML private Label lblNombreCliente;

    private Cliente clienteActual;
    private Venta ventaActual;
    private ObservableList<DetalleVenta> detallesObservable;

    private Empleado empleadoLogueado = obtenerEmpleadoSesion();

    private Producto productoSeleccionado;

    @FXML
    public void initialize() {
        detallesObservable = FXCollections.observableArrayList();
        tablaVenta.setItems(detallesObservable);

        // Configurar columnas de la tabla
        productoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        marcaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProducto().getMarca()));
        precioColumn.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getPrecioVenta()).asObject());
        cantidadColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getCantidad()).asObject());
        subtotalColumn.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().calcularSubtotal()).asObject());

        cantidadSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, 1));

        ventaActual = VentaBuilder.getBuilder()
                .conVendedor(empleadoLogueado)
                .conFecha(new Date())
                .conEstado("pendiente")
                .conCliente(null)
                .conDetalleVenta(new ArrayList<>())
                .build();

        List<MedioPago> listadoMedioPagos = MedioPagoBuilder.getBuilder()
                .conHabilitado(true)
                .build()
                .listarMedioPagos();

        ObservableList<MedioPago> mediosPago = FXCollections.observableArrayList(listadoMedioPagos);
        cbxMedioPago.setItems(mediosPago);

        txtCodigoBarras.setOnAction(event -> cargarDatosProducto());
        agregarButton.setOnAction(event -> agregarProducto());

        cbxMedioPago.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(MedioPago item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNombre());
            }
        });
        cbxMedioPago.setButtonCell(cbxMedioPago.getCellFactory().call(null));

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
    }

    @FXML
    public void confirmarVenta() {
        try {
            float montoPagado = Float.parseFloat(txtMontoPagado.getText());
            MedioPago medioPago = obtenerMedioPagoDesdeCombo();

            if (medioPago == null) {
                mostrarAlerta("Seleccione un medio de pago");
                return;
            }

            ventaActual.setMedioPago(medioPago);
            List<DescuentoRecargo> descuentoRecargos = medioPago.getDescuentoRecargo();
            DescuentoRecargo descuentoRecargo = (descuentoRecargos != null && !descuentoRecargos.isEmpty()) ? descuentoRecargos.getFirst() : null;

            float total = ventaActual.calcularMontoTotal(descuentoRecargo);
            ventaActual.setEstado("EN COBRO");


              System.out.println("[DEBUG] Monto: " + montoPagado);
            if (ventaActual.cobrar(montoPagado)) {
                ventaActual.setEstado("PAGADA");
                ventaActual.setMontoPagado(montoPagado);
                float vuelto = ventaActual.calcularVuelto(total);
                ventaActual.setEstado("CONFIRMADA");

                Venta ventaFinal = ventaActual.actualizararVenta(null);

                if (ventaFinal != null) {
                    mostrarAlerta("Venta registrada con éxito. Vuelto: $" + vuelto);
                    limpiarFormulario();
                } else {
                    mostrarAlerta("Error al registrar la venta en base de datos");
                }

            } else {
                ventaActual.setEstado("CANCELADA");
                mostrarAlerta("El monto pagado es insuficiente");
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

    @FXML
    public void buscarCliente() {
        String nroCliente = txtNroCliente.getText().trim();
        if (nroCliente.isEmpty()) {
            mostrarAlerta("Debe ingresar el número de cliente");
            return;
        }

        try {
            Cliente cliente = ClienteBuilder.getBuilder()
                    .conNroCliente(nroCliente)
                    .build();

            List<Cliente> resultado = cliente.consultarCliente(List.of("nroCliente"));
            if (resultado != null && !resultado.isEmpty()) {
                cliente = resultado.getFirst();
                clienteActual = cliente;
                lblNombreCliente.setText("Cliente: " + clienteActual.getNombreApellido());
                iniciarVentaConCliente(clienteActual);
            } else {
                mostrarAlerta("Cliente no encontrado");
                clienteActual = null;
                lblNombreCliente.setText("Cliente: -");
            }

        } catch (Exception e) {
            mostrarAlerta("Error al buscar cliente: " + e.getMessage());
            clienteActual = null;
            lblNombreCliente.setText("Cliente: -");
        }
    }

    private void iniciarVentaConCliente(Cliente cliente) {
        String codigoUnico = UUID.randomUUID().toString().substring(0, 8);

        ventaActual = VentaBuilder.getBuilder()
                .conCodigoVenta(codigoUnico)
                .conVendedor(empleadoLogueado)
                .conFecha(new Date())
                .conEstado("pendiente")
                .conCliente(cliente)
                .conDetalleVenta(new ArrayList<>())
                .build();
    }

    private void actualizarTotal() {
        float total = 0;
        for (DetalleVenta d : ventaActual.getDetalleVenta()) {
            total += d.calcularSubtotal();
        }
        lblTotal.setText(String.format("Total: $%.2f", total));
    }

    private MedioPago obtenerMedioPagoDesdeCombo() {
        return cbxMedioPago.getValue();
    }

    private void limpiarFormulario() {
        txtCodigoBarras.clear();
        nombreField.clear();
        marcaField.clear();
        precioField.clear();
        stockField.clear();
        cantidadSpinner.getValueFactory().setValue(1);
        txtMontoPagado.clear();
        cbxMedioPago.getSelectionModel().clearSelection();
        detallesObservable.clear();
        lblTotal.setText("Total: $0.00");
        lblNombreCliente.setText("Cliente: -");
        txtNroCliente.clear();

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
            List<Empleado> resultado = empleado.consultarEmpleado(List.of("legajo"));
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
        List<Producto> listado = p.buscarProducto(List.of("codigoBarras"));
        return (listado != null && !listado.isEmpty()) ? listado.getFirst() : null;
    }
}
