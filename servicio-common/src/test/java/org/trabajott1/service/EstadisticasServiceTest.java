package org.trabajott1.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trabajott1.persistence.entity.EntidadSolicitudEntity;
import org.trabajott1.persistence.entity.EstadisticaPoblacionEntity;
import org.trabajott1.persistence.entity.SolicitudEntity;
import org.trabajott1.repository.EntidadSolicitudRepository;
import org.trabajott1.repository.EstadisticaPoblacionRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para {@link EstadisticasService}.
 * Verifica el cálculo de estadísticas de simulación: color más reproducido,
 * color que más comió, dominante final, y manejo de token nulo o sin datos.
 *
 * @author Lucas, Ana, Clara, Santiago
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
class EstadisticasServiceTest {

    @Mock
    private EstadisticaPoblacionRepository estadisticaRepo;

    @Mock
    private EntidadSolicitudRepository entidadRepo;

    @InjectMocks
    private EstadisticasService estadisticasService;

    // --- Helpers ---

    private EstadisticaPoblacionEntity stat(int paso, String color, int cantidad) {
        EstadisticaPoblacionEntity e = new EstadisticaPoblacionEntity();
        e.setPasoTiempo(paso);
        e.setColor(color);
        e.setCantidad(cantidad);
        return e;
    }

    private EntidadSolicitudEntity entidad(String nombre) {
        EntidadSolicitudEntity e = new EntidadSolicitudEntity();
        e.setNombreEntidad(nombre);
        return e;
    }

    // --- Token nulo ---

