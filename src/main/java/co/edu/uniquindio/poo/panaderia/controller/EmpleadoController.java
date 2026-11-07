package co.edu.uniquindio.poo.panaderia.controller;

import co.edu.uniquindio.poo.panaderia.model.Cajero;
import co.edu.uniquindio.poo.panaderia.model.Empleado;
import co.edu.uniquindio.poo.panaderia.model.Panaderia;
import co.edu.uniquindio.poo.panaderia.model.PanaderoArtesanal;
import co.edu.uniquindio.poo.panaderia.model.PanaderoIndustrial;

import java.util.List;

/**
 * Servicios relacionados con los empleados. Sirve de puente entre la
 * vista y el modelo.
 */
public class EmpleadoController {

    public static final String ARTESANAL = "Panadero artesanal";
    public static final String INDUSTRIAL = "Panadero industrial";
    public static final String CAJERO = "Cajero";

    private final Panaderia panaderia;

    public EmpleadoController(Panaderia panaderia) {
        this.panaderia = panaderia;
    }

    public List<String> obtenerTiposEmpleado() {
        return List.of(ARTESANAL, INDUSTRIAL, CAJERO);
    }

    public Empleado registrarEmpleado(String tipo, String nombre, String documento,
                                      String codigo, int minutos, String caja) {
        Empleado empleado;
        switch (tipo) {
            case ARTESANAL:
                empleado = new PanaderoArtesanal(nombre, documento, codigo, minutos);
                break;
            case INDUSTRIAL:
                empleado = new PanaderoIndustrial(nombre, documento, codigo, minutos);
                break;
            case CAJERO:
                empleado = new Cajero(nombre, documento, codigo, minutos, caja);
                break;
            default:
                throw new IllegalArgumentException("Tipo de empleado no valido");
        }
        panaderia.registrarEmpleado(empleado);
        return empleado;
    }

    public List<Empleado> obtenerEmpleados() {
        return panaderia.getListaEmpleados();
    }
}
