package org.trabajott1.api.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.trabajott1.model.Solicitud;
import org.trabajott1.model.SolicitudResponse;
import org.trabajott1.service.SolicitudService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

/**
 * Tests unitarios para {@link SolicitudApiDelegateImpl}.
 * Verifica que el delegate devuelve los códigos HTTP correctos para cada endpoint de solicitud.
 *
 * @author Lucas, Ana, Clara, Santiago
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
class SolicitudApiDelegateImplTest {

    @Mock
    private SolicitudService solicitudService;

    private SolicitudApiDelegateImpl solicitudApiDelegate;

    /**
     * Inicializa el delegate con el mock del servicio antes de cada test.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @BeforeEach
    void setUp() {
        solicitudApiDelegate = new SolicitudApiDelegateImpl(solicitudService);
    }

    /**
     * Verifica que al comprobar una solicitud existente se devuelve HTTP 200 con el estado correcto.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void solicitudComprobarSolicitudGet_Success() {
        List<Integer> mockResult = List.of(1, 0);
        when(solicitudService.comprobarSolicitud("user1", 12345)).thenReturn(mockResult);

        ResponseEntity<List<Integer>> response = solicitudApiDelegate.solicitudComprobarSolicitudGet("user1", 12345);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResult, response.getBody());
    }

    /**
     * Verifica que si el servicio lanza excepción al comprobar la solicitud, el delegate devuelve HTTP 400.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void solicitudComprobarSolicitudGet_BadRequest() {
        when(solicitudService.comprobarSolicitud(any(), any())).thenThrow(new IllegalArgumentException("Error"));

        ResponseEntity<List<Integer>> response = solicitudApiDelegate.solicitudComprobarSolicitudGet(null, null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * Verifica que al obtener las solicitudes de un usuario se devuelve HTTP 200 con la lista de tokens.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void solicitudGetSolicitudesUsuarioGet_Success() {
        List<Integer> mockResult = List.of(12345, 67890);
        when(solicitudService.getSolicitudesUsuario("user1")).thenReturn(mockResult);

        ResponseEntity<List<Integer>> response = solicitudApiDelegate.solicitudGetSolicitudesUsuarioGet("user1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResult, response.getBody());
    }

    /**
     * Verifica que si el servicio lanza excepción al listar solicitudes, el delegate devuelve HTTP 400.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void solicitudGetSolicitudesUsuarioGet_BadRequest() {
        when(solicitudService.getSolicitudesUsuario(any())).thenThrow(new IllegalArgumentException("Error"));

        ResponseEntity<List<Integer>> response = solicitudApiDelegate.solicitudGetSolicitudesUsuarioGet(null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * Verifica que al crear una solicitud con datos válidos se devuelve HTTP 201 con el token asignado.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void solicitudSolicitarPost_Success() {
        Solicitud solicitud = new Solicitud();
        SolicitudResponse mockResponse = new SolicitudResponse().done(true).tokenSolicitud(12345);
        when(solicitudService.crearSolicitud("user1", solicitud)).thenReturn(mockResponse);

        ResponseEntity<SolicitudResponse> response = solicitudApiDelegate.solicitudSolicitarPost("user1", solicitud);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());
    }

    /**
     * Verifica que si el servicio lanza excepción al crear la solicitud, el delegate devuelve HTTP 400.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void solicitudSolicitarPost_BadRequest() {
        when(solicitudService.crearSolicitud(any(), any())).thenThrow(new IllegalArgumentException("Error"));

        ResponseEntity<SolicitudResponse> response = solicitudApiDelegate.solicitudSolicitarPost(null, null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
