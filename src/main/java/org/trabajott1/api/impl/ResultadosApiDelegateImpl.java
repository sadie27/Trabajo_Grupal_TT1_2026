package org.trabajott1.api.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.trabajott1.api.ResultadosApiDelegate;
import org.trabajott1.model.ResultsResponse;
import org.trabajott1.service.ResultadosService;

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
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
