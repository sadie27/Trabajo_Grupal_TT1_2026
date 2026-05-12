package org.trabajott1.service;

import org.trabajott1.model.ResultsResponse;
import org.trabajott1.persistence.entity.ResultadoEntity;
import org.trabajott1.repository.ResultadoRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Servicio para la consulta de resultados de simulaciones.
 *
 * <p>Expone la operación de negocio principal: dado un nombre de usuario
 * y un token de solicitud, recupera los datos de simulación almacenados
 * en base de datos y los devuelve encapsulados en un {@link ResultsResponse}.</p>
 *
 * <p>Aplica validación de propiedad: aunque el token exista, solo el usuario
 * que creó la solicitud puede consultar sus resultados.</p>
 */
@Service
public class ResultadosService {

    private final ResultadoRepository resultadoRepository;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param resultadoRepository repositorio JPA para acceder a {@link ResultadoEntity}
     */
    public ResultadosService(ResultadoRepository resultadoRepository) {
        this.resultadoRepository = resultadoRepository;
    }

    /**
     * Obtiene el resultado de una simulación identificada por token,
     * verificando que pertenece al usuario indicado.
     *
     * <p>Flujo de validación:</p>
     * <ol>
     *   <li>Comprueba que {@code nombreUsuario} no sea nulo ni vacío.</li>
     *   <li>Comprueba que {@code tok} no sea nulo.</li>
     *   <li>Busca el resultado por token en base de datos.</li>
     *   <li>Si existe, verifica que el usuario de la solicitud coincida.</li>
     * </ol>
     *
     * @param nombreUsuario nombre del usuario que solicita los resultados; no puede ser nulo ni vacío
     * @param tok           token único de la solicitud; no puede ser nulo
     * @return {@link ResultsResponse} con {@code done=true} y los datos si todo es correcto,
     *         o con {@code done=false} y un mensaje de error en caso contrario
     * @throws IllegalArgumentException si {@code nombreUsuario} es nulo/vacío o {@code tok} es nulo
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
