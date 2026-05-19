package org.trabajott1.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.trabajott1.model.Solicitud;
import org.trabajott1.model.SolicitudResponse;
import org.trabajott1.persistence.entity.SolicitudEntity;
import org.trabajott1.repository.SolicitudRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests extendidos para {@link SolicitudService}.
 * Complementa {@link SolicitudServiceTest} cubriendo: colisión de token en el bucle de
 * generación, fallo de publicación en RabbitMQ y rollback transaccional ante excepción
 * en el save.
 *
 * @author Lucas, Ana, Clara, Santiago
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
class SolicitudServiceExtendedTest {

    @Mock
    private SolicitudRepository solicitudRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private SolicitudService solicitudService;

    // ── Colisión de token ─────────────────────────────────────────────────────

    /**
     * Verifica que cuando el primer token generado ya existe en la BD, el bucle itera
     * y asigna el siguiente token libre. El token final devuelto está en el rango [10000, 99999].
     *
     * Comportamiento: findByTokenSolicitud devuelve Optional.of(...) la primera vez
     * (token ocupado) y Optional.empty() la segunda (token libre).
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void crearSolicitud_TokenCollision_RetriesUntilUnique() {
        Solicitud solicitud = new Solicitud();
        solicitud.setNombreEntidades(
                org.openapitools.jackson.nullable.JsonNullable.of(List.of("A")));
        solicitud.setCantidadesIniciales(
                org.openapitools.jackson.nullable.JsonNullable.of(List.of(5)));

        SolicitudEntity existente = new SolicitudEntity();
        SolicitudEntity guardada  = new SolicitudEntity();
        guardada.setIdSolicitud(1);

        // Primera llamada: token ocupado. Segunda: libre.
        when(solicitudRepository.findByTokenSolicitud(anyInt()))
                .thenReturn(Optional.of(existente))
                .thenReturn(Optional.empty());
        when(solicitudRepository.save(any(SolicitudEntity.class))).thenReturn(guardada);

        SolicitudResponse response = solicitudService.crearSolicitud("user", solicitud);

        assertTrue(response.getDone());
        int token = response.getTokenSolicitud();
        assertTrue(token >= 10000 && token <= 99999,
                "El token debe estar en el rango [10000, 99999], fue: " + token);

        // findByTokenSolicitud debe haberse llamado al menos dos veces (colisión + éxito)
        verify(solicitudRepository, atLeast(2)).findByTokenSolicitud(anyInt());
        verify(solicitudRepository).save(any(SolicitudEntity.class));
    }

    /**
     * Verifica que aunque ocurran múltiples colisiones consecutivas, el bucle sigue
     * intentando hasta encontrar un token libre.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void crearSolicitud_MultipleCollisions_EventuallySucceeds() {
        Solicitud solicitud = new Solicitud();
        solicitud.setNombreEntidades(
                org.openapitools.jackson.nullable.JsonNullable.of(List.of("A")));
        solicitud.setCantidadesIniciales(
                org.openapitools.jackson.nullable.JsonNullable.of(List.of(1)));

        SolicitudEntity existente = new SolicitudEntity();
        SolicitudEntity guardada  = new SolicitudEntity();
        guardada.setIdSolicitud(2);

        // 4 colisiones seguidas, luego libre
        when(solicitudRepository.findByTokenSolicitud(anyInt()))
                .thenReturn(Optional.of(existente))
                .thenReturn(Optional.of(existente))
                .thenReturn(Optional.of(existente))
                .thenReturn(Optional.of(existente))
                .thenReturn(Optional.empty());
        when(solicitudRepository.save(any(SolicitudEntity.class))).thenReturn(guardada);

        SolicitudResponse response = solicitudService.crearSolicitud("user", solicitud);

        assertTrue(response.getDone());
        verify(solicitudRepository, times(5)).findByTokenSolicitud(anyInt());
    }

    // ── Fallo de RabbitMQ ─────────────────────────────────────────────────────

    /**
     * Verifica que si RabbitMQ lanza {@link AmqpException} al publicar el mensaje,
     * la excepción se propaga fuera de crearSolicitud (la solicitud ya fue guardada
     * en BD pero la transacción se marca para rollback).
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void crearSolicitud_RabbitMQFailure_ThrowsAmqpException() {
        Solicitud solicitud = new Solicitud();
        solicitud.setNombreEntidades(
                org.openapitools.jackson.nullable.JsonNullable.of(List.of("A")));
        solicitud.setCantidadesIniciales(
                org.openapitools.jackson.nullable.JsonNullable.of(List.of(5)));

        SolicitudEntity guardada = new SolicitudEntity();
        guardada.setIdSolicitud(3);

        when(solicitudRepository.findByTokenSolicitud(anyInt())).thenReturn(Optional.empty());
        when(solicitudRepository.save(any(SolicitudEntity.class))).thenReturn(guardada);
        doThrow(new AmqpException("Broker no disponible"))
                .when(rabbitTemplate).convertAndSend(anyString(), anyString(), any(Object.class));

        assertThrows(AmqpException.class,
                () -> solicitudService.crearSolicitud("user", solicitud));

        // El save sí se invocó antes de que fallara RabbitMQ
        verify(solicitudRepository).save(any(SolicitudEntity.class));
    }

    // ── Rollback transaccional ────────────────────────────────────────────────

    /**
     * Verifica que si el repositorio lanza una {@link RuntimeException} durante el save,
     * la excepción se propaga y el método no llega a publicar en RabbitMQ
     * (la transacción queda sin completar).
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void crearSolicitud_SaveThrows_RabbitMQNeverCalled() {
        Solicitud solicitud = new Solicitud();
        solicitud.setNombreEntidades(
                org.openapitools.jackson.nullable.JsonNullable.of(List.of("A")));
        solicitud.setCantidadesIniciales(
                org.openapitools.jackson.nullable.JsonNullable.of(List.of(5)));

        when(solicitudRepository.findByTokenSolicitud(anyInt())).thenReturn(Optional.empty());
        when(solicitudRepository.save(any(SolicitudEntity.class)))
                .thenThrow(new RuntimeException("Error de BD"));

        assertThrows(RuntimeException.class,
                () -> solicitudService.crearSolicitud("user", solicitud));

        verifyNoInteractions(rabbitTemplate);
    }

    /**
     * Verifica que si la solicitud es null, se lanza {@link IllegalArgumentException}
     * antes de llegar al repositorio ni a RabbitMQ.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void crearSolicitud_NullSolicitud_ThrowsBeforeAnyInteraction() {
        assertThrows(IllegalArgumentException.class,
                () -> solicitudService.crearSolicitud("user", null));

        verifyNoInteractions(solicitudRepository, rabbitTemplate);
    }

    /**
     * Verifica que si el usuario es null, se lanza {@link IllegalArgumentException}
     * antes de llegar al repositorio ni a RabbitMQ.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void crearSolicitud_NullUsername_ThrowsBeforeAnyInteraction() {
        assertThrows(IllegalArgumentException.class,
                () -> solicitudService.crearSolicitud(null, new Solicitud()));

        verifyNoInteractions(solicitudRepository, rabbitTemplate);
    }
}
