package co.edu.uniquindio.poo.panaderia.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de los cambios de estado del pedido.
 */
public class PedidoTest {

    @Test
    public void pedidoNuevoQuedaPendienteSinEmpleado() {
        Pedido pedido = new Pedido("P01", "Cafeteria", TipoPan.TRADICIONAL, 25);
        assertEquals(EstadoPedido.PENDIENTE, pedido.getEstado());
        assertNull(pedido.getCodigoEmpleadoAsignado());
        assertEquals(0, pedido.getMinutosReservados());
    }

    @Test
    public void asignarGuardaEmpleadoYMinutos() {
        Pedido pedido = new Pedido("P01", "Cafeteria", TipoPan.TRADICIONAL, 25);
        pedido.asignar("E02", 45);
        assertEquals(EstadoPedido.ASIGNADO, pedido.getEstado());
        assertEquals("E02", pedido.getCodigoEmpleadoAsignado());
        assertEquals(45, pedido.getMinutosReservados());
    }

    @Test
    public void noSePuedeAsignarDosVeces() {
        Pedido pedido = new Pedido("P01", "Cafeteria", TipoPan.TRADICIONAL, 25);
        pedido.asignar("E02", 45);
        assertThrows(IllegalStateException.class, () -> pedido.asignar("E01", 90));
        assertEquals("E02", pedido.getCodigoEmpleadoAsignado());
        assertEquals(45, pedido.getMinutosReservados());
    }

    @Test
    public void cancelarCambiaElEstado() {
        Pedido pedido = new Pedido("P01", "Cafeteria", TipoPan.INTEGRAL, 10);
        pedido.cancelar();
        assertEquals(EstadoPedido.CANCELADO, pedido.getEstado());
        assertTrue(pedido.estaCancelado());
    }

    @Test
    public void noSePuedeCancelarDosVecesNiAsignarCancelado() {
        Pedido pedido = new Pedido("P01", "Cafeteria", TipoPan.INTEGRAL, 10);
        pedido.cancelar();
        assertThrows(IllegalStateException.class, pedido::cancelar);
        assertThrows(IllegalStateException.class, () -> pedido.asignar("E01", 40));
        assertEquals(EstadoPedido.CANCELADO, pedido.getEstado());
    }
}
