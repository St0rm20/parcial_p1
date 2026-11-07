package co.edu.uniquindio.poo.panaderia.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas obligatorias del parcial: tres por funcionalidad.
 * Cada prueba prepara sus propios datos.
 */
public class PanaderiaTest {

    private static final Logger LOG = Logger.getLogger(PanaderiaTest.class.getName());

    private Panaderia panaderia;

    @BeforeEach
    public void setUp() {
        panaderia = new Panaderia("El Trigal");
    }

    // ---------------------------------------------------------------
    // Registrar
    // ---------------------------------------------------------------

    @Test
    public void registrarPedidoQuedaPendienteSinConsumirMinutos() {
        LOG.info("Inicio registrarPedidoQuedaPendienteSinConsumirMinutos");
        PanaderoArtesanal ana = new PanaderoArtesanal("Ana", "1001", "E01", 100);
        PanaderoIndustrial carlos = new PanaderoIndustrial("Carlos", "1002", "E02", 45);
        panaderia.registrarEmpleado(ana);
        panaderia.registrarEmpleado(carlos);

        Pedido pedido = panaderia.registrarPedido("P01", "Cafeteria Central", TipoPan.TRADICIONAL, 25);

        assertEquals(EstadoPedido.PENDIENTE, pedido.getEstado());
        assertEquals(25, pedido.getCantidad());
        assertEquals(1, panaderia.getListaPedidos().size());
        assertEquals(100, ana.getMinutosDisponibles());
        assertEquals(45, carlos.getMinutosDisponibles());
        LOG.info("Fin registrarPedidoQuedaPendienteSinConsumirMinutos");
    }

    @Test
    public void registrarPedidoConCodigoDeUnPedidoCanceladoLanzaExcepcion() {
        LOG.info("Inicio registrarPedidoConCodigoDeUnPedidoCanceladoLanzaExcepcion");
        Pedido original = panaderia.registrarPedido("P01", "Cafeteria Central", TipoPan.TRADICIONAL, 25);
        panaderia.cancelarPedido("P01");

        assertThrows(IllegalArgumentException.class,
                () -> panaderia.registrarPedido("P01", "Restaurante Sur", TipoPan.INTEGRAL, 10));

        assertEquals(1, panaderia.getListaPedidos().size());
        assertSame(original, panaderia.getListaPedidos().get(0));
        assertEquals(EstadoPedido.CANCELADO, panaderia.getListaPedidos().get(0).getEstado());
        LOG.info("Fin registrarPedidoConCodigoDeUnPedidoCanceladoLanzaExcepcion");
    }

    @Test
    public void registrarPedidoDeCeroPanesLanzaExcepcion() {
        LOG.info("Inicio registrarPedidoDeCeroPanesLanzaExcepcion");
        assertTrue(panaderia.getListaPedidos().isEmpty());

        assertThrows(IllegalArgumentException.class,
                () -> panaderia.registrarPedido("P01", "Cafeteria Central", TipoPan.TRADICIONAL, 0));

        assertTrue(panaderia.getListaPedidos().isEmpty());
        LOG.info("Fin registrarPedidoDeCeroPanesLanzaExcepcion");
    }

    // ---------------------------------------------------------------
    // Asignar
    // ---------------------------------------------------------------

    @Test
    public void asignarPedidoAlPanaderoQueTardaMenos() {
        LOG.info("Inicio asignarPedidoAlPanaderoQueTardaMenos");
        PanaderoArtesanal ana = new PanaderoArtesanal("Ana", "1001", "E01", 100);
        PanaderoIndustrial carlos = new PanaderoIndustrial("Carlos", "1002", "E02", 45);
        panaderia.registrarEmpleado(ana);
        panaderia.registrarEmpleado(carlos);
        Pedido pedido = panaderia.registrarPedido("P01", "Cafeteria Central", TipoPan.TRADICIONAL, 25);

        assertEquals(90, ana.estimarMinutos(pedido));
        assertEquals(45, carlos.estimarMinutos(pedido));

        Panadero asignado = panaderia.asignarPedido("P01");

        assertSame(carlos, asignado);
        assertEquals(EstadoPedido.ASIGNADO, pedido.getEstado());
        assertEquals("E02", pedido.getCodigoEmpleadoAsignado());
        assertEquals(45, pedido.getMinutosReservados());
        assertEquals(0, carlos.getMinutosDisponibles());
        assertEquals(100, ana.getMinutosDisponibles());
        LOG.info("Fin asignarPedidoAlPanaderoQueTardaMenos");
    }

    @Test
    public void asignarPedidoEnEmpateSeleccionaAlPrimeroRegistrado() {
        LOG.info("Inicio asignarPedidoEnEmpateSeleccionaAlPrimeroRegistrado");
        PanaderoArtesanal ana = new PanaderoArtesanal("Ana", "1001", "E01", 80);
        PanaderoArtesanal diana = new PanaderoArtesanal("Diana", "1003", "E03", 80);
        PanaderoIndustrial carlos = new PanaderoIndustrial("Carlos", "1002", "E02", 54);
        panaderia.registrarEmpleado(ana);
        panaderia.registrarEmpleado(diana);
        panaderia.registrarEmpleado(carlos);
        Pedido pedido = panaderia.registrarPedido("P02", "Restaurante Sur", TipoPan.INTEGRAL, 13);

        assertEquals(55, carlos.estimarMinutos(pedido));
        assertFalse(carlos.puedeAtender(pedido));
        assertEquals(80, ana.estimarMinutos(pedido));
        assertEquals(80, diana.estimarMinutos(pedido));

        Panadero asignado = panaderia.asignarPedido("P02");

        assertSame(ana, asignado);
        assertEquals(EstadoPedido.ASIGNADO, pedido.getEstado());
        assertEquals("E01", pedido.getCodigoEmpleadoAsignado());
        assertEquals(80, pedido.getMinutosReservados());
        assertEquals(0, ana.getMinutosDisponibles());
        assertEquals(80, diana.getMinutosDisponibles());
        assertEquals(54, carlos.getMinutosDisponibles());
        LOG.info("Fin asignarPedidoEnEmpateSeleccionaAlPrimeroRegistrado");
    }

