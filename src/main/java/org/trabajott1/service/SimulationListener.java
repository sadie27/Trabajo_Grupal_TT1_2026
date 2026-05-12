package org.trabajott1.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.trabajott1.configuration.RabbitMQConfig;
import org.trabajott1.model.SimulationMessage;
/**
 * Componente que escucha la cola RabbitMQ de simulaciones.
 *
 * Actúa como consumidor de mensajes: permanece a la espera de que
 * llegue un SimulationMessage a la cola "simulation.queue" y,
 * al recibirlo, delega la ejecución en SimulationService.
 *
 * Esto desacopla completamente la recepción de la solicitud (API)
 * de su procesamiento (simulación), haciendo el sistema más robusto:
 * si el procesamiento falla, el API ya ha respondido al cliente
 * y el error queda registrado en los logs.
 */
@Component
public class SimulationListener {

    private static final Logger log = LoggerFactory.getLogger(SimulationListener.class);

    /** Servicio que contiene la lógica de ejecución de la simulación */
    private final SimulationService simulationService;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param simulationService servicio de simulación a invocar
     *                         al recibir cada mensaje
     */
    public SimulationListener(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    /**
     * Método invocado automáticamente cada vez que llega un mensaje
     * a la cola "simulation.queue".
     *
     * Extrae los datos del mensaje y lanza la simulación.
     * Si la simulación falla, el error se registra en el log
     * pero no se propaga, evitando que el listener se bloquee.
     *
     * @param message el mensaje recibido de la cola, deserializado
     *                automáticamente desde JSON por Jackson
     */
    @RabbitListener(queues = RabbitMQConfig.SIMULATION_QUEUE)
    public void receiveMessage(SimulationMessage message) {
        log.info("Recibida tarea de simulación para solicitud ID: {}",
                message.getSolicitudId());
        try {
            simulationService.executeSimulation(
                    message.getSolicitudId(),
                    message.getEntityNames(),
                    message.getInitialQuantities()
            );
            log.info("Simulación completada para solicitud ID: {}",
                    message.getSolicitudId());
        } catch (Exception e) {
            log.error("Error procesando simulación para solicitud ID: {}",
                    message.getSolicitudId(), e);
        }
    }
}