package org.trabajott1.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trabajott1.persistence.entity.EstadisticaPoblacionEntity;
import org.trabajott1.persistence.entity.SolicitudEntity;
import org.trabajott1.repository.EstadisticaPoblacionRepository;
import org.trabajott1.repository.SolicitudRepository;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests de borde, colisión y reproducibilidad para {@link StandardSimulationService}.
 * Complementa {@link SimulationServiceTest} con escenarios que el test original no cubre:
 * validación de entidades fuera de rango, invariantes del grid 8×8, colisiones entre
 * depredador/presa y reproducibilidad de invariantes estructurales.
 *
 * @author Lucas, Ana, Clara, Santiago
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
class StandardSimulationServiceBorderTest {

    @Mock
    private SolicitudRepository solicitudRepository;

    @Mock
    private EstadisticaPoblacionRepository estadisticaPoblacionRepository;

    @InjectMocks
    private StandardSimulationService simulationService;

    // ── Helpers ───────────────────────────────────────────────────────────────

    private String runGenerate(List<String> names, List<Integer> quantities,
                               List<EstadisticaPoblacionEntity> stats) throws Exception {
        Method m = StandardSimulationService.class.getDeclaredMethod(
                "generateSimulationData", List.class, List.class, List.class);
        m.setAccessible(true);
        return (String) m.invoke(simulationService, names, quantities, stats);
    }

    private Map<Integer, Long> parseCuentaPorPaso(String output) {
        return output.lines()
                .skip(1)
                .filter(l -> !l.isBlank())
                .collect(Collectors.groupingBy(
                        l -> Integer.parseInt(l.split(",")[0]),
                        Collectors.counting()));
    }

    // ── Validación de entidades fuera de rango ────────────────────────────────

