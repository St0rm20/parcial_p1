package co.edu.uniquindio.poo.panaderia.model;

/**
 * Contrato que cumple todo empleado que participa en la produccion.
 */
public interface IProduccion {

    /**
     * Calcula los minutos que tardaria en producir la cantidad de panes
     * del tipo indicado. Solo consulta, no consume tiempo.
     */
    int calcularMinutosProduccion(TipoPan tipoPan, int cantidad);
}
