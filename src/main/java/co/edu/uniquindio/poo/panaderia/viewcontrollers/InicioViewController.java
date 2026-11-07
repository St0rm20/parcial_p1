package co.edu.uniquindio.poo.panaderia.viewcontrollers;

import co.edu.uniquindio.poo.panaderia.app.Aplicacion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class InicioViewController {

    private Aplicacion aplicacion;

    @FXML
    void abrirEmpleadosEvent(ActionEvent event) {
        aplicacion.openViewEmpleados();
    }

    @FXML
    void abrirPedidosEvent(ActionEvent event) {
        aplicacion.openViewPedidos();
    }

    public void setApp(Aplicacion aplicacion) {
        this.aplicacion = aplicacion;
    }
}
