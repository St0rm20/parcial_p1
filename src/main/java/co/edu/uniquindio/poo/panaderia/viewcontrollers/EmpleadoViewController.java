package co.edu.uniquindio.poo.panaderia.viewcontrollers;

import co.edu.uniquindio.poo.panaderia.app.Aplicacion;
import co.edu.uniquindio.poo.panaderia.controller.EmpleadoController;
import co.edu.uniquindio.poo.panaderia.model.Cajero;
import co.edu.uniquindio.poo.panaderia.model.Empleado;
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

public class EmpleadoViewController {

    private Aplicacion aplicacion;
    private EmpleadoController empleadoController;

    @FXML private ComboBox<String> cbTipo;
    @FXML private TextField txtNombre;
    @FXML private TextField txtDocumento;
    @FXML private TextField txtCodigo;
    @FXML private TextField txtMinutos;
    @FXML private TextField txtCaja;

    @FXML private TableView<Empleado> tblEmpleados;
    @FXML private TableColumn<Empleado, String> colCodigo;
    @FXML private TableColumn<Empleado, String> colNombre;
    @FXML private TableColumn<Empleado, String> colDocumento;
    @FXML private TableColumn<Empleado, String> colTipo;
    @FXML private TableColumn<Empleado, Integer> colMinutos;
    @FXML private TableColumn<Empleado, String> colCaja;

    @FXML
    void initialize() {
        colCodigo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCodigo()));
        colNombre.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNombre()));
        colDocumento.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDocumento()));
        colTipo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTipo()));
        colMinutos.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getMinutosDisponibles()));
        colCaja.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue() instanceof Cajero ? ((Cajero) c.getValue()).getCaja() : "-"));

        cbTipo.valueProperty().addListener((obs, anterior, nuevo) ->
                txtCaja.setDisable(!EmpleadoController.CAJERO.equals(nuevo)));
    }

    public void setApp(Aplicacion aplicacion) {
        this.aplicacion = aplicacion;
        this.empleadoController = new EmpleadoController(Aplicacion.panaderia);
        cbTipo.setItems(FXCollections.observableArrayList(empleadoController.obtenerTiposEmpleado()));
        cbTipo.getSelectionModel().selectFirst();
        refrescarTabla();
    }

    @FXML
    void registrarEmpleadoEvent(ActionEvent event) {
        String tipo = cbTipo.getValue();
        String nombre = txtNombre.getText().trim();
        String documento = txtDocumento.getText().trim();
        String codigo = txtCodigo.getText().trim();
        String caja = txtCaja.getText().trim();

        if (tipo == null || nombre.isEmpty() || documento.isEmpty() || codigo.isEmpty()
                || (EmpleadoController.CAJERO.equals(tipo) && caja.isEmpty())) {
            mostrarMensaje("Datos incompletos", "Debe diligenciar todos los campos", Alert.AlertType.WARNING);
            return;
        }
        int minutos;
        try {
            minutos = Integer.parseInt(txtMinutos.getText().trim());
        } catch (NumberFormatException e) {
            mostrarMensaje("Dato invalido", "Los minutos deben ser un numero entero", Alert.AlertType.WARNING);
            return;
        }
        if (minutos < 0) {
            mostrarMensaje("Dato invalido", "Los minutos no pueden ser negativos", Alert.AlertType.WARNING);
            return;
        }

        try {
            empleadoController.registrarEmpleado(tipo, nombre, documento, codigo, minutos, caja);
            mostrarMensaje("Empleado registrado", nombre + " fue registrado correctamente", Alert.AlertType.INFORMATION);
            limpiarCampos();
            refrescarTabla();
        } catch (IllegalArgumentException e) {
            mostrarMensaje("No se pudo registrar", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void volverEvent(ActionEvent event) {
        aplicacion.openViewInicio();
    }

    private void refrescarTabla() {
        tblEmpleados.setItems(FXCollections.observableArrayList(empleadoController.obtenerEmpleados()));
        tblEmpleados.refresh();
    }

    private void limpiarCampos() {
        txtNombre.clear();
        txtDocumento.clear();
        txtCodigo.clear();
        txtMinutos.clear();
        txtCaja.clear();
    }

    public void mostrarMensaje(String titulo, String mensaje, Alert.AlertType alertType) {
        Alert alerta = new Alert(alertType);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