    /**
     * Verifica que pasar token nulo devuelve una respuesta de error sin consultar repositorios.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void obtenerEstadisticas_NullToken_ReturnsError() {
        EstadisticasService.EstadisticasResponse result = estadisticasService.obtenerEstadisticas(null);

        assertFalse(result.done());
        assertNotNull(result.errorMessage());
        assertNull(result.tokenSolicitud());
        verifyNoInteractions(estadisticaRepo, entidadRepo);
    }

    // --- Sin datos ---

    /**
     * Verifica que un token válido sin estadísticas asociadas devuelve una respuesta de error.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void obtenerEstadisticas_EmptyStats_ReturnsError() {
        when(estadisticaRepo.findBySolicitud_TokenSolicitud(999)).thenReturn(List.of());

        EstadisticasService.EstadisticasResponse result = estadisticasService.obtenerEstadisticas(999);

        assertFalse(result.done());
        assertNotNull(result.errorMessage());
        assertEquals(999, result.tokenSolicitud());
        verify(estadisticaRepo).findBySolicitud_TokenSolicitud(999);
        verifyNoInteractions(entidadRepo);
    }

    // --- Happy path ---

    /**
     * Verifica el happy path completo: calcula correctamente el color más reproducido,
     * el que más comió y el dominante al final con datos de dos colores (red y blue).
     *
     * Red: 10 → 20 → 15  (incremento=10, decremento=5)
     * Blue: 5 → 3 → 8    (incremento=5, decremento=2)
     * Último paso: red=15, blue=8 → dominante=red
     * Más reproducido: red (delta +10 > blue +5)
     * Más disminuido: red (delta -5 > blue -2) → depredador de red = yellow
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void obtenerEstadisticas_HappyPath_CalculatesAllFields() {
        int tok = 12345;
        List<EstadisticaPoblacionEntity> stats = List.of(
            stat(0, "red",  10),
            stat(1, "red",  20),
            stat(2, "red",  15),
            stat(0, "blue",  5),
            stat(1, "blue",  3),
            stat(2, "blue",  8)
        );
        List<EntidadSolicitudEntity> entidades = List.of(
            entidad("Rojo"),
            entidad("Azul")
        );

        when(estadisticaRepo.findBySolicitud_TokenSolicitud(tok)).thenReturn(stats);
        when(entidadRepo.findBySolicitud_TokenSolicitudOrderByIdAsc(tok)).thenReturn(entidades);

        EstadisticasService.EstadisticasResponse result = estadisticasService.obtenerEstadisticas(tok);

        assertTrue(result.done());
        assertNull(result.errorMessage());
        assertEquals(tok, result.tokenSolicitud());

        // Entidades mapeadas con colores asignados por orden
        assertEquals(2, result.entidades().size());
        assertEquals("red",  result.entidades().get(0).color());
        assertEquals("blue", result.entidades().get(1).color());

        // Población ordenada por paso y luego por color
        assertFalse(result.poblacionPorPaso().isEmpty());

        // Red tuvo el mayor incremento acumulado
        assertEquals("red", result.colorMasReproducido());

        // Red fue la que más disminuyó → su depredador es yellow
        assertEquals("yellow", result.colorMasComio());

        // En el paso 2: red=15 > blue=8
        assertEquals("red", result.colorMasDominante());
    }

    /**
     * Verifica que con un único color (sin comparaciones) los campos de comedor/reproducido/dominante
     * se calculan sin excepción y devuelven el único color disponible.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void obtenerEstadisticas_SingleColor_ReturnsWithoutError() {
        int tok = 11111;
        List<EstadisticaPoblacionEntity> stats = List.of(
            stat(0, "green", 5),
            stat(1, "green", 10),
            stat(2, "green", 8)
        );
        when(estadisticaRepo.findBySolicitud_TokenSolicitud(tok)).thenReturn(stats);
        when(entidadRepo.findBySolicitud_TokenSolicitudOrderByIdAsc(tok)).thenReturn(List.of(entidad("Verde")));

        EstadisticasService.EstadisticasResponse result = estadisticasService.obtenerEstadisticas(tok);

        assertTrue(result.done());
        assertEquals("green", result.colorMasReproducido());
        assertEquals("green", result.colorMasDominante());
        // green es el más disminuido → su depredador es blue
        assertEquals("blue", result.colorMasComio());
    }

    // --- Valores extremos en totalIncrementos / totalDecrementos ---

    /**
     * Verifica que una población estable (sin cambios) produce incremento y decremento cero,
     * y el resultado es igualmente válido.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void obtenerEstadisticas_FlatPopulation_ZeroIncrementAndDecrement() {
        int tok = 22222;
        List<EstadisticaPoblacionEntity> stats = List.of(
            stat(0, "red", 10),
            stat(1, "red", 10),
            stat(2, "red", 10)
        );
        when(estadisticaRepo.findBySolicitud_TokenSolicitud(tok)).thenReturn(stats);
        when(entidadRepo.findBySolicitud_TokenSolicitudOrderByIdAsc(tok)).thenReturn(List.of(entidad("Rojo")));

        EstadisticasService.EstadisticasResponse result = estadisticasService.obtenerEstadisticas(tok);

        assertTrue(result.done());
        // Con delta=0 en ambas direcciones, el único color gana por defecto
        assertEquals("red", result.colorMasReproducido());
        assertEquals("red", result.colorMasDominante());
    }

    /**
     * Verifica que una población en caída constante (solo decrementos, ningún incremento)
     * se procesa correctamente: colorMasReproducido sigue siendo el único color,
     * colorMasComio es su depredador definido en DEPREDADOR_DE.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void obtenerEstadisticas_OnlyDecrementing_NegativeDeltasIgnoredForIncrements() {
        int tok = 33333;
        List<EstadisticaPoblacionEntity> stats = List.of(
            stat(0, "blue", 100),
            stat(1, "blue",  50),
            stat(2, "blue",   5)
        );
        when(estadisticaRepo.findBySolicitud_TokenSolicitud(tok)).thenReturn(stats);
        when(entidadRepo.findBySolicitud_TokenSolicitudOrderByIdAsc(tok)).thenReturn(List.of(entidad("Azul")));

        EstadisticasService.EstadisticasResponse result = estadisticasService.obtenerEstadisticas(tok);

        assertTrue(result.done());
        // Ningún delta positivo → incremento = 0, pero el único color gana
        assertEquals("blue", result.colorMasReproducido());
        // blue decrementó → su depredador es red
        assertEquals("red", result.colorMasComio());
        // Dominante en el último paso
        assertEquals("blue", result.colorMasDominante());
    }

    /**
     * Verifica que valores de cantidad muy grandes (cercanos a Integer.MAX_VALUE) no producen
     * desbordamiento aritmético observable en el resultado.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void obtenerEstadisticas_LargeQuantities_NoArithmeticOverflow() {
        int tok = 44444;
        // Incremento = MAX_VALUE - 1, que encaja en int positivo sin overflow
        List<EstadisticaPoblacionEntity> stats = List.of(
            stat(0, "red", 1),
            stat(1, "red", Integer.MAX_VALUE)
        );
        when(estadisticaRepo.findBySolicitud_TokenSolicitud(tok)).thenReturn(stats);
        when(entidadRepo.findBySolicitud_TokenSolicitudOrderByIdAsc(tok)).thenReturn(List.of(entidad("Rojo")));

        // No debe lanzar excepción
        assertDoesNotThrow(() -> estadisticasService.obtenerEstadisticas(tok));
    }
}
