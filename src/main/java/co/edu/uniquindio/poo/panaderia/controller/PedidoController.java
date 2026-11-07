package co.edu.uniquindio.poo.panaderia.controller;

import co.edu.uniquindio.poo.panaderia.model.Panaderia;
import co.edu.uniquindio.poo.panaderia.model.Panadero;
import co.edu.uniquindio.poo.panaderia.model.Pedido;
import co.edu.uniquindio.poo.panaderia.model.TipoPan;

import java.util.List;

/**
 * Servicios relacionados con los pedidos: registrar, asignar y cancelar.
 */
public class PedidoController {

    private final Panaderia panaderia;

    public PedidoController(Panaderia panaderia) {
        this.panaderia = panaderia;
    }

    public Pedido registrarPedido(String codigo, String cliente, TipoPan tipoPan, int cantidad) {
        return panaderia.registrarPedido(codigo, cliente, tipoPan, cantidad);
    }

    public Panadero asignarPedido(String codigo) {
        return panaderia.asignarPedido(codigo);
    }

    public void cancelarPedido(String codigo) {
        panaderia.cancelarPedido(codigo);
    }

    public List<Pedido> obtenerPedidos() {
        return panaderia.getListaPedidos();
    }
}