    /**
     * Verifica que pasar lista vacía (0 entidades) lanza {@link IllegalArgumentException}.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void executeSimulation_ZeroEntities_ThrowsIllegalArgument() {
        assertThrows(IllegalArgumentException.class,
                () -> simulationService.executeSimulation(1, List.of(), List.of()));
    }

    /**
     * Verifica que pasar 7 entidades (por encima del máximo de 6) lanza
     * {@link IllegalArgumentException}.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void executeSimulation_SevenEntities_ThrowsIllegalArgument() {
        List<String> names = List.of("A", "B", "C", "D", "E", "F", "G");
        List<Integer> qtys  = List.of(1, 1, 1, 1, 1, 1, 1);
        assertThrows(IllegalArgumentException.class,
                () -> simulationService.executeSimulation(1, names, qtys));
    }

    // ── Invariante del grid 8×8: máximo 64 células ───────────────────────────

    /**
     * Verifica que aunque se pidan más de 64 células, en ningún paso hay más de 64 registradas.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void generateSimulationData_OverflowGrid_NeverExceedsMaxCapacity() throws Exception {
        List<EstadisticaPoblacionEntity> stats = new ArrayList<>();
        String output = runGenerate(List.of("Rojo", "Azul"), List.of(50, 50), stats);

        Map<Integer, Long> porPaso = parseCuentaPorPaso(output);
        porPaso.forEach((paso, count) ->
                assertTrue(count <= 64,
                        "Paso " + paso + " tiene " + count + " células, supera el límite de 64"));
    }

    /**
     * Verifica que el output empieza con la línea "8" (GRID_SIZE).
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void generateSimulationData_OutputStartsWithGridSize() throws Exception {
        String output = runGenerate(List.of("Rojo"), List.of(5), new ArrayList<>());
        assertEquals("8", output.lines().findFirst().orElse(""));
    }

    // ── Borde de cuadrícula ───────────────────────────────────────────────────

    /**
     * Verifica que todas las coordenadas en el output están dentro del rango [0, 7],
     * incluso cuando las células son forzadas a moverse en los bordes del grid.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void generateSimulationData_AllCoordinatesWithinGridBounds() throws Exception {
        String output = runGenerate(List.of("Rojo"), List.of(10), new ArrayList<>());

        output.lines().skip(1).filter(l -> !l.isBlank()).forEach(line -> {
            String[] parts = line.split(",");
            int row = Integer.parseInt(parts[1]);
            int col = Integer.parseInt(parts[2]);
            assertTrue(row >= 0 && row <= 7, "Fila fuera de rango: " + row + " en línea: " + line);
            assertTrue(col >= 0 && col <= 7, "Col fuera de rango: " + col + " en línea: " + line);
        });
    }

    // ── Colisión depredador/presa ─────────────────────────────────────────────

    /**
     * Verifica que con depredadores (red) y presas (blue) la cantidad total de células
     * permanece en [0, 64] en todos los pasos y las estadísticas se generan correctamente
     * (11 pasos × 6 colores = 66 entradas).
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void generateSimulationData_PredatorPrey_CountStaysInBoundsAndStatsGenerated() throws Exception {
        List<EstadisticaPoblacionEntity> stats = new ArrayList<>();
        String output = runGenerate(List.of("Rojo", "Azul"), List.of(10, 10), stats);

        Map<Integer, Long> porPaso = parseCuentaPorPaso(output);
        for (int t = 0; t <= 10; t++) {
            long count = porPaso.getOrDefault(t, 0L);
            assertTrue(count >= 0 && count <= 64,
                    "Paso " + t + ": cantidad inválida " + count);
        }
        assertEquals(11 * 6, stats.size());
    }

    /**
     * Verifica que dos especies sin relación de depredación directa (red y green,
     * separadas por blue en la jerarquía, con blue ausente) colocan correctamente
     * sus células en T=0.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void generateSimulationData_NonDirectPredatorSpecies_InitialCountCorrect() throws Exception {
        List<EstadisticaPoblacionEntity> stats = new ArrayList<>();
        String output = runGenerate(
                List.of("Rojo", "Dummy", "Verde"), List.of(8, 0, 8), stats);

        Map<Integer, Long> porPaso = parseCuentaPorPaso(output);
        assertEquals(16L, porPaso.getOrDefault(0, 0L),
                "En T=0 deben haber exactamente 16 células");
    }

    // ── Reproducibilidad de invariantes estructurales ─────────────────────────

    /**
     * Verifica que dos ejecuciones independientes con los mismos parámetros producen
     * el mismo número de entradas de estadísticas y la misma cabecera (invariantes
     * estructurales), aunque la distribución exacta sea aleatoria.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void generateSimulationData_TwoRuns_SameStructuralInvariants() throws Exception {
        List<String> names = List.of("A", "B");
        List<Integer> qtys  = List.of(5, 5);

        List<EstadisticaPoblacionEntity> stats1 = new ArrayList<>();
        List<EstadisticaPoblacionEntity> stats2 = new ArrayList<>();

        String out1 = runGenerate(names, qtys, stats1);
        String out2 = runGenerate(names, qtys, stats2);

        assertEquals(out1.lines().findFirst().orElse(""),
                     out2.lines().findFirst().orElse(""),
                     "Ambas ejecuciones deben empezar con el mismo GRID_SIZE");
        assertEquals(11 * 6, stats1.size());
        assertEquals(stats1.size(), stats2.size());
    }

    // ── executeSimulation completo con mock ───────────────────────────────────

    /**
     * Verifica que con una sola entidad (mínimo válido) la simulación completa
     * persiste el resultado y las estadísticas.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void executeSimulation_MinimumOneEntity_PersistsResultAndStats() {
        SolicitudEntity entity = new SolicitudEntity();
        entity.setIdSolicitud(42);
        when(solicitudRepository.findById(42)).thenReturn(Optional.of(entity));

        simulationService.executeSimulation(42, List.of("Solo"), List.of(3));

        ArgumentCaptor<SolicitudEntity> captor = ArgumentCaptor.forClass(SolicitudEntity.class);
        verify(solicitudRepository).save(captor.capture());
        assertEquals("FINALIZADA", captor.getValue().getEstado());
        assertNotNull(captor.getValue().getResultado().getDatosResultado());
        verify(estadisticaPoblacionRepository).saveAll(anyList());
    }

    /**
     * Verifica que con seis entidades (máximo válido) la simulación no lanza excepción
     * y persiste el resultado.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void executeSimulation_MaximumSixEntities_PersistsResult() {
        SolicitudEntity entity = new SolicitudEntity();
        entity.setIdSolicitud(99);
        when(solicitudRepository.findById(99)).thenReturn(Optional.of(entity));

        simulationService.executeSimulation(99,
                List.of("A", "B", "C", "D", "E", "F"),
                List.of(1, 1, 1, 1, 1, 1));

        verify(solicitudRepository).save(any(SolicitudEntity.class));
    }
}
