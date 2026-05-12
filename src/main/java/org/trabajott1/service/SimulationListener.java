package org.trabajott1.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.trabajott1.configuration.RabbitMQConfig;
import org.trabajott1.model.SimulationMessage;

/**
 * Componente que escucha mensajes de la cola de RabbitMQ para procesar simulaciones.
 */
@Component
public class SimulationListener {

    private static final Logger log = LoggerFactory.getLogger(SimulationListener.class);
    private final SimulationService simulationService;

    /**
     * Constructor de SimulationListener.
     *
     * @param simulationService El servicio de simulación a utilizar.
     */
    public SimulationListener(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    /**
     * Procesa un mensaje de simulación recibido de la cola.
     *
     * @param message El mensaje que contiene los datos para la simulación.
     */
    @RabbitListener(queues = RabbitMQConfig.SIMULATION_QUEUE)
    public void receiveMessage(SimulationMessage message) {
        log.info("Recibida tarea de simulación para solicitud ID: {}", message.getSolicitudId());
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
