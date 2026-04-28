package org.trabajott1.service;

import org.trabajott1.model.ResultsResponse;
import org.trabajott1.persistence.entity.ResultadoEntity;
import org.trabajott1.repository.ResultadoRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ResultadosService {

    private final ResultadoRepository resultadoRepository;

    public ResultadosService(ResultadoRepository resultadoRepository) {
        this.resultadoRepository = resultadoRepository;
    }

    public ResultsResponse obtenerResultados(String nombreUsuario, Integer tok) {
        if (nombreUsuario == null || nombreUsuario.isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario es obligatorio");
        }

        if (tok == null) {
            throw new IllegalArgumentException("El token es obligatorio");
        }

        Optional<ResultadoEntity> resultado = resultadoRepository.findBySolicitud_TokenSolicitud(tok);

        if (resultado.isPresent() && resultado.get().getSolicitud().getNombreUsuario().equals(nombreUsuario)) {
            return new ResultsResponse()
                    .done(true)
                    .tokenSolicitud(tok)
                    .data(resultado.get().getDatosResultado())
                    .errorMessage(null);
        }

        return new ResultsResponse()
                .done(false)
                .tokenSolicitud(tok)
                .errorMessage("No se encontraron resultados para la solicitud y usuario especificados");
    }
}
