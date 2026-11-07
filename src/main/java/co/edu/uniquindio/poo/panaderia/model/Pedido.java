package co.edu.uniquindio.poo.panaderia.model;

import java.util.Objects;

/**
 * Pedido de pan de un cliente. Nace en estado PENDIENTE.
 */
public class Pedido {

    private final String codigo;
    private String nombreCliente;
    private TipoPan tipoPan;
    private int cantidad;
    private EstadoPedido estado;
    private String codigoEmpleadoAsignado;
    private int minutosReservados;

    public Pedido(String codigo, String nombreCliente, TipoPan tipoPan, int cantidad) {
        this.codigo = codigo;
        this.nombreCliente = nombreCliente;
        this.tipoPan = tipoPan;
        this.cantidad = cantidad;
        this.estado = EstadoPedido.PENDIENTE;
        this.codigoEmpleadoAsignado = null;
        this.minutosReservados = 0;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public TipoPan getTipoPan() {
        return tipoPan;
    }

    public void setTipoPan(TipoPan tipoPan) {
        this.tipoPan = tipoPan;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public String getCodigoEmpleadoAsignado() {
        return codigoEmpleadoAsignado;
    }

    public int getMinutosReservados() {
        return minutosReservados;
    }

    public boolean estaPendiente() {
        return estado == EstadoPedido.PENDIENTE;
    }

    public boolean estaAsignado() {
        return estado == EstadoPedido.ASIGNADO;
    }

    public boolean estaCancelado() {
        return estado == EstadoPedido.CANCELADO;
    }

    /**
     * Guarda el empleado y los minutos reservados y pasa el pedido a ASIGNADO.
     */
    public void asignar(String codigoEmpleado, int minutos) {
        if (!estaPendiente()) {
            throw new IllegalStateException("Solo se puede asignar un pedido pendiente");
        }
        this.codigoEmpleadoAsignado = codigoEmpleado;
        this.minutosReservados = minutos;
        this.estado = EstadoPedido.ASIGNADO;
    }

    /**
     * Pasa el pedido a CANCELADO. Se conservan el empleado y los minutos
     * reservados como parte del historial.
     */
    public void cancelar() {
        if (estaCancelado()) {
            throw new IllegalStateException("El pedido ya esta cancelado");
        }
        this.estado = EstadoPedido.CANCELADO;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Pedido pedido = (Pedido) o;
        return Objects.equals(codigo, pedido.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(codigo);
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "codigo='" + codigo + '\'' +
                ", cliente='" + nombreCliente + '\'' +
                ", tipoPan=" + tipoPan +
                ", cantidad=" + cantidad +
                ", estado=" + estado +
                '}';
    }
}
