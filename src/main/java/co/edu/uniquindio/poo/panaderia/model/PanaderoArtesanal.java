package co.edu.uniquindio.poo.panaderia.model;

/**
 * Panadero que trabaja con bandejas de hasta 12 panes.
 * Tradicional: 30 minutos por bandeja. Integral: 40 minutos por bandeja.
 */
public class PanaderoArtesanal extends Panadero {

    public static final int CAPACIDAD_BANDEJA = 12;
    public static final int MINUTOS_BANDEJA_TRADICIONAL = 30;
    public static final int MINUTOS_BANDEJA_INTEGRAL = 40;

    public PanaderoArtesanal(String nombre, String documento, String codigo, int minutosDisponibles) {
        super(nombre, documento, codigo, minutosDisponibles);
    }

    @Override
    public int calcularMinutosProduccion(TipoPan tipoPan, int cantidad) {
        int bandejas = calcularGrupos(cantidad, CAPACIDAD_BANDEJA);
        int minutosPorBandeja = (tipoPan == TipoPan.TRADICIONAL)
                ? MINUTOS_BANDEJA_TRADICIONAL
                : MINUTOS_BANDEJA_INTEGRAL;
        return bandejas * minutosPorBandeja;
    }

    @Override
    public String getTipo() {
        return "Panadero artesanal";
    }
}
