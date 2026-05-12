package org.trabajott1.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.trabajott1.configuration.RabbitMQConfig;
import org.trabajott1.model.SimulationMessage;
import org.trabajott1.repository.SolicitudRepository;

/**
 * Componente que escucha la cola RabbitMQ de simulaciones.
 *
 * <p>Actúa como consumidor de mensajes: permanece a la espera de que
 * llegue un {@link SimulationMessage} a la cola {@code simulation.queue} y,
 * al recibirlo, delega la ejecución en {@link SimulationService}.</p>
 *
 * <p>Esto desacopla completamente la recepción de la solicitud (API)
 * de su procesamiento (simulación). Si el procesamiento falla, el estado
 * de la solicitud se actualiza a {@code ERROR} en base de datos para
 * evitar que quede bloqueada en estado {@code PROCESANDO} indefinidamente.</p>
 */
@Component
public class SimulationListener {

    private static final Logger log = LoggerFactory.getLogger(SimulationListener.class);

    /** Servicio que contiene la lógica de ejecución de la simulación. */
    private final SimulationService simulationService;

    /** Repositorio para actualizar el estado de la solicitud en caso de error. */
    private final SolicitudRepository solicitudRepository;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param simulationService   servicio de simulación a invocar al recibir cada mensaje
     * @param solicitudRepository repositorio de solicitudes para actualizar el estado
     */
    public SimulationListener(SimulationService simulationService,
                              SolicitudRepository solicitudRepository) {
        this.simulationService = simulationService;
        this.solicitudRepository = solicitudRepository;
    }

    /**
     * Método invocado automáticamente cada vez que llega un mensaje
     * a la cola {@code simulation.queue}.
     *
     * <p>Extrae los datos del mensaje y lanza la simulación mediante
     * {@link SimulationService#executeSimulation}. Si la simulación lanza
     * una excepción, el estado de la solicitud se cambia a {@code ERROR}
     * en base de datos y el error se registra en el log, pero no se
     * propaga para evitar que el listener quede bloqueado.</p>
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
            log.error("Error procesando simulación para solicitud ID: {}. " +
                    "Actualizando estado a ERROR.", message.getSolicitudId(), e);
            solicitudRepository.findById(message.getSolicitudId())
                    .ifPresent(solicitud -> {
                        solicitud.setEstado("ERROR");
                        solicitudRepository.save(solicitud);
                    });
        }
    }
}
