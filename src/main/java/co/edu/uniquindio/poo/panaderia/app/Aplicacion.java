package co.edu.uniquindio.poo.panaderia.app;

import co.edu.uniquindio.poo.panaderia.model.Cajero;
import co.edu.uniquindio.poo.panaderia.model.Panaderia;
import co.edu.uniquindio.poo.panaderia.model.PanaderoArtesanal;
import co.edu.uniquindio.poo.panaderia.model.PanaderoIndustrial;
import co.edu.uniquindio.poo.panaderia.model.TipoPan;
import co.edu.uniquindio.poo.panaderia.viewcontrollers.EmpleadoViewController;
import co.edu.uniquindio.poo.panaderia.viewcontrollers.InicioViewController;
import co.edu.uniquindio.poo.panaderia.viewcontrollers.PedidoViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Aplicacion extends Application {

    private Stage primaryStage;
    // instancia unica del modelo que comparten todas las ventanas
    public static Panaderia panaderia = new Panaderia("El Trigal");

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Panaderia El Trigal");
        inicializarData();
        openViewInicio();
    }

    public void openViewInicio() {
        FXMLLoader loader = cargarVista("inicio.fxml");
        InicioViewController viewController = loader.getController();
        viewController.setApp(this);
    }

    public void openViewEmpleados() {
        FXMLLoader loader = cargarVista("empleados.fxml");
        EmpleadoViewController viewController = loader.getController();
        viewController.setApp(this);
    }

    public void openViewPedidos() {
        FXMLLoader loader = cargarVista("pedidos.fxml");
        PedidoViewController viewController = loader.getController();
        viewController.setApp(this);
    }

    // este codigo sirve para abrir una ventana
    private FXMLLoader cargarVista(String archivo) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Aplicacion.class.getResource("/co/edu/uniquindio/poo/panaderia/" + archivo));
            AnchorPane rootLayout = loader.load();
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.sizeToScene();
            primaryStage.centerOnScreen();
            primaryStage.show();
            return loader;
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo cargar la vista " + archivo, e);
        }
    }

    private void inicializarData() {
        if (!panaderia.getListaEmpleados().isEmpty()) {
            return;
        }
        panaderia.registrarEmpleado(new PanaderoArtesanal("Ana", "1001", "E01", 100));
        panaderia.registrarEmpleado(new PanaderoIndustrial("Carlos", "1002", "E02", 120));
        panaderia.registrarEmpleado(new PanaderoArtesanal("Diana", "1003", "E03", 80));
        panaderia.registrarEmpleado(new Cajero("Lucia", "1004", "E04", 120, "Caja 1"));

        panaderia.registrarPedido("P01", "Cafeteria Central", TipoPan.TRADICIONAL, 25);
        panaderia.registrarPedido("P02", "Restaurante Sur", TipoPan.INTEGRAL, 13);
    }

    public static void main(String[] args) {
        launch();
    }
}
