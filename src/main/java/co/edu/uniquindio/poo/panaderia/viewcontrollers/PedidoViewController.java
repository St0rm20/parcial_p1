package co.edu.uniquindio.poo.panaderia.viewcontrollers;

import co.edu.uniquindio.poo.panaderia.app.Aplicacion;
import co.edu.uniquindio.poo.panaderia.controller.EmpleadoController;
import co.edu.uniquindio.poo.panaderia.controller.PedidoController;
import co.edu.uniquindio.poo.panaderia.model.Empleado;
import co.edu.uniquindio.poo.panaderia.model.Panadero;
import co.edu.uniquindio.poo.panaderia.model.Pedido;
import co.edu.uniquindio.poo.panaderia.model.TipoPan;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class PedidoViewController {

    private Aplicacion aplicacion;
    private PedidoController pedidoController;
    private EmpleadoController empleadoController;

    @FXML private TextField txtCodigo;
    @FXML private TextField txtCliente;
    @FXML private ComboBox<TipoPan> cbTipoPan;
    @FXML private TextField txtCantidad;

    @FXML private TableView<Pedido> tblPedidos;
    @FXML private TableColumn<Pedido, String> colCodigo;
    @FXML private TableColumn<Pedido, String> colCliente;
    @FXML private TableColumn<Pedido, String> colTipoPan;
    @FXML private TableColumn<Pedido, Integer> colCantidad;
    @FXML private TableColumn<Pedido, String> colEstado;
    @FXML private TableColumn<Pedido, String> colEmpleado;
    @FXML private TableColumn<Pedido, Integer> colMinutos;

    @FXML private TableView<Empleado> tblEmpleados;
    @FXML private TableColumn<Empleado, String> colEmpCodigo;
    @FXML private TableColumn<Empleado, String> colEmpNombre;
    @FXML private TableColumn<Empleado, String> colEmpTipo;
    @FXML private TableColumn<Empleado, Integer> colEmpMinutos;

    @FXML
    void initialize() {
        cbTipoPan.setItems(FXCollections.observableArrayList(TipoPan.values()));
        cbTipoPan.getSelectionModel().selectFirst();

        colCodigo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCodigo()));
        colCliente.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNombreCliente()));
        colTipoPan.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTipoPan().name()));
        colCantidad.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getCantidad()));
        colEstado.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEstado().name()));
        colEmpleado.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getCodigoEmpleadoAsignado() == null ? "-" : c.getValue().getCodigoEmpleadoAsignado()));
        colMinutos.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getMinutosReservados()));

        colEmpCodigo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCodigo()));
        colEmpNombre.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNombre()));
        colEmpTipo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTipo()));
        colEmpMinutos.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getMinutosDisponibles()));

        // al seleccionar un pedido se copia su codigo para asignar o cancelar
        tblPedidos.getSelectionModel().selectedItemProperty().addListener((obs, anterior, nuevo) -> {
            if (nuevo != null) {
                txtCodigo.setText(nuevo.getCodigo());
            }
        });
    }

    public void setApp(Aplicacion aplicacion) {
        this.aplicacion = aplicacion;
        this.pedidoController = new PedidoController(Aplicacion.panaderia);
        this.empleadoController = new EmpleadoController(Aplicacion.panaderia);
        refrescarTablas();
    }

    @FXML
    void registrarPedidoEvent(ActionEvent event) {
        String codigo = txtCodigo.getText().trim();
        String cliente = txtCliente.getText().trim();
        TipoPan tipoPan = cbTipoPan.getValue();
        if (codigo.isEmpty() || cliente.isEmpty() || tipoPan == null) {
            mostrarMensaje("Datos incompletos", "Debe diligenciar codigo, cliente y tipo de pan", Alert.AlertType.WARNING);
            return;
        }
        int cantidad;
        try {
            cantidad = Integer.parseInt(txtCantidad.getText().trim());
        } catch (NumberFormatException e) {
            mostrarMensaje("Dato invalido", "La cantidad debe ser un numero entero", Alert.AlertType.WARNING);
            return;
        }
        try {
            pedidoController.registrarPedido(codigo, cliente, tipoPan, cantidad);
            mostrarMensaje("Pedido registrado", "El pedido " + codigo + " quedo PENDIENTE", Alert.AlertType.INFORMATION);
            limpiarCampos();
            refrescarTablas();
        } catch (IllegalArgumentException e) {
            mostrarMensaje("No se pudo registrar", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void asignarPedidoEvent(ActionEvent event) {
        String codigo = obtenerCodigo();
        if (codigo == null) return;
        try {
            Panadero panadero = pedidoController.asignarPedido(codigo);
            mostrarMensaje("Pedido asignado", "El pedido " + codigo + " se asigno a " + panadero.getNombre()
                    + " (" + panadero.getTipo() + ")", Alert.AlertType.INFORMATION);
            refrescarTablas();
        } catch (IllegalArgumentException | IllegalStateException e) {
            mostrarMensaje("No se pudo asignar", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void cancelarPedidoEvent(ActionEvent event) {
        String codigo = obtenerCodigo();
        if (codigo == null) return;
        try {
            pedidoController.cancelarPedido(codigo);
            mostrarMensaje("Pedido cancelado", "El pedido " + codigo + " quedo CANCELADO", Alert.AlertType.INFORMATION);
            refrescarTablas();
        } catch (IllegalArgumentException | IllegalStateException e) {
            mostrarMensaje("No se pudo cancelar", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void volverEvent(ActionEvent event) {
        aplicacion.openViewInicio();
    }

    private String obtenerCodigo() {
        String codigo = txtCodigo.getText().trim();
        if (codigo.isEmpty()) {
            mostrarMensaje("Datos incompletos", "Ingrese o seleccione el codigo del pedido", Alert.AlertType.WARNING);
            return null;
        }
        return codigo;
    }

    private void refrescarTablas() {
        tblPedidos.setItems(FXCollections.observableArrayList(pedidoController.obtenerPedidos()));
        tblEmpleados.setItems(FXCollections.observableArrayList(empleadoController.obtenerEmpleados()));
        tblPedidos.refresh();
        tblEmpleados.refresh();
    }

    private void limpiarCampos() {
        txtCodigo.clear();
        txtCliente.clear();
        txtCantidad.clear();
    }

    public void mostrarMensaje(String titulo, String mensaje, Alert.AlertType alertType) {
        Alert alerta = new Alert(alertType);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
