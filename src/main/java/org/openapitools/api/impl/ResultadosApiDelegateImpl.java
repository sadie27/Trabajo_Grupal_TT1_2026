package org.openapitools.api.impl;

import org.openapitools.api.ResultadosApiDelegate;
import org.openapitools.model.ResultsResponse;
import org.openapitools.service.ResultadosService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ResultadosApiDelegateImpl implements ResultadosApiDelegate {

    private final ResultadosService resultadosService;

    public ResultadosApiDelegateImpl(ResultadosService resultadosService) {
        this.resultadosService = resultadosService;
    }

    @Override
    public ResponseEntity<ResultsResponse> resultadosPost(String nombreUsuario, Integer tok) {
        ResultsResponse response = resultadosService.obtenerResultados(nombreUsuario, tok);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
