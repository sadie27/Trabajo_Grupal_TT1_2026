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
 * Tests unitarios para {@link SimulationListenerB}.
 * Verifica que el worker B delega correctamente en el servicio de simulación
 * y que no lanza excepciones cuando el servicio falla.
 *
 * @author Ana
 * @version 2.0
 */
@ExtendWith(MockitoExtension.class)
class SimulationListenerBTest {

    @Mock
    private ISimulationService simulationService;

    @InjectMocks
    private SimulationListenerB simulationListenerB;

    /**
     * Verifica que al recibir un mensaje en la cola B, el listener llama a executeSimulation con los datos correctos.
     */
    @Test
    void receiveMessage_WorkerB_CallsService() {
        Integer solicitudId = 2;
        List<String> names = List.of("B");
        List<Integer> quantities = List.of(20);
        SimulationMessage message = new SimulationMessage(solicitudId, names, quantities);

        simulationListenerB.receiveMessage(message);

        verify(simulationService).executeSimulation(solicitudId, names, quantities);
    }

    /**
     * Verifica que cuando el servicio lanza una excepción, el worker B la captura sin propagarla.
     */
    @Test
    void receiveMessage_WorkerB_HandlesException() {
        Integer solicitudId = 2;
        SimulationMessage message = new SimulationMessage(solicitudId, List.of("B"), List.of(20));

        doThrow(new RuntimeException("Simulated error"))
                .when(simulationService).executeSimulation(anyInt(), anyList(), anyList());

        // No debería lanzar excepción
        simulationListenerB.receiveMessage(message);

        verify(simulationService).executeSimulation(anyInt(), anyList(), anyList());
    }
}