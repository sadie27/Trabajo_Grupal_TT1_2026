package org.trabajott1.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trabajott1.repository.SolicitudRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests de regresión para verificar el comportamiento del motor de simulación de {@link SimulationService}.
 * Comprueba que las células no desaparecen incorrectamente al colisionar con otras de la misma especie
 * y que cuando dos especies no pueden comerse entre ellas, ambas sobreviven.
 *
 * @author Lucas, Ana, Clara, Santiago
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
class SimulationServiceReproductionTest {

    @Mock
    private SolicitudRepository solicitudRepository;

    @InjectMocks
    private SimulationService simulationService;

    /**
     * Verifica que cuando una especie colisiona consigo misma, el número de células no disminuye
     * en cada paso de tiempo (las colisiones de misma especie no deben causar muertes).
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void testSameSpeciesCollision_CellDisappears() {
        List<String> names = List.of("SpeciesA");
        List<Integer> quantities = List.of(10);

        try {
            java.lang.reflect.Method method = SimulationService.class.getDeclaredMethod("generateSimulationData", List.class, List.class);
            method.setAccessible(true);
            String result = (String) method.invoke(simulationService, names, quantities);
            
            String[] lines = result.split("\n");
            java.util.Map<Integer, Integer> counts = new java.util.HashMap<>();
            for (int i = 1; i < lines.length; i++) {
                if (lines[i].trim().isEmpty()) continue;
                int t = Integer.parseInt(lines[i].split(",")[0]);
                counts.put(t, counts.getOrDefault(t, 0) + 1);
            }
            
            for (int t = 0; t <= 10; t++) {
                Integer count = counts.get(t);
                System.out.println("T" + t + " count: " + count);
                if (t > 0) {
                    Integer prevCount = counts.get(t-1);
                    if (prevCount != null && count != null) {
                        assertTrue(count >= prevCount, "Cells disappeared at T=" + t + "! Prev=" + prevCount + ", Curr=" + count);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Verifica que cuando dos especies no tienen relación de depredación entre ellas
     * (p.ej. "roja" y "verde" separadas por otra en la jerarquía), el número de células
     * no disminuye en ningún paso de tiempo.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void testDifferentSpeciesNoEating_BothSurvive() {
        // Red and Green don't eat each other (Red eats Blue, Blue eats Green)
        List<String> names = List.of("SpeciesRed", "SpeciesBlue", "SpeciesGreen"); // Use Red and Green by skipping Blue
        // Wait, COLORS[0]=red, COLORS[1]=blue, COLORS[2]=green
        // If I use species 0 and 2, they are Red and Green.
        List<String> selectedNames = List.of("RedSpecies", "Dummy", "GreenSpecies");
        List<Integer> selectedQuantities = List.of(5, 0, 5);

        try {
            java.lang.reflect.Method method = SimulationService.class.getDeclaredMethod("generateSimulationData", List.class, List.class);
            method.setAccessible(true);
            String result = (String) method.invoke(simulationService, selectedNames, selectedQuantities);
            
            String[] lines = result.split("\n");
            java.util.Map<Integer, Integer> counts = new java.util.HashMap<>();
            for (int i = 1; i < lines.length; i++) {
                if (lines[i].trim().isEmpty()) continue;
                int t = Integer.parseInt(lines[i].split(",")[0]);
                counts.put(t, counts.getOrDefault(t, 0) + 1);
            }
            
            for (int t = 0; t <= 10; t++) {
                Integer count = counts.get(t);
                System.out.println("T" + t + " count: " + count);
                if (t > 0) {
                    Integer prevCount = counts.get(t-1);
                    if (prevCount != null && count != null) {
                        assertTrue(count >= prevCount, "Cells disappeared at T=" + t + " when they shouldn't have been eaten! Prev=" + prevCount + ", Curr=" + count);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
