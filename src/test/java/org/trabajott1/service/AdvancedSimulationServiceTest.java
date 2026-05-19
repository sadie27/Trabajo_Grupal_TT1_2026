package org.trabajott1.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trabajott1.repository.SolicitudRepository;
import org.trabajott1.repository.EstadisticaPoblacionRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AdvancedSimulationServiceTest {

    @Mock
    private SolicitudRepository solicitudRepository;

    @Mock
    private EstadisticaPoblacionRepository estadisticaPoblacionRepository;

    @InjectMocks
    private AdvancedSimulationService simulationService;

    @Test
    void testCanEat_YellowEatsNothing() {
        try {
            java.lang.reflect.Method method = AdvancedSimulationService.class.getDeclaredMethod("canEat", String.class, String.class);
            method.setAccessible(true);
            
            assertFalse((Boolean) method.invoke(simulationService, "yellow", "red"));
            assertFalse((Boolean) method.invoke(simulationService, "yellow", "blue"));
            assertFalse((Boolean) method.invoke(simulationService, "yellow", "green"));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testCanEat_RedIsCima() {
        try {
            java.lang.reflect.Method method = AdvancedSimulationService.class.getDeclaredMethod("canEat", String.class, String.class);
            method.setAccessible(true);
            
            assertFalse((Boolean) method.invoke(simulationService, "blue", "red"));
            assertFalse((Boolean) method.invoke(simulationService, "green", "red"));
            assertFalse((Boolean) method.invoke(simulationService, "yellow", "red"));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testCanEat_BlueEatsYellowNotGreen() {
        try {
            java.lang.reflect.Method method = AdvancedSimulationService.class.getDeclaredMethod("canEat", String.class, String.class);
            method.setAccessible(true);
            
            assertTrue((Boolean) method.invoke(simulationService, "blue", "yellow"));
            assertFalse((Boolean) method.invoke(simulationService, "blue", "green"));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testNewFoodChain() {
        try {
            java.lang.reflect.Method method = AdvancedSimulationService.class.getDeclaredMethod("canEat", String.class, String.class);
            method.setAccessible(true);
            
            // Red eats blue and green
            assertTrue((Boolean) method.invoke(simulationService, "red", "blue"));
            assertTrue((Boolean) method.invoke(simulationService, "red", "green"));
            // Blue and Green eat yellow
            assertTrue((Boolean) method.invoke(simulationService, "blue", "yellow"));
            assertTrue((Boolean) method.invoke(simulationService, "green", "yellow"));
            
            // Blue and Green don't eat each other
            assertFalse((Boolean) method.invoke(simulationService, "blue", "green"));
            assertFalse((Boolean) method.invoke(simulationService, "green", "blue"));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testBlueSharingLogic() {
        try {
            java.lang.reflect.Field field = AdvancedSimulationService.class.getDeclaredClasses()[0].getDeclaredField("hasFoodToShare");
            assertNotNull(field);
        } catch (Exception e) {
            fail("hasFoodToShare field missing in Cell class: " + e.getMessage());
        }
    }
}
