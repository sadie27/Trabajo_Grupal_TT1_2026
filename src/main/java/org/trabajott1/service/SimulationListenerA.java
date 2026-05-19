package org.trabajott1.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.trabajott1.configuration.RabbitMQConfig;
import org.trabajott1.model.SimulationMessage;

/**
 * Worker A: escucha la cola {@code simulation.queue.A} y procesa simulaciones de forma asíncrona.
 * En el patrón muchos-a-muchos, este listener es uno de los múltiples consumidores independientes
 * que reciben cada mensaje publicado en el exchange. Opera en paralelo con SimulationListenerB
 * y con cualquier otro worker que se añada en el futuro.
 *
 * @author Ana, Lucas, Santi, Clara
 * @version 2.0
 */
@Component
public class SimulationListenerA {

    private static final Logger log = LoggerFactory.getLogger(SimulationListenerA.class);

    /** Servicio que ejecuta la lógica de simulación de vida artificial. */
    private final ISimulationService simulationService;

    /**
     * Crea el listener inyectando el servicio de simulación.
     *
     * @param simulationService el servicio que ejecutará la simulación al recibir un mensaje
     * @author Lucas, Ana, Clara, Santiago
     * @version 2.0
     */
    public SimulationListenerA(ISimulationService simulationService) {
        this.simulationService = simulationService;
    }

    /**
     * Recibe un mensaje de la cola A y ejecuta la simulación correspondiente.
     * Espera 5 segundos antes de comenzar para simular un proceso costoso.
     * Captura cualquier excepción para que no se interrumpa el listener.
     *
     * @param message el mensaje con el ID de solicitud, nombres de entidades y cantidades iniciales
     * @author Lucas, Ana, Clara, Santiago
     * @version 2.0
     */
    @RabbitListener(queues = RabbitMQConfig.SIMULATION_QUEUE_A)
    public void receiveMessage(SimulationMessage message) {
        log.info("[Worker A] Recibida tarea de simulación para solicitud ID: {}", message.getSolicitudId());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        try {
            simulationService.executeSimulation(
                    message.getSolicitudId(),
                    message.getEntityNames(),
                    message.getInitialQuantities()
            );
            log.info("[Worker A] Simulación completada para solicitud ID: {}", message.getSolicitudId());
        } catch (Exception e) {
            log.error("[Worker A] Error procesando simulación para solicitud ID: {}", message.getSolicitudId(), e);
        }
    }
}
