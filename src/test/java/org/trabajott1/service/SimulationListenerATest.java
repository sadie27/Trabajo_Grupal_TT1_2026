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
 * Tests unitarios para {@link SimulationListenerA}.
 * Verifica que el worker A delega correctamente en el servicio de simulación
 * y que no lanza excepciones cuando el servicio falla.
 *
 * @author Ana
 * @version 2.0
 */
@ExtendWith(MockitoExtension.class)
class SimulationListenerATest {

    @Mock
    private ISimulationService simulationService;

    @InjectMocks
    private SimulationListenerA simulationListenerA;

    /**
     * Verifica que al recibir un mensaje en la cola A, el listener llama a executeSimulation con los datos correctos.
     */
    @Test
    void receiveMessage_WorkerA_CallsService() {
        Integer solicitudId = 1;
        List<String> names = List.of("A");
        List<Integer> quantities = List.of(10);
        SimulationMessage message = new SimulationMessage(solicitudId, names, quantities);

        simulationListenerA.receiveMessage(message);

        verify(simulationService).executeSimulation(solicitudId, names, quantities);
    }

    /**
     * Verifica que cuando el servicio lanza una excepción, el worker A la captura sin propagarla.
     */
    @Test
    void receiveMessage_WorkerA_HandlesException() {
        Integer solicitudId = 1;
        SimulationMessage message = new SimulationMessage(solicitudId, List.of("A"), List.of(10));

        doThrow(new RuntimeException("Simulated error"))
                .when(simulationService).executeSimulation(anyInt(), anyList(), anyList());

        // No debería lanzar excepción
        simulationListenerA.receiveMessage(message);

        verify(simulationService).executeSimulation(anyInt(), anyList(), anyList());
    }
}