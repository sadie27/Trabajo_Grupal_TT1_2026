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
 * Servicio encargado de gestionar el ciclo de vida de las solicitudes de simulación.
 * Permite crear nuevas solicitudes, comprobar su estado y listar las solicitudes de un usuario.
 * Al crear una solicitud, la encola en RabbitMQ para su procesamiento asíncrono.
 *
 * @author Lucas, Ana, Clara, Santiago
 * @version 1.0
 */
@Service
public class SolicitudService {

    /** Repositorio para persistir y consultar solicitudes en la base de datos. */
    private final SolicitudRepository solicitudRepository;

    /** Template de RabbitMQ usado para publicar mensajes de simulación en la cola. */
    private final RabbitTemplate rabbitTemplate;

    /**
     * Crea el servicio inyectando el repositorio de solicitudes y el template de RabbitMQ.
     *
     * @param solicitudRepository el repositorio para acceder a los datos de las solicitudes
     * @param rabbitTemplate      el template para publicar mensajes en RabbitMQ
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    public SolicitudService(SolicitudRepository solicitudRepository, RabbitTemplate rabbitTemplate) {
        this.solicitudRepository = solicitudRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Comprueba el estado de una solicitud de simulación específica.
     * Devuelve una lista de dos enteros: [1,0] si está finalizada, [0,1] si sigue procesándose,
     * o [0,0] si no se encontró la solicitud o no pertenece al usuario.
     *
     * @param nombreUsuario el nombre del usuario propietario de la solicitud
     * @param tok           el token numérico de la solicitud a comprobar
     * @return lista con dos enteros que codifican el estado de la solicitud
     * @throws IllegalArgumentException si el nombre de usuario o el token son nulos o vacíos
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
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
     * Devuelve la lista de tokens de todas las solicitudes de simulación que ha hecho un usuario.
     *
     * @param nombreUsuario el nombre del usuario del que se quieren listar las solicitudes
     * @return lista con los tokens numéricos de las solicitudes del usuario, vacía si no tiene ninguna
     * @throws IllegalArgumentException si el nombre de usuario es nulo o vacío
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
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
     * Crea una nueva solicitud de simulación, le asigna un token único de 5 dígitos,
     * la guarda en la base de datos y la encola en RabbitMQ para su procesamiento asíncrono.
     *
     * @param nombreUsuario el nombre del usuario que realiza la solicitud
     * @param solicitud     los datos de la solicitud: nombres de entidades y cantidades iniciales
     * @return un {@link org.trabajott1.model.SolicitudResponse} con el token asignado y {@code done = true}
     * @throws IllegalArgumentException si el nombre de usuario o la solicitud son nulos o vacíos
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
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
