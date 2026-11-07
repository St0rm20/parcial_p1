package co.edu.uniquindio.poo.panaderia.model;

/**
 * Empleado que participa en la produccion de pan. Cada subclase define
 * su forma de trabajo mediante el metodo calcularMinutosProduccion.
 */
public abstract class Panadero extends Empleado implements IProduccion {

    public Panadero(String nombre, String documento, String codigo, int minutosDisponibles) {
        super(nombre, documento, codigo, minutosDisponibles);
    }

    /**
     * Estima los minutos que tardaria en atender el pedido completo.
     */
    public int estimarMinutos(Pedido pedido) {
        return calcularMinutosProduccion(pedido.getTipoPan(), pedido.getCantidad());
    }

    /**
     * Indica si tiene al menos los minutos necesarios para el pedido completo.
     */
    public boolean puedeAtender(Pedido pedido) {
        return tieneMinutosSuficientes(estimarMinutos(pedido));
    }

    /**
     * Calcula cuantos grupos (bandejas o lotes) se necesitan. Un grupo
     * incompleto cuenta como uno completo, por eso se redondea hacia arriba.
     */
    protected int calcularGrupos(int cantidad, int capacidad) {
        return (cantidad + capacidad - 1) / capacidad;
    }
}
