package org.trabajott1.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.trabajott1.model.Solicitud;

/**
 * DTO Wrapper para manejar la estructura JSON que envía el cliente.
 * El cliente puede enviar el cuerpo como: {@code {"solicitud": {"cantidadesIniciales": [...], ...}}}.
 * Este wrapper desempaqueta el objeto {@link org.trabajott1.model.Solicitud} anidado.
 *
 * @author Lucas, Ana, Clara, Santiago
 * @version 1.0
 */
public class SolicitudWrapper {

    @JsonProperty("solicitud")
    private Solicitud solicitud;

    /**
     * Devuelve la solicitud extraída del cuerpo JSON.
     *
     * @return el objeto {@link Solicitud} con los datos de la simulación
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    public Solicitud getSolicitud() {
        return solicitud;
    }

    /**
     * Establece la solicitud en el wrapper.
     *
     * @param solicitud el objeto {@link Solicitud} a guardar en el wrapper
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    public void setSolicitud(Solicitud solicitud) {
        this.solicitud = solicitud;
    }
}