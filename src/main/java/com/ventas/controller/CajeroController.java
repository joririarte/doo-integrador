package com.ventas.controller;

import com.ventas.model.*;
import com.ventas.model.Cliente.ClienteBuilder;
import com.ventas.model.DetalleVenta.DetalleVentaBuilder;
import com.ventas.model.MedioPago.MedioPagoBuilder;
import com.ventas.model.Producto.ProductoBuilder;
import com.ventas.model.Venta.VentaBuilder;
import com.ventas.dao.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.*;

import java.util.*;

public class CajeroController {

    @FXML private TextField txtCodigoBarras;
    @FXML private TextField txtCantidad;
    @FXML private TableView<DetalleVenta> tablaVenta;
    @FXML private Label lblTotal;
    @FXML private TextField txtMontoPagado;
    @FXML private ComboBox<String> cbxMedioPago;
    @FXML private Button btnConfirmarVenta;
    @FXML private Button btnCancelarVenta;

    private Venta ventaActual;
    private ObservableList<DetalleVenta> detallesObservable;

    private Producto producto = new Producto();
    private Venta venta = new Venta();

    private Empleado empleadoLogueado = obtenerEmpleadoSesion();

    @FXML
    public void initialize() {
        detallesObservable = FXCollections.observableArrayList();
        tablaVenta.setItems(detallesObservable);

        this.ventaActual = new Venta.VentaBuilder()
            .conVendedor(empleadoLogueado)
            .conFecha(new Date())
            .conEstado("pendiente")
            .conCliente(null)
            .conDetalleVenta(new ArrayList<>())
            .build();

        cbxMedioPago.setItems(FXCollections.observableArrayList("Efectivo", "Tarjeta", "Transferencia"));
    }

    @FXML
    public void agregarProducto() {
        String codigo = txtCodigoBarras.getText();
        int cantidad;
        try {
            cantidad = Integer.parseInt(txtCantidad.getText());
            if (cantidad <= 0) {
                mostrarAlerta("La cantidad debe ser mayor que cero");
                return;
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Cantidad inválida");
            return;
        }
        
        Producto producto = this.buscarPorCodigoBarras(codigo);

        if (producto == null) {
            mostrarAlerta("Producto no encontrado");
            return;
        }

        DetalleVenta detalle = new DetalleVenta.DetalleVentaBuilder()
            .conProducto(producto)
            .conCantidad(cantidad)
            .conPrecioVenta(producto.getPrecio().get(producto.getPrecio().size() - 1).getMonto()) // obtener último precio
            .conNombre(producto.getNombre())
            .build();

        ventaActual.agregarDetalleVenta(detalle);
        detallesObservable.add(detalle);
        actualizarTotal();
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

            if (!ventaActual.cobrar(montoPagado)) {
                mostrarAlerta("El monto pagado es insuficiente");
                return;
            }

            ventaActual.setEstado("confirmada");
            Venta ventaRegistrada = venta.registrarVenta();

            if (ventaRegistrada != null) {
                mostrarAlerta("Venta registrada con éxito");
                limpiarFormulario();
            } else {
                mostrarAlerta("Error al registrar la venta");
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
        float total = 0;
        for (DetalleVenta d : ventaActual.getDetalleVenta()) {
            total += d.calcularSubtotal();
        }
        lblTotal.setText(String.format("Total: $%.2f", total));
    }

    private MedioPago obtenerMedioPagoDesdeCombo() {
        String seleccionado = cbxMedioPago.getValue();
        if (seleccionado == null) return null;

        MedioPago mp = new MedioPago();
        mp.setNombre(seleccionado);
        
        return mp;
    }

    private void limpiarFormulario() {
        txtCodigoBarras.clear();
        txtCantidad.clear();
        txtMontoPagado.clear();
        cbxMedioPago.getSelectionModel().clearSelection();
        detallesObservable.clear();
        lblTotal.setText("Total: $0.00");

        this.ventaActual = new Venta.VentaBuilder()
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
        // Implementa la lógica para obtener el empleado conectado desde la sesión o contexto
        return new Empleado(); // solo un ejemplo
    }

    private Producto buscarPorCodigoBarras(String codigoBarras){
        Producto p = ProductoBuilder.getBuilder()
                                    .conCodigoBarras(codigoBarras)
                                    .build();
        List<Producto> listado = p.buscarProducto(Arrays.asList("codigoBarras"));
        if(listado != null && !listado.isEmpty()){
            p = listado.getFirst();
            return p;
        }
        return null;
    }

    private Cliente buscarClientePorNroCliente(String nroCliente){
        Cliente c = ClienteBuilder.getBuilder()
                                  .conNroCliente(nroCliente)
                                  .build();
        List<Cliente> listado = c.consultarCliente(Arrays.asList("nroCliente"));
        if(listado != null && !listado.isEmpty()){
            c = listado.getFirst();
            return c;
        }
        return null;
    }

    private Venta nuevaVenta(String codigoVenta, Empleado vendedor, Cliente cliente ){
        Venta v = VentaBuilder.getBuilder()
                              .conCodigoVenta(codigoVenta)
                              .conVendedor(vendedor)
                              .conCliente(cliente)
                              .conFecha(new Date())
                              .conEstado("NUEVA")
                              .build();
        return v;
    }

    private DetalleVenta nuevoDetalleVenta(String nombre, int cantidad, Producto producto){
        DetalleVenta dv = DetalleVentaBuilder.getBuilder()
                                              .conNombre(nombre)
                                              .conCantidad(cantidad)
                                              .conProducto(producto)
                                              .build();
        float precioVenta = producto.getPrecio().getFirst().getMonto();
        dv.setPrecioVenta(precioVenta);
        return dv;
    }

    private List<MedioPago> listarMedioPagoHabilitado(){
        MedioPago mp = MedioPagoBuilder.getBuilder()
                                       .conHabilitado(true)
                                       .build();
        List<MedioPago> listado = mp.consultarMedioPago(Arrays.asList("habilitado"));
        return listado;                             
    }
}
