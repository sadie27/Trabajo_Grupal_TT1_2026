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
 *
 * <p>Cubre los tres escenarios principales: resultado encontrado con
 * usuario correcto, token inexistente y token de otro usuario.</p>
 */
@ExtendWith(MockitoExtension.class)
class ResultadosServiceTest {

    @Mock
    private ResultadoRepository resultadoRepository;

    @InjectMocks
    private ResultadosService resultadosService;

    // ---- Caso exitoso ----

    @Test
    void obtenerResultados_usuarioYTokenCorrectos_devuelveResultado() {
        SolicitudEntity solicitud = new SolicitudEntity();
        solicitud.setNombreUsuario("alice");
        solicitud.setTokenSolicitud(1001);

        ResultadoEntity resultado = new ResultadoEntity();
        resultado.setSolicitud(solicitud);
        resultado.setDatosResultado("8\n0,0,0,red\n");

        when(resultadoRepository.findBySolicitud_TokenSolicitud(1001))
                .thenReturn(Optional.of(resultado));

        ResultsResponse response = resultadosService.obtenerResultados("alice", 1001);

        assertTrue(response.getDone());
        assertEquals(1001, response.getTokenSolicitud());
        assertEquals("8\n0,0,0,red\n", response.getData());
        assertNull(response.getErrorMessage());
    }

    // ---- Token inexistente ----

    @Test
    void obtenerResultados_tokenInexistente_devuelveErrorMessage() {
        when(resultadoRepository.findBySolicitud_TokenSolicitud(9999))
                .thenReturn(Optional.empty());

        ResultsResponse response = resultadosService.obtenerResultados("alice", 9999);

        assertFalse(response.getDone());
        assertNotNull(response.getErrorMessage());
    }

    // ---- Usuario incorrecto ----

    @Test
    void obtenerResultados_usuarioIncorrecto_devuelveErrorMessage() {
        SolicitudEntity solicitud = new SolicitudEntity();
        solicitud.setNombreUsuario("bob");
        solicitud.setTokenSolicitud(2002);

        ResultadoEntity resultado = new ResultadoEntity();
        resultado.setSolicitud(solicitud);
        resultado.setDatosResultado("datos");

        when(resultadoRepository.findBySolicitud_TokenSolicitud(2002))
                .thenReturn(Optional.of(resultado));

        ResultsResponse response = resultadosService.obtenerResultados("alice", 2002);

        assertFalse(response.getDone());
        assertNotNull(response.getErrorMessage());
    }

    // ---- Validaciones de entrada ----

    @Test
    void obtenerResultados_nombreUsuarioNulo_lanzaIllegalArgument() {
        assertThrows(IllegalArgumentException.class,
                () -> resultadosService.obtenerResultados(null, 1001));
    }

    @Test
    void obtenerResultados_nombreUsuarioVacio_lanzaIllegalArgument() {
        assertThrows(IllegalArgumentException.class,
                () -> resultadosService.obtenerResultados("", 1001));
    }

    @Test
    void obtenerResultados_tokenNulo_lanzaIllegalArgument() {
        assertThrows(IllegalArgumentException.class,
                () -> resultadosService.obtenerResultados("alice", null));
    }
}
