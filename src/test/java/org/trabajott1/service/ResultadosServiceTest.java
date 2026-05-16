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

/**
 * Tests unitarios para {@link ResultadosService}.
 * Verifica la validación de parámetros y los distintos escenarios de consulta de resultados:
 * usuario correcto con resultado, usuario incorrecto y resultado no encontrado.
 *
 * @author Lucas, Ana, Clara, Santiago
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
class ResultadosServiceTest {

    @Mock
    private ResultadoRepository resultadoRepository;

    @InjectMocks
    private ResultadosService resultadosService;

    /**
     * Verifica que un nombre de usuario nulo lanza {@link IllegalArgumentException}.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void obtenerResultados_NullUsername_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> resultadosService.obtenerResultados(null, 123));
    }

    /**
     * Verifica que un nombre de usuario vacío lanza {@link IllegalArgumentException}.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void obtenerResultados_EmptyUsername_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> resultadosService.obtenerResultados("", 123));
    }

    /**
     * Verifica que un token nulo lanza {@link IllegalArgumentException}.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void obtenerResultados_NullToken_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> resultadosService.obtenerResultados("user", null));
    }

    /**
     * Verifica que cuando la solicitud existe y el usuario es correcto, se devuelven los datos de la simulación.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
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

    /**
     * Verifica que cuando el usuario no coincide con el propietario de la solicitud, se devuelve done=false.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
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

    /**
     * Verifica que cuando la solicitud no existe en la base de datos, se devuelve done=false con mensaje de error.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
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
