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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests de flujo completo y food-sharing para {@link AdvancedSimulationService}.
 * Complementa {@link AdvancedSimulationServiceTest} (que solo cubre canEat() y el campo
 * hasFoodToShare) añadiendo: executeSimulation() end-to-end, invariantes del grid,
 * jerarquía alimentaria avanzada (red come blue y green, yellow no come) y verificación
 * de que la lógica de compartición de comida de blue es estructuralmente alcanzable.
 *
 * @author Lucas, Ana, Clara, Santiago
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
class AdvancedSimulationServiceFlowTest {

    @Mock
    private SolicitudRepository solicitudRepository;

    @Mock
    private EstadisticaPoblacionRepository estadisticaPoblacionRepository;

    @InjectMocks
    private AdvancedSimulationService simulationService;

    // ── Helpers ───────────────────────────────────────────────────────────────

    private String runGenerate(List<String> names, List<Integer> quantities,
                               List<EstadisticaPoblacionEntity> stats) throws Exception {
        Method m = AdvancedSimulationService.class.getDeclaredMethod(
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
     * Verifica que 0 entidades lanza {@link IllegalArgumentException}.
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
     * Verifica que 7 entidades lanza {@link IllegalArgumentException}.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void executeSimulation_SevenEntities_ThrowsIllegalArgument() {
        assertThrows(IllegalArgumentException.class,
                () -> simulationService.executeSimulation(1,
                        List.of("A", "B", "C", "D", "E", "F", "G"),
                        List.of(1, 1, 1, 1, 1, 1, 1)));
    }

    // ── executeSimulation end-to-end ──────────────────────────────────────────

    /**
     * Verifica el flujo completo de executeSimulation() cuando la solicitud existe:
     * persiste resultado, marca estado FINALIZADA y guarda estadísticas.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void executeSimulation_SolicitudFound_PersistsResultAndStats() {
        SolicitudEntity entity = new SolicitudEntity();
        entity.setIdSolicitud(10);
        when(solicitudRepository.findById(10)).thenReturn(Optional.of(entity));

        simulationService.executeSimulation(10, List.of("Rojo", "Azul"), List.of(5, 5));

        ArgumentCaptor<SolicitudEntity> captor = ArgumentCaptor.forClass(SolicitudEntity.class);
        verify(solicitudRepository).save(captor.capture());
        SolicitudEntity saved = captor.getValue();

        assertEquals("FINALIZADA", saved.getEstado());
        assertNotNull(saved.getResultado());
        assertNotNull(saved.getResultado().getDatosResultado());
        assertSame(saved, saved.getResultado().getSolicitud());
        verify(estadisticaPoblacionRepository).saveAll(anyList());
    }

    /**
     * Verifica que cuando la solicitud no existe no se persiste nada.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void executeSimulation_SolicitudNotFound_DoesNothing() {
        when(solicitudRepository.findById(99)).thenReturn(Optional.empty());

        simulationService.executeSimulation(99, List.of("A"), List.of(1));

        verify(solicitudRepository, never()).save(any());
        verify(estadisticaPoblacionRepository, never()).saveAll(anyList());
    }

    // ── Invariantes del grid ──────────────────────────────────────────────────

    /**
     * Verifica que el output comienza con "8" (GRID_SIZE) y que todas las coordenadas
     * están dentro de [0, 7] en todos los pasos.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void generateSimulationData_GridSizeAndBoundsCorrect() throws Exception {
        String output = runGenerate(List.of("Rojo"), List.of(8), new ArrayList<>());

        assertEquals("8", output.lines().findFirst().orElse(""),
                "El output debe empezar con GRID_SIZE=8");

        output.lines().skip(1).filter(l -> !l.isBlank()).forEach(line -> {
            String[] parts = line.split(",");
            int row = Integer.parseInt(parts[1]);
            int col = Integer.parseInt(parts[2]);
            assertTrue(row >= 0 && row <= 7, "Fila fuera de rango: " + row);
            assertTrue(col >= 0 && col <= 7, "Columna fuera de rango: " + col);
        });
    }

    /**
     * Verifica que se generan exactamente 11 × 6 entradas de estadísticas
     * (11 pasos × 6 colores) independientemente de las especies usadas.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void generateSimulationData_StatsHaveCorrectSize() throws Exception {
        List<EstadisticaPoblacionEntity> stats = new ArrayList<>();
        runGenerate(List.of("Rojo", "Azul", "Verde"), List.of(4, 4, 4), stats);

        assertEquals(11 * 6, stats.size(),
                "Deben generarse 11 pasos × 6 colores = 66 entradas de estadísticas");
    }

    /**
     * Verifica que en T=0 el número de células colocadas es exactamente el solicitado
     * (siempre que no supere 64).
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void generateSimulationData_InitialPlacementCorrect() throws Exception {
        List<EstadisticaPoblacionEntity> stats = new ArrayList<>();
        String output = runGenerate(List.of("Rojo", "Azul"), List.of(5, 5), stats);

        Map<Integer, Long> porPaso = parseCuentaPorPaso(output);
        assertEquals(10L, porPaso.getOrDefault(0, 0L),
                "En T=0 deben colocarse exactamente 10 células");
    }

    // ── Jerarquía alimentaria avanzada ────────────────────────────────────────

    /**
     * Verifica que con red (depredador cima) y blue (presa de red) la cantidad de
     * células permanece en [0, 64] en todos los pasos (red puede comer blue).
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void generateSimulationData_RedEatsBlue_CountStaysInBounds() throws Exception {
        List<EstadisticaPoblacionEntity> stats = new ArrayList<>();
        String output = runGenerate(List.of("Rojo", "Azul"), List.of(10, 10), stats);

        parseCuentaPorPaso(output).forEach((paso, count) ->
                assertTrue(count >= 0 && count <= 64,
                        "Paso " + paso + ": cantidad inválida " + count));
    }

    /**
     * Verifica que con red y green (ambas presas en jerarquía diferente: red come green)
     * las cuentas permanecen válidas.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void generateSimulationData_RedEatsGreen_CountStaysInBounds() throws Exception {
        // red→blue→green: red también come green directamente en AdvancedSimulationService
        List<EstadisticaPoblacionEntity> stats = new ArrayList<>();
        String output = runGenerate(
                List.of("Rojo", "Dummy", "Verde"), List.of(8, 0, 8), stats);

        parseCuentaPorPaso(output).forEach((paso, count) ->
                assertTrue(count >= 0 && count <= 64,
                        "Paso " + paso + ": cantidad inválida " + count));
    }

    /**
     * Verifica que yellow (no come nada) y red (no es comida) mantienen sus células
     * sin que yellow pueda eliminar a nadie: en T=0 las células de yellow están presentes.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void generateSimulationData_YellowDoesNotEat_InitialYellowCellsPresent() throws Exception {
        // yellow=4ª especie (índice 3), red=1ª (índice 0)
        // Ponemos solo yellow (4ª posición) usando 3 dummies con cantidad 0
        List<EstadisticaPoblacionEntity> stats = new ArrayList<>();
        String output = runGenerate(
                List.of("D1", "D2", "D3", "Amarillo"), List.of(0, 0, 0, 6), stats);

        Map<Integer, Long> porPaso = parseCuentaPorPaso(output);
        assertEquals(6L, porPaso.getOrDefault(0, 0L),
                "Las 6 células de yellow deben estar en T=0");
    }

    // ── Food-sharing de blue: campo y alcanzabilidad ──────────────────────────

    /**
     * Verifica que la clase interna Cell de AdvancedSimulationService tiene el campo
     * hasFoodToShare y que es de tipo boolean (requisito de la lógica de sharing).
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void cellClass_HasFoodToShareField_IsBoolean() throws Exception {
        Class<?>[] innerClasses = AdvancedSimulationService.class.getDeclaredClasses();
        assertTrue(innerClasses.length > 0, "AdvancedSimulationService debe tener clase interna Cell");

        Class<?> cellClass = innerClasses[0];
        Field field = cellClass.getDeclaredField("hasFoodToShare");
        field.setAccessible(true);
        assertEquals(boolean.class, field.getType(),
                "hasFoodToShare debe ser de tipo boolean");
    }

    /**
     * Verifica que una simulación con blue presente genera estadísticas válidas
     * (el código de food-sharing no rompe el flujo principal).
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void generateSimulationData_WithBlueSpecies_FoodSharingDoesNotBreakFlow() throws Exception {
        // blue=2ª especie (índice 1), con muchas células para que interaccionen entre sí
        List<EstadisticaPoblacionEntity> stats = new ArrayList<>();
        String output = runGenerate(
                List.of("Dummy", "Azul"), List.of(0, 20), stats);

        assertEquals("8", output.lines().findFirst().orElse(""),
                "El output debe ser válido con blue activo");
        assertEquals(11 * 6, stats.size(),
                "Las estadísticas deben generarse completas con blue activo");

        // Ninguna coordenada debe salirse del grid
        output.lines().skip(1).filter(l -> !l.isBlank()).forEach(line -> {
            String[] parts = line.split(",");
            int row = Integer.parseInt(parts[1]);
            int col = Integer.parseInt(parts[2]);
            assertTrue(row >= 0 && row <= 7, "Fila fuera de rango con blue: " + row);
            assertTrue(col >= 0 && col <= 7, "Col fuera de rango con blue: " + col);
        });
    }

    /**
     * Verifica que con red comiendo blue (para que blue coma y active hasFoodToShare)
     * la simulación completa no lanza ninguna excepción.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void generateSimulationData_RedAndBlue_FoodSharingPathReachable() throws Exception {
        // red come blue → blue activa hasFoodToShare al comer yellow
        // Aquí: red (índice 0) y blue (índice 1) con muchas células para maximizar colisiones
        List<EstadisticaPoblacionEntity> stats = new ArrayList<>();
        assertDoesNotThrow(() -> {
            try {
                runGenerate(List.of("Rojo", "Azul", "Dummy", "Amarillo"),
                            List.of(10, 10, 0, 10), stats);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        assertEquals(11 * 6, stats.size());
    }
}
