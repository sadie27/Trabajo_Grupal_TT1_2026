package org.trabajott1.api.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.trabajott1.api.SolicitudApiDelegate;
import org.trabajott1.model.Solicitud;
import org.trabajott1.model.SolicitudResponse;
import org.trabajott1.service.SolicitudService;

import java.util.List;

/**
 * Implementación del delegado para la API de gestión de solicitudes.
 */
@Service
public class SolicitudApiDelegateImpl implements SolicitudApiDelegate {

    private final SolicitudService solicitudService;

    /**
     * Constructor de SolicitudApiDelegateImpl.
     *
     * @param solicitudService El servicio de solicitudes a utilizar.
     */
    public SolicitudApiDelegateImpl(SolicitudService solicitudService) {
        this.solicitudService = solicitudService;
    }

    /**
     * Punto de entrada para comprobar el estado de una solicitud.
     *
     * @param nombreUsuario El nombre del usuario.
     * @param tok           El token de la solicitud.
     * @return Una respuesta con el estado de la solicitud.
     */
    @Override
    public ResponseEntity<List<Integer>> solicitudComprobarSolicitudGet(String nombreUsuario, Integer tok) {
        try {
            List<Integer> result = solicitudService.comprobarSolicitud(nombreUsuario, tok);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Punto de entrada para obtener todas las solicitudes de un usuario.
     *
     * @param nombreUsuario El nombre del usuario.
     * @return Una respuesta con la lista de tokens de las solicitudes del usuario.
     */
    @Override
    public ResponseEntity<List<Integer>> solicitudGetSolicitudesUsuarioGet(String nombreUsuario) {
        try {
            List<Integer> result = solicitudService.getSolicitudesUsuario(nombreUsuario);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Punto de entrada para crear una nueva solicitud de simulación.
     *
     * @param nombreUsuario El nombre del usuario.
     * @param solicitud     Los datos de la simulación a solicitar.
     * @return Una respuesta con el token generado para la nueva solicitud.
     */
    @Override
    public ResponseEntity<SolicitudResponse> solicitudSolicitarPost(String nombreUsuario, Solicitud solicitud) {
        try {
            SolicitudResponse response = solicitudService.crearSolicitud(nombreUsuario, solicitud);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
