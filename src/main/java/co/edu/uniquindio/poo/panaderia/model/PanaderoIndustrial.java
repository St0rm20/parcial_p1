package co.edu.uniquindio.poo.panaderia.model;

/**
 * Panadero que usa una maquina con lotes de hasta 30 panes.
 * Invierte 20 minutos de preparacion una sola vez por pedido.
 * Tradicional: 25 minutos por lote. Integral: 35 minutos por lote.
 */
public class PanaderoIndustrial extends Panadero {

    public static final int CAPACIDAD_LOTE = 30;
    public static final int MINUTOS_PREPARACION = 20;
    public static final int MINUTOS_LOTE_TRADICIONAL = 25;
    public static final int MINUTOS_LOTE_INTEGRAL = 35;

    public PanaderoIndustrial(String nombre, String documento, String codigo, int minutosDisponibles) {
        super(nombre, documento, codigo, minutosDisponibles);
    }

    @Override
    public int calcularMinutosProduccion(TipoPan tipoPan, int cantidad) {
        int lotes = calcularGrupos(cantidad, CAPACIDAD_LOTE);
        int minutosPorLote = (tipoPan == TipoPan.TRADICIONAL)
                ? MINUTOS_LOTE_TRADICIONAL
                : MINUTOS_LOTE_INTEGRAL;
        return MINUTOS_PREPARACION + lotes * minutosPorLote;
    }

    @Override
    public String getTipo() {
        return "Panadero industrial";
    }
}
