package org.trabajott1.api.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.trabajott1.api.ResultadosApiDelegate;
import org.trabajott1.model.ResultsResponse;
import org.trabajott1.service.ResultadosService;

/**
 * Implementación del delegado para la API de obtención de resultados.
 */
@Service
public class ResultadosApiDelegateImpl implements ResultadosApiDelegate {

    private final ResultadosService resultadosService;

    /**
     * Constructor de ResultadosApiDelegateImpl.
     *
     * @param resultadosService El servicio de resultados a utilizar.
     */
    public ResultadosApiDelegateImpl(ResultadosService resultadosService) {
        this.resultadosService = resultadosService;
    }

    /**
     * Punto de entrada para obtener los resultados de una simulación.
     *
     * @param nombreUsuario El nombre del usuario.
     * @param tok           El token de la simulación.
     * @return Una respuesta con los resultados de la simulación.
     */
    @Override
    public ResponseEntity<ResultsResponse> resultadosPost(String nombreUsuario, Integer tok) {
        try {
            ResultsResponse response = resultadosService.obtenerResultados(nombreUsuario, tok);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
