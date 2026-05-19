package org.trabajott1.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trabajott1.model.SimulationMessage;

import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Tests unitarios para {@link SimulationListener}.
 * Verifica que el listener delega correctamente en el servicio de simulación
 * y que no lanza excepciones cuando el servicio falla.
 *
 * @author Lucas, Ana, Clara, Santiago
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
class SimulationListenerTest {

    @Mock
    private ISimulationService simulationService;

    @InjectMocks
    private SimulationListener simulationListener;

    /**
     * Verifica que al recibir un mensaje, el listener llama a executeSimulation con los datos correctos.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void receiveMessage_CallsService() {
        Integer solicitudId = 1;
        List<String> names = List.of("A");
        List<Integer> quantities = List.of(10);
        SimulationMessage message = new SimulationMessage(solicitudId, names, quantities);

        simulationListener.receiveMessage(message);

        verify(simulationService).executeSimulation(solicitudId, names, quantities);
    }

    /**
     * Verifica que cuando el servicio lanza una excepción, el listener la captura sin propagarla.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void receiveMessage_HandlesException() {
        Integer solicitudId = 1;
        SimulationMessage message = new SimulationMessage(solicitudId, List.of("A"), List.of(10));

        doThrow(new RuntimeException("Simulated error"))
                .when(simulationService).executeSimulation(anyInt(), anyList(), anyList());

        // No debería lanzar excepción
        simulationListener.receiveMessage(message);

        verify(simulationService).executeSimulation(anyInt(), anyList(), anyList());
    }
}
