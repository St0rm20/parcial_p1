package co.edu.uniquindio.poo.panaderia.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de la forma de trabajo del panadero industrial
 * (lotes de 30 y 20 minutos de preparacion por pedido).
 */
public class PanaderoIndustrialTest {

    @Test
    public void unLoteTradicionalIncluyePreparacion() {
        PanaderoIndustrial carlos = new PanaderoIndustrial("Carlos", "1002", "E02", 100);
        assertEquals(45, carlos.calcularMinutosProduccion(TipoPan.TRADICIONAL, 25));
    }

    @Test
    public void treintaYUnPanesTradicionalesTardan70Minutos() {
        PanaderoIndustrial carlos = new PanaderoIndustrial("Carlos", "1002", "E02", 100);
        assertEquals(70, carlos.calcularMinutosProduccion(TipoPan.TRADICIONAL, 31));
    }

    @Test
    public void panIntegralTarda35MinutosPorLoteMasPreparacion() {
        PanaderoIndustrial carlos = new PanaderoIndustrial("Carlos", "1002", "E02", 100);
        assertEquals(55, carlos.calcularMinutosProduccion(TipoPan.INTEGRAL, 13));
        assertEquals(90, carlos.calcularMinutosProduccion(TipoPan.INTEGRAL, 60));
    }

    @Test
    public void laPreparacionSeCuentaUnaSolaVezPorPedido() {
        PanaderoIndustrial carlos = new PanaderoIndustrial("Carlos", "1002", "E02", 200);
        // 3 lotes: 20 + 3 * 25 = 95, no 3 * (20 + 25)
        assertEquals(95, carlos.calcularMinutosProduccion(TipoPan.TRADICIONAL, 90));
    }

    @Test
    public void noPuedeAtenderSiLeFaltaUnMinuto() {
        PanaderoIndustrial carlos = new PanaderoIndustrial("Carlos", "1002", "E02", 44);
        Pedido pedido = new Pedido("P01", "Cafeteria", TipoPan.TRADICIONAL, 25);
        assertFalse(carlos.puedeAtender(pedido));
        carlos.setMinutosDisponibles(45);
        assertTrue(carlos.puedeAtender(pedido));
    }
}