    @Test
    public void asignarPedidoSinPanaderoConTiempoSuficienteLanzaExcepcion() {
        LOG.info("Inicio asignarPedidoSinPanaderoConTiempoSuficienteLanzaExcepcion");
        PanaderoIndustrial carlos = new PanaderoIndustrial("Carlos", "1002", "E02", 44);
        Cajero lucia = new Cajero("Lucia", "1004", "E04", 120, "Caja 1");
        panaderia.registrarEmpleado(carlos);
        panaderia.registrarEmpleado(lucia);
        Pedido pedido = panaderia.registrarPedido("P01", "Cafeteria Central", TipoPan.TRADICIONAL, 25);

        assertThrows(IllegalStateException.class, () -> panaderia.asignarPedido("P01"));

        assertEquals(EstadoPedido.PENDIENTE, pedido.getEstado());
        assertNull(pedido.getCodigoEmpleadoAsignado());
        assertEquals(0, pedido.getMinutosReservados());
        assertEquals(44, carlos.getMinutosDisponibles());
        assertEquals(120, lucia.getMinutosDisponibles());
        LOG.info("Fin asignarPedidoSinPanaderoConTiempoSuficienteLanzaExcepcion");
    }

    // ---------------------------------------------------------------
    // Cancelar
    // ---------------------------------------------------------------

    @Test
    public void cancelarPedidoAsignadoDevuelveLosMinutosReservados() {
        LOG.info("Inicio cancelarPedidoAsignadoDevuelveLosMinutosReservados");
        PanaderoIndustrial carlos = new PanaderoIndustrial("Carlos", "1002", "E02", 120);
        panaderia.registrarEmpleado(carlos);
        Pedido primero = panaderia.registrarPedido("P01", "Cafeteria Central", TipoPan.TRADICIONAL, 25);
        Pedido segundo = panaderia.registrarPedido("P02", "Restaurante Sur", TipoPan.TRADICIONAL, 25);
        panaderia.asignarPedido("P01");
        panaderia.asignarPedido("P02");
        assertEquals(45, primero.getMinutosReservados());
        assertEquals(45, segundo.getMinutosReservados());
        assertEquals(30, carlos.getMinutosDisponibles());

        panaderia.cancelarPedido("P01");

        assertEquals(75, carlos.getMinutosDisponibles());
        assertEquals(EstadoPedido.CANCELADO, primero.getEstado());
        assertEquals(EstadoPedido.ASIGNADO, segundo.getEstado());
        LOG.info("Fin cancelarPedidoAsignadoDevuelveLosMinutosReservados");
    }

    @Test
    public void cancelarPedidoPendienteConservaMinutosYHistorial() {
        LOG.info("Inicio cancelarPedidoPendienteConservaMinutosYHistorial");
        PanaderoArtesanal ana = new PanaderoArtesanal("Ana", "1001", "E01", 100);
        panaderia.registrarEmpleado(ana);
        Pedido pedido = panaderia.registrarPedido("P01", "Cafeteria Central", TipoPan.TRADICIONAL, 25);

        panaderia.cancelarPedido("P01");

        assertEquals(EstadoPedido.CANCELADO, pedido.getEstado());
        assertTrue(panaderia.buscarPedido("P01").isPresent());
        assertEquals(1, panaderia.getListaPedidos().size());
        assertEquals(100, ana.getMinutosDisponibles());
        LOG.info("Fin cancelarPedidoPendienteConservaMinutosYHistorial");
    }

    @Test
    public void cancelarDosVecesLanzaExcepcionYDevuelveTiempoUnaSolaVez() {
        LOG.info("Inicio cancelarDosVecesLanzaExcepcionYDevuelveTiempoUnaSolaVez");
        PanaderoIndustrial carlos = new PanaderoIndustrial("Carlos", "1002", "E02", 120);
        panaderia.registrarEmpleado(carlos);
        Pedido pedido = panaderia.registrarPedido("P01", "Cafeteria Central", TipoPan.TRADICIONAL, 25);
        panaderia.asignarPedido("P01");
        assertEquals(75, carlos.getMinutosDisponibles());
        panaderia.cancelarPedido("P01");
        assertEquals(120, carlos.getMinutosDisponibles());

        assertThrows(IllegalStateException.class, () -> panaderia.cancelarPedido("P01"));

        assertEquals(120, carlos.getMinutosDisponibles());
        assertEquals(EstadoPedido.CANCELADO, pedido.getEstado());
        LOG.info("Fin cancelarDosVecesLanzaExcepcionYDevuelveTiempoUnaSolaVez");
    }
}
