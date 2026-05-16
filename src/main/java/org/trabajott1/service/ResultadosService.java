package org.trabajott1.service;

import org.trabajott1.model.ResultsResponse;
import org.trabajott1.persistence.entity.ResultadoEntity;
import org.trabajott1.repository.ResultadoRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Servicio encargado de consultar y devolver los resultados de las simulaciones procesadas.
 * Verifica que el usuario sea el propietario de la solicitud antes de devolver los datos.
 *
 * @author Lucas, Ana, Clara, Santiago
 * @version 1.0
 */
@Service
public class ResultadosService {

    /** Repositorio para buscar los resultados de simulación en la base de datos. */
    private final ResultadoRepository resultadoRepository;

    /**
     * Crea el servicio inyectando el repositorio de resultados.
     *
     * @param resultadoRepository el repositorio para acceder a los datos de los resultados
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    public ResultadosService(ResultadoRepository resultadoRepository) {
        this.resultadoRepository = resultadoRepository;
    }

    /**
     * Consulta y devuelve los resultados de una simulación dado el usuario y el token de la solicitud.
     * Si la solicitud no pertenece al usuario o no existe, devuelve una respuesta con {@code done = false}.
     *
     * @param nombreUsuario el nombre del usuario que consulta los resultados
     * @param tok           el token numérico de la solicitud de simulación
     * @return un {@link ResultsResponse} con los datos si la solicitud existe y pertenece al usuario,
     *         o con un mensaje de error en caso contrario
     * @throws IllegalArgumentException si el nombre de usuario o el token son nulos o vacíos
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
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
