package org.trabajott1.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.trabajott1.configuration.RabbitMQConfig;
import org.trabajott1.model.SimulationMessage;

/**
 * Componente que escucha la cola de RabbitMQ y lanza la ejecución de simulaciones de forma asíncrona.
 * Cuando llega un mensaje con los datos de una solicitud, delega el procesamiento en {@link SimulationService}.
 * Incluye una espera inicial de 5 segundos antes de ejecutar la simulación.
 *
 * @author Lucas, Ana, Clara, Santiago
 * @version 1.0
 */
@Component
public class SimulationListener {

    private static final Logger log = LoggerFactory.getLogger(SimulationListener.class);

    /** Servicio que ejecuta la lógica de simulación de vida artificial. */
    private final ISimulationService simulationService;

    /**
     * Crea el listener inyectando el servicio de simulación.
     *
     * @param simulationService el servicio que ejecutará la simulación al recibir un mensaje
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    public SimulationListener(ISimulationService simulationService) {
        this.simulationService = simulationService;
    }

    /**
     * Recibe un mensaje de la cola de simulación y ejecuta la simulación correspondiente.
     * Espera 5 segundos antes de comenzar para simular un proceso costoso.
     * Captura cualquier excepción para que no se interrumpa el listener.
     *
     * @param message el mensaje con el ID de solicitud, nombres de entidades y cantidades iniciales
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @RabbitListener(queues = RabbitMQConfig.SIMULATION_QUEUE)
    public void receiveMessage(SimulationMessage message) {
        log.info("Recibida tarea de simulación para solicitud ID: {}", message.getSolicitudId());
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
            log.info("Simulación completada para solicitud ID: {}", message.getSolicitudId());
        } catch (Exception e) {
            log.error("Error procesando simulación para solicitud ID: {}", message.getSolicitudId(), e);
        }
    }
}
