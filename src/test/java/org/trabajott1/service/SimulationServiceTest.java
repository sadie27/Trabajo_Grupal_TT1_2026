package org.trabajott1.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trabajott1.repository.SolicitudRepository;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para {@link SimulationService}.
 *
 * <p>El método {@code canEat} es privado, por lo que se usa Reflection
 * para invocarlo directamente y verificar la cadena alimenticia cíclica:
 * red → blue → green → yellow → red.</p>
 */
@ExtendWith(MockitoExtension.class)
class SimulationServiceTest {

    @Mock
    private SolicitudRepository solicitudRepository;

    @InjectMocks
    private SimulationService simulationService;

    /** Referencia al método privado canEat obtenida mediante Reflection. */
    private Method canEatMethod;

    @BeforeEach
    void setUp() throws Exception {
        canEatMethod = SimulationService.class
                .getDeclaredMethod("canEat", String.class, String.class);
        canEatMethod.setAccessible(true);
    }

    // ---- Tests de cadena alimenticia válida ----

    @Test
    void canEat_redComeBlue() throws Exception {
        boolean result = (boolean) canEatMethod.invoke(simulationService, "red", "blue");
        assertTrue(result, "Red debería poder comerse a Blue");
    }

    @Test
    void canEat_blueComeGreen() throws Exception {
        boolean result = (boolean) canEatMethod.invoke(simulationService, "blue", "green");
        assertTrue(result, "Blue debería poder comerse a Green");
    }

    @Test
    void canEat_greenComeYellow() throws Exception {
        boolean result = (boolean) canEatMethod.invoke(simulationService, "green", "yellow");
        assertTrue(result, "Green debería poder comerse a Yellow");
    }

    @Test
    void canEat_yellowComeRed_cierraCiclo() throws Exception {
        boolean result = (boolean) canEatMethod.invoke(simulationService, "yellow", "red");
        assertTrue(result, "Yellow debería poder comerse a Red (cierra el ciclo)");
    }

    // ---- Tests de cadena alimenticia inválida ----

    @Test
    void canEat_redNoPuedeComerGreen() throws Exception {
        boolean result = (boolean) canEatMethod.invoke(simulationService, "red", "green");
        assertFalse(result, "Red NO debería poder comerse a Green");
    }

    @Test
    void canEat_blueNoPuedeComerRed() throws Exception {
        boolean result = (boolean) canEatMethod.invoke(simulationService, "blue", "red");
        assertFalse(result, "Blue NO debería poder comerse a Red");
    }

    @Test
    void canEat_greenNoPuedeComerRed() throws Exception {
        boolean result = (boolean) canEatMethod.invoke(simulationService, "green", "red");
        assertFalse(result, "Green NO debería poder comerse a Red");
    }

    @Test
    void canEat_yellowNoPuedeComerBlue() throws Exception {
        boolean result = (boolean) canEatMethod.invoke(simulationService, "yellow", "blue");
        assertFalse(result, "Yellow NO debería poder comerse a Blue");
    }

    // ---- Tests de casos borde ----

    @Test
    void canEat_mismoColor_devuelveFalse() throws Exception {
        boolean result = (boolean) canEatMethod.invoke(simulationService, "red", "red");
        assertFalse(result, "Una especie no debería poder comerse a sí misma");
    }

    @Test
    void canEat_colorDesconocidoAtacante_devuelveFalse() throws Exception {
        boolean result = (boolean) canEatMethod.invoke(simulationService, "purple", "red");
        assertFalse(result, "Un color desconocido como atacante no debería poder comer");
    }

    @Test
    void canEat_colorDesconocidoObjetivo_devuelveFalse() throws Exception {
        boolean result = (boolean) canEatMethod.invoke(simulationService, "red", "purple");
        assertFalse(result, "Un color desconocido como objetivo no debería poder ser comido");
    }

    @Test
    void canEat_ambosColorDesconocido_devuelveFalse() throws Exception {
        boolean result = (boolean) canEatMethod.invoke(simulationService, "purple", "orange");
        assertFalse(result, "Dos colores desconocidos no deberían interactuar");
    }
}
