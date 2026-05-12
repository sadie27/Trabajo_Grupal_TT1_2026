package org.trabajott1.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.trabajott1.configuration.RabbitMQConfig;
import org.trabajott1.model.SimulationMessage;
import org.trabajott1.model.Solicitud;
import org.trabajott1.model.SolicitudResponse;
import org.trabajott1.persistence.entity.SolicitudEntity;
import org.trabajott1.repository.SolicitudRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Servicio para gestionar las solicitudes de simulación.
 */
@Service
public class SolicitudService {

    private final SolicitudRepository solicitudRepository;
    private final RabbitTemplate rabbitTemplate;

    /**
     * Constructor de SolicitudService.
     *
     * @param solicitudRepository El repositorio de solicitudes.
     * @param rabbitTemplate      El template de RabbitMQ para el envío de mensajes.
     */
    public SolicitudService(SolicitudRepository solicitudRepository, RabbitTemplate rabbitTemplate) {
        this.solicitudRepository = solicitudRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Comprueba el estado de una solicitud específica.
     *
     * @param nombreUsuario El nombre del usuario.
     * @param tok           El token de la solicitud.
     * @return Una lista de enteros que representa el estado (finalizada, en proceso, no encontrada).
     * @throws IllegalArgumentException Si los parámetros obligatorios son nulos o vacíos.
     */
    public List<Integer> comprobarSolicitud(String nombreUsuario, Integer tok) {
        if (nombreUsuario == null || nombreUsuario.isEmpty()) {
            throw new IllegalArgumentException("nombreUsuario es obligatorio");
        }
        if (tok == null) {
            throw new IllegalArgumentException("tok es obligatorio");
        }

        Optional<SolicitudEntity> solicitud = solicitudRepository.findByTokenSolicitud(tok);
        
        if (solicitud.isPresent() && solicitud.get().getNombreUsuario().equals(nombreUsuario)) {
            if ("FINALIZADA".equals(solicitud.get().getEstado())) {
                return List.of(1, 0);
            } else {
                return List.of(0, 1);
            }
        }
        return List.of(0, 0);
    }

    /**
     * Obtiene los tokens de todas las solicitudes de un usuario.
     *
     * @param nombreUsuario El nombre del usuario.
     * @return Una lista con los tokens de las solicitudes.
     * @throws IllegalArgumentException Si el nombre de usuario es obligatorio pero no se proporciona.
     */
    public List<Integer> getSolicitudesUsuario(String nombreUsuario) {
        if (nombreUsuario == null || nombreUsuario.isEmpty()) {
            throw new IllegalArgumentException("nombreUsuario es obligatorio");
        }

        return solicitudRepository.findByNombreUsuario(nombreUsuario).stream()
                .map(SolicitudEntity::getTokenSolicitud)
                .collect(Collectors.toList());
    }

    /**
     * Crea una nueva solicitud de simulación y la encola para su procesamiento.
     *
     * @param nombreUsuario El nombre del usuario que realiza la solicitud.
     * @param solicitud     Los datos de la solicitud.
     * @return Un objeto SolicitudResponse con el token generado.
     * @throws IllegalArgumentException Si los parámetros obligatorios son nulos o vacíos.
     */
    @Transactional
    public SolicitudResponse crearSolicitud(String nombreUsuario, Solicitud solicitud) {
        if (nombreUsuario == null || nombreUsuario.isEmpty()) {
            throw new IllegalArgumentException("nombreUsuario es obligatorio");
        }
        if (solicitud == null) {
            throw new IllegalArgumentException("solicitud es obligatoria");
        }

        SolicitudEntity entity = new SolicitudEntity();
        entity.setNombreUsuario(nombreUsuario);
        
        int tokenGenerado;
        Random random = new Random();
        boolean unico;
        do {
            tokenGenerado = 10000 + random.nextInt(90000);
            unico = solicitudRepository.findByTokenSolicitud(tokenGenerado).isEmpty();
        } while (!unico);
        
        entity.setTokenSolicitud(tokenGenerado);
        entity.setEstado("PROCESANDO");

        List<String> nombres = solicitud.getNombreEntidades() != null ? solicitud.getNombreEntidades().orElse(null) : null;
        List<Integer> cantidades = solicitud.getCantidadesIniciales() != null ? solicitud.getCantidadesIniciales().orElse(null) : null;
        
        entity.addEntidades(nombres, cantidades);

        final SolicitudEntity savedEntity = solicitudRepository.save(entity);

        // Enviar a RabbitMQ en lugar de llamar al servicio asíncrono directamente
        if (nombres != null && cantidades != null) {
            SimulationMessage message = new SimulationMessage(savedEntity.getIdSolicitud(), nombres, cantidades);
            rabbitTemplate.convertAndSend(RabbitMQConfig.SIMULATION_EXCHANGE, RabbitMQConfig.SIMULATION_ROUTING_KEY, message);
        }

        return new SolicitudResponse()
                .done(true)
                .tokenSolicitud(tokenGenerado)
                .data(true)
                .errorMessage(null);
    }
}
