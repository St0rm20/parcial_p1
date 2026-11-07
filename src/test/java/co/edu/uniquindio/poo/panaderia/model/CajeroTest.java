package co.edu.uniquindio.poo.panaderia.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas del cajero y del manejo de minutos heredado de Empleado.
 */
public class CajeroTest {

    @Test
    public void cajeroGuardaSusDatos() {
        Cajero lucia = new Cajero("Lucia", "1004", "E04", 120, "Caja 1");
        assertEquals("Lucia", lucia.getNombre());
        assertEquals("1004", lucia.getDocumento());
        assertEquals("E04", lucia.getCodigo());
        assertEquals(120, lucia.getMinutosDisponibles());
        assertEquals("Caja 1", lucia.getCaja());
    }

    @Test
    public void cajeroNoParticipaEnProduccion() {
        Empleado lucia = new Cajero("Lucia", "1004", "E04", 120, "Caja 1");
        assertFalse(lucia instanceof IProduccion);
        assertFalse(lucia instanceof Panadero);
    }

    @Test
    public void cajeroNoEsTenidoEnCuentaComoPanadero() {
        Panaderia panaderia = new Panaderia("El Trigal");
        panaderia.registrarEmpleado(new Cajero("Lucia", "1004", "E04", 120, "Caja 1"));
        panaderia.registrarEmpleado(new PanaderoArtesanal("Ana", "1001", "E01", 100));
        assertEquals(2, panaderia.getListaEmpleados().size());
        assertEquals(1, panaderia.obtenerPanaderos().size());
    }

    @Test
    public void noSePermitenEmpleadosConCodigoRepetido() {
        Panaderia panaderia = new Panaderia("El Trigal");
        panaderia.registrarEmpleado(new Cajero("Lucia", "1004", "E04", 120, "Caja 1"));
        assertThrows(IllegalArgumentException.class,
                () -> panaderia.registrarEmpleado(new Cajero("Pedro", "1005", "E04", 60, "Caja 2")));
        assertEquals(1, panaderia.getListaEmpleados().size());
    }

    @Test
    public void reservarYLiberarMinutos() {
        Cajero lucia = new Cajero("Lucia", "1004", "E04", 120, "Caja 1");
        lucia.reservarMinutos(20);
        assertEquals(100, lucia.getMinutosDisponibles());
        lucia.liberarMinutos(20);
        assertEquals(120, lucia.getMinutosDisponibles());
        assertThrows(IllegalStateException.class, () -> lucia.reservarMinutos(121));
        assertEquals(120, lucia.getMinutosDisponibles());
    }
}
