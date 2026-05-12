package org.trabajott1.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trabajott1.model.ResultsResponse;
import org.trabajott1.persistence.entity.ResultadoEntity;
import org.trabajott1.persistence.entity.SolicitudEntity;
import org.trabajott1.repository.ResultadoRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResultadosServiceTest {

    @Mock
    private ResultadoRepository resultadoRepository;

    @InjectMocks
    private ResultadosService resultadosService;

    @Test
    void obtenerResultados_NullUsername_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> resultadosService.obtenerResultados(null, 123));
    }

    @Test
    void obtenerResultados_EmptyUsername_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> resultadosService.obtenerResultados("", 123));
    }

    @Test
    void obtenerResultados_NullToken_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> resultadosService.obtenerResultados("user", null));
    }

    @Test
    void obtenerResultados_Success() {
        String username = "testUser";
        Integer token = 12345;
        String data = "some simulation results";

        SolicitudEntity solicitud = new SolicitudEntity();
        solicitud.setNombreUsuario(username);
        solicitud.setTokenSolicitud(token);

        ResultadoEntity resultado = new ResultadoEntity();
        resultado.setSolicitud(solicitud);
        resultado.setDatosResultado(data);

        when(resultadoRepository.findBySolicitud_TokenSolicitud(token)).thenReturn(Optional.of(resultado));

        ResultsResponse response = resultadosService.obtenerResultados(username, token);

        assertTrue(response.getDone());
        assertEquals(token, response.getTokenSolicitud());
        assertEquals(org.openapitools.jackson.nullable.JsonNullable.of(data), response.getData());
        assertNull(response.getErrorMessage().orElse(null));
    }

    @Test
    void obtenerResultados_WrongUser() {
        String username = "testUser";
        Integer token = 12345;

        SolicitudEntity solicitud = new SolicitudEntity();
        solicitud.setNombreUsuario("otherUser");
        solicitud.setTokenSolicitud(token);

        ResultadoEntity resultado = new ResultadoEntity();
        resultado.setSolicitud(solicitud);

        when(resultadoRepository.findBySolicitud_TokenSolicitud(token)).thenReturn(Optional.of(resultado));

        ResultsResponse response = resultadosService.obtenerResultados(username, token);

        assertFalse(response.getDone());
        assertEquals(token, response.getTokenSolicitud());
        assertTrue(response.getErrorMessage().isPresent());
    }

    @Test
    void obtenerResultados_NotFound() {
        String username = "testUser";
        Integer token = 12345;

        when(resultadoRepository.findBySolicitud_TokenSolicitud(token)).thenReturn(Optional.empty());

        ResultsResponse response = resultadosService.obtenerResultados(username, token);

        assertFalse(response.getDone());
        assertEquals(token, response.getTokenSolicitud());
        assertTrue(response.getErrorMessage().isPresent());
    }
}
