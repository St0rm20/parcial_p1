package co.edu.uniquindio.poo.panaderia.model;

import java.util.Objects;

/**
 * Empleado de la panaderia. Se identifica por un codigo unico y maneja
 * los minutos que tiene disponibles en el turno.
 */
public abstract class Empleado extends Persona {

    private final String codigo;
    private int minutosDisponibles;

    public Empleado(String nombre, String documento, String codigo, int minutosDisponibles) {
        super(nombre, documento);
        this.codigo = codigo;
        this.minutosDisponibles = minutosDisponibles;
    }

    public String getCodigo() {
        return codigo;
    }

    public int getMinutosDisponibles() {
        return minutosDisponibles;
    }

    public void setMinutosDisponibles(int minutosDisponibles) {
        this.minutosDisponibles = minutosDisponibles;
    }

    /**
     * Indica si el empleado tiene al menos los minutos solicitados.
     */
    public boolean tieneMinutosSuficientes(int minutos) {
        return minutosDisponibles >= minutos;
    }

    /**
     * Descuenta minutos del tiempo disponible (reserva).
     */
    public void reservarMinutos(int minutos) {
        if (minutos <= 0) {
            throw new IllegalArgumentException("Los minutos a reservar deben ser mayores que cero");
        }
        if (!tieneMinutosSuficientes(minutos)) {
            throw new IllegalStateException("El empleado no tiene minutos suficientes");
        }
        minutosDisponibles -= minutos;
    }

    /**
     * Devuelve minutos al tiempo disponible (cuando se cancela un pedido).
     */
    public void liberarMinutos(int minutos) {
        if (minutos <= 0) {
            throw new IllegalArgumentException("Los minutos a liberar deben ser mayores que cero");
        }
        minutosDisponibles += minutos;
    }

    /**
     * Nombre del tipo de empleado, se usa para mostrarlo en la interfaz.
     */
    public abstract String getTipo();

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Empleado empleado = (Empleado) o;
        return Objects.equals(codigo, empleado.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(codigo);
    }

    @Override
    public String toString() {
        return getTipo() + "{" +
                "codigo='" + codigo + '\'' +
                ", nombre='" + getNombre() + '\'' +
                ", minutosDisponibles=" + minutosDisponibles +
                '}';
    }
}
