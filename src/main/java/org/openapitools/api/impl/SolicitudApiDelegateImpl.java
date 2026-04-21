package org.openapitools.api.impl;

import org.openapitools.api.SolicitudApiDelegate;
import org.openapitools.model.Solicitud;
import org.openapitools.model.SolicitudResponse;
import org.openapitools.service.SolicitudService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SolicitudApiDelegateImpl implements SolicitudApiDelegate {

    private final SolicitudService solicitudService;

    public SolicitudApiDelegateImpl(SolicitudService solicitudService) {
        this.solicitudService = solicitudService;
    }

    @Override
    public ResponseEntity<List<Integer>> solicitudComprobarSolicitudGet(String nombreUsuario, Integer tok) {
        List<Integer> result = solicitudService.comprobarSolicitud(nombreUsuario, tok);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(result);
    }

    @Override
    public ResponseEntity<List<Integer>> solicitudGetSolicitudesUsuarioGet(String nombreUsuario) {
        List<Integer> result = solicitudService.getSolicitudesUsuario(nombreUsuario);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(result);
    }

    @Override
    public ResponseEntity<SolicitudResponse> solicitudSolicitarPost(String nombreUsuario, Solicitud solicitud) {
        SolicitudResponse response = solicitudService.crearSolicitud(nombreUsuario, solicitud);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
