package co.edu.uniquindio.poo.panaderia.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Clase principal del modelo. Conserva la lista de empleados en orden de
 * registro y el historial de pedidos, y contiene la logica de negocio.
 */
public class Panaderia {

    private String nombre;
    private final List<Empleado> listaEmpleados;
    private final List<Pedido> listaPedidos;

    public Panaderia(String nombre) {
        this.nombre = nombre;
        this.listaEmpleados = new ArrayList<>();
        this.listaPedidos = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Empleado> getListaEmpleados() {
        return Collections.unmodifiableList(listaEmpleados);
    }

    public List<Pedido> getListaPedidos() {
        return Collections.unmodifiableList(listaPedidos);
    }

    // ---------------------------------------------------------------
    // Empleados
    // ---------------------------------------------------------------

    /**
     * Registra un empleado al final de la lista. El codigo debe ser unico.
     */
    public void registrarEmpleado(Empleado empleado) {
        if (empleado == null) {
            throw new IllegalArgumentException("El empleado no puede ser nulo");
        }
        if (buscarEmpleado(empleado.getCodigo()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un empleado con el codigo " + empleado.getCodigo());
        }
        listaEmpleados.add(empleado);
    }

    public Optional<Empleado> buscarEmpleado(String codigo) {
        return listaEmpleados.stream()
                .filter(e -> e.getCodigo().equals(codigo))
                .findFirst();
    }

    /**
     * Devuelve solo los empleados que participan en la produccion,
     * respetando el orden de registro.
     */
    public List<Panadero> obtenerPanaderos() {
        List<Panadero> panaderos = new ArrayList<>();
        for (Empleado empleado : listaEmpleados) {
            if (empleado instanceof Panadero) {
                panaderos.add((Panadero) empleado);
            }
        }
        return panaderos;
    }

    // ---------------------------------------------------------------
    // Funcionalidad 1: registrar pedido
    // ---------------------------------------------------------------

    /**
     * Registra un pedido en estado PENDIENTE. No reserva minutos.
     *
     * @throws IllegalArgumentException si la cantidad no es mayor que cero
     *                                  o si el codigo ya existe en el historial
     */
    public Pedido registrarPedido(String codigo, String nombreCliente, TipoPan tipoPan, int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad de panes debe ser mayor que cero");
        }
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El codigo del pedido es obligatorio");
        }
        if (tipoPan == null) {
            throw new IllegalArgumentException("El tipo de pan es obligatorio");
        }
        if (buscarPedido(codigo).isPresent()) {
            throw new IllegalArgumentException("Ya existe un pedido con el codigo " + codigo);
        }
        Pedido pedido = new Pedido(codigo, nombreCliente, tipoPan, cantidad);
        listaPedidos.add(pedido);
        return pedido;
    }

    /**
     * Busca en todo el historial, incluidos los pedidos cancelados.
     */
    public Optional<Pedido> buscarPedido(String codigo) {
        return listaPedidos.stream()
                .filter(p -> p.getCodigo().equals(codigo))
                .findFirst();
    }

    private Pedido obtenerPedido(String codigo) {
        return buscarPedido(codigo)
                .orElseThrow(() -> new IllegalArgumentException("No existe un pedido con el codigo " + codigo));
    }

    // ---------------------------------------------------------------
    // Funcionalidad 2: asignar pedido
    // ---------------------------------------------------------------

    /**
     * Selecciona entre los panaderos con tiempo suficiente al que tarda menos.
     * Si hay empate gana el que aparece primero en la lista.
     * La estimacion no consume tiempo; solo se descuenta al seleccionado.
     *
     * @return el panadero al que se asigno el pedido
     * @throws IllegalArgumentException si el pedido no existe
     * @throws IllegalStateException    si el pedido no esta pendiente o
     *                                  ningun panadero tiene tiempo suficiente
     */
    public Panadero asignarPedido(String codigoPedido) {
        Pedido pedido = obtenerPedido(codigoPedido);
        if (!pedido.estaPendiente()) {
            throw new IllegalStateException("El pedido " + codigoPedido + " no esta pendiente, su estado es " + pedido.getEstado());
        }

        Panadero seleccionado = null;
        int menorTiempo = Integer.MAX_VALUE;
        for (Panadero panadero : obtenerPanaderos()) {
            int minutos = panadero.estimarMinutos(pedido);
            // "<" estricto: en empate se conserva el primero de la lista
            if (panadero.tieneMinutosSuficientes(minutos) && minutos < menorTiempo) {
                seleccionado = panadero;
                menorTiempo = minutos;
            }
        }

        if (seleccionado == null) {
            throw new IllegalStateException("Ningun panadero tiene tiempo suficiente para el pedido " + codigoPedido);
        }

        seleccionado.reservarMinutos(menorTiempo);
        pedido.asignar(seleccionado.getCodigo(), menorTiempo);
        return seleccionado;
    }

    // ---------------------------------------------------------------
    // Funcionalidad 3: cancelar pedido
    // ---------------------------------------------------------------

    /**
     * PENDIENTE -> CANCELADO sin tocar minutos.
     * ASIGNADO  -> devuelve los minutos reservados y pasa a CANCELADO.
     * CANCELADO -> IllegalStateException.
     * El pedido permanece en el historial.
     *
     * @throws IllegalArgumentException si el pedido no existe
     * @throws IllegalStateException    si el pedido ya estaba cancelado
     */
    public void cancelarPedido(String codigoPedido) {
        Pedido pedido = obtenerPedido(codigoPedido);
        switch (pedido.getEstado()) {
            case PENDIENTE:
                pedido.cancelar();
                break;
            case ASIGNADO:
                Empleado empleado = buscarEmpleado(pedido.getCodigoEmpleadoAsignado())
                        .orElseThrow(() -> new IllegalStateException("No se encontro el empleado asignado"));
                empleado.liberarMinutos(pedido.getMinutosReservados());
                pedido.cancelar();
                break;
            case CANCELADO:
                throw new IllegalStateException("El pedido " + codigoPedido + " ya esta cancelado");
        }
    }
}
