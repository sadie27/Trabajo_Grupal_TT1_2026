package org.trabajott1.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.trabajott1.configuration.RabbitMQConfig;
import org.trabajott1.model.SimulationMessage;
import org.trabajott1.model.Solicitud;
import org.trabajott1.model.SolicitudResponse;
import org.trabajott1.persistence.entity.SolicitudEntity;
import org.trabajott1.repository.SolicitudRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SolicitudServiceTest {

    @Mock
    private SolicitudRepository solicitudRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private SolicitudService solicitudService;

    @Test
    void comprobarSolicitud_InvalidInputs() {
        assertThrows(IllegalArgumentException.class, () -> solicitudService.comprobarSolicitud(null, 123));
        assertThrows(IllegalArgumentException.class, () -> solicitudService.comprobarSolicitud("user", null));
    }

    @Test
    void comprobarSolicitud_NotFound() {
        when(solicitudRepository.findByTokenSolicitud(123)).thenReturn(Optional.empty());
        List<Integer> result = solicitudService.comprobarSolicitud("user", 123);
        assertEquals(List.of(0, 0), result);
    }

    @Test
    void comprobarSolicitud_Finalizada() {
        SolicitudEntity entity = new SolicitudEntity();
        entity.setNombreUsuario("user");
        entity.setEstado("FINALIZADA");
        when(solicitudRepository.findByTokenSolicitud(123)).thenReturn(Optional.of(entity));

        List<Integer> result = solicitudService.comprobarSolicitud("user", 123);
        assertEquals(List.of(1, 0), result);
    }

    @Test
    void comprobarSolicitud_Procesando() {
        SolicitudEntity entity = new SolicitudEntity();
        entity.setNombreUsuario("user");
        entity.setEstado("PROCESANDO");
        when(solicitudRepository.findByTokenSolicitud(123)).thenReturn(Optional.of(entity));

        List<Integer> result = solicitudService.comprobarSolicitud("user", 123);
        assertEquals(List.of(0, 1), result);
    }

    @Test
    void getSolicitudesUsuario_Success() {
        SolicitudEntity s1 = new SolicitudEntity();
        s1.setTokenSolicitud(10001);
        SolicitudEntity s2 = new SolicitudEntity();
        s2.setTokenSolicitud(10002);

        when(solicitudRepository.findByNombreUsuario("user")).thenReturn(List.of(s1, s2));

        List<Integer> result = solicitudService.getSolicitudesUsuario("user");
        assertEquals(List.of(10001, 10002), result);
    }

    @Test
    void crearSolicitud_Success() {
        String username = "user";
        Solicitud solicitud = new Solicitud();
        solicitud.setNombreEntidades(org.openapitools.jackson.nullable.JsonNullable.of(List.of("A", "B")));
        solicitud.setCantidadesIniciales(org.openapitools.jackson.nullable.JsonNullable.of(List.of(5, 10)));

        SolicitudEntity savedEntity = new SolicitudEntity();
        savedEntity.setIdSolicitud(1);
        when(solicitudRepository.save(any(SolicitudEntity.class))).thenReturn(savedEntity);

        SolicitudResponse response = solicitudService.crearSolicitud(username, solicitud);

        assertTrue(response.getDone());
        assertNotNull(response.getTokenSolicitud());
        assertNull(response.getErrorMessage().orElse(null));
        verify(solicitudRepository).save(any(SolicitudEntity.class));
        verify(rabbitTemplate).convertAndSend(eq(RabbitMQConfig.SIMULATION_EXCHANGE), eq(RabbitMQConfig.SIMULATION_ROUTING_KEY), any(SimulationMessage.class));
    }
}
