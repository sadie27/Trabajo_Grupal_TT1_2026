package org.trabajott1.api.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.trabajott1.model.ResultsResponse;
import org.trabajott1.service.ResultadosService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResultadosApiDelegateImplTest {

    @Mock
    private ResultadosService resultadosService;

    private ResultadosApiDelegateImpl resultadosApiDelegate;

    @BeforeEach
    void setUp() {
        resultadosApiDelegate = new ResultadosApiDelegateImpl(resultadosService);
    }

    @Test
    void resultadosPost_Success() {
        String username = "user1";
        Integer token = 12345;
        ResultsResponse mockResponse = new ResultsResponse().done(true).tokenSolicitud(token);

        when(resultadosService.obtenerResultados(username, token)).thenReturn(mockResponse);

        ResponseEntity<ResultsResponse> response = resultadosApiDelegate.resultadosPost(username, token);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());
    }

    @Test
    void resultadosPost_BadRequest() {
        when(resultadosService.obtenerResultados(any(), any())).thenThrow(new IllegalArgumentException("Missing params"));

        ResponseEntity<ResultsResponse> response = resultadosApiDelegate.resultadosPost(null, null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
