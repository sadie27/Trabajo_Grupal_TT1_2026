package org.trabajott1.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.trabajott1.model.Solicitud;

/**
 * DTO Wrapper para manejar la estructura JSON que envía el cliente.
 * El cliente envía: {"solicitud": {"cantidadesIniciales": [...], ...}}
 * Este wrapper desempaqueta el objeto anidado.
 */
public class SolicitudWrapper {

    @JsonProperty("solicitud")
    private Solicitud solicitud;

    public Solicitud getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(Solicitud solicitud) {
        this.solicitud = solicitud;
    }
}