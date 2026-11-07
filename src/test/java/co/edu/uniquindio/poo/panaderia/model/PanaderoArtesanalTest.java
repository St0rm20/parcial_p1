package co.edu.uniquindio.poo.panaderia.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de la forma de trabajo del panadero artesanal (bandejas de 12).
 */
public class PanaderoArtesanalTest {

    @Test
    public void unaBandejaLlenaTradicionalTarda30Minutos() {
        PanaderoArtesanal ana = new PanaderoArtesanal("Ana", "1001", "E01", 100);
        assertEquals(30, ana.calcularMinutosProduccion(TipoPan.TRADICIONAL, 12));
    }

    @Test
    public void bandejaIncompletaCuentaComoCompleta() {
        PanaderoArtesanal ana = new PanaderoArtesanal("Ana", "1001", "E01", 100);
        assertEquals(30, ana.calcularMinutosProduccion(TipoPan.TRADICIONAL, 1));
        assertEquals(60, ana.calcularMinutosProduccion(TipoPan.TRADICIONAL, 13));
    }

    @Test
    public void treintaYUnPanesTradicionalesTardan90Minutos() {
        PanaderoArtesanal ana = new PanaderoArtesanal("Ana", "1001", "E01", 100);
        assertEquals(90, ana.calcularMinutosProduccion(TipoPan.TRADICIONAL, 31));
    }

    @Test
    public void panIntegralTarda40MinutosPorBandeja() {
        PanaderoArtesanal ana = new PanaderoArtesanal("Ana", "1001", "E01", 100);
        assertEquals(80, ana.calcularMinutosProduccion(TipoPan.INTEGRAL, 13));
    }

    @Test
    public void estimarNoConsumeMinutos() {
        PanaderoArtesanal ana = new PanaderoArtesanal("Ana", "1001", "E01", 100);
        Pedido pedido = new Pedido("P01", "Cafeteria", TipoPan.TRADICIONAL, 25);
        assertEquals(90, ana.estimarMinutos(pedido));
        assertTrue(ana.puedeAtender(pedido));
        assertEquals(100, ana.getMinutosDisponibles());
    }
}
