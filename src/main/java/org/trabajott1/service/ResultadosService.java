package org.trabajott1.service;

import org.trabajott1.model.ResultsResponse;
import org.springframework.stereotype.Service;

@Service
public class ResultadosService {

    public ResultsResponse obtenerResultados(String nombreUsuario, Integer tok) {
        // validaciones
        if (nombreUsuario == null || nombreUsuario.isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario es obligatorio");
        }

        if (tok == null) {
            throw new IllegalArgumentException("El token es obligatorio");
        }

        // lógica real

        return new ResultsResponse()
                .done(true)
                .tokenSolicitud(tok)
                .data("Resultados para usuario: " + nombreUsuario)
                .errorMessage(null);
    }
}
