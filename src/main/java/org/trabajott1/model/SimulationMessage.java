package org.trabajott1.model;

import java.io.Serializable;
import java.util.List;

/**
 * Representa un mensaje de simulación que se envía a través del sistema de mensajería.
 * Contiene la información necesaria para iniciar una simulación, incluyendo el ID de la solicitud
 * y los detalles de las entidades involucradas.
 */
public class SimulationMessage implements Serializable {
    private Integer solicitudId;
    private List<String> entityNames;
    private List<Integer> initialQuantities;

    /**
     * Constructor por defecto.
     */
    public SimulationMessage() {}

    /**
     * Constructor con todos los campos.
     *
     * @param solicitudId El identificador único de la solicitud de simulación.
     * @param entityNames Lista de nombres de las entidades a simular.
     * @param initialQuantities Lista de cantidades iniciales correspondientes a cada entidad.
     */
    public SimulationMessage(Integer solicitudId, List<String> entityNames, List<Integer> initialQuantities) {
        this.solicitudId = solicitudId;
        this.entityNames = entityNames;
        this.initialQuantities = initialQuantities;
    }

    /**
     * Obtiene el ID de la solicitud.
     *
     * @return El ID de la solicitud.
     */
    public Integer getSolicitudId() { return solicitudId; }

    /**
     * Establece el ID de la solicitud.
     *
     * @param solicitudId El ID de la solicitud a establecer.
     */
    public void setSolicitudId(Integer solicitudId) { this.solicitudId = solicitudId; }

    /**
     * Obtiene la lista de nombres de las entidades.
     *
     * @return Lista de nombres de entidades.
     */
    public List<String> getEntityNames() { return entityNames; }

    /**
     * Establece la lista de nombres de las entidades.
     *
     * @param entityNames Lista de nombres de entidades a establecer.
     */
    public void setEntityNames(List<String> entityNames) { this.entityNames = entityNames; }

    /**
     * Obtiene la lista de cantidades iniciales.
     *
     * @return Lista de cantidades iniciales.
     */
    public List<Integer> getInitialQuantities() { return initialQuantities; }

    /**
     * Establece la lista de cantidades iniciales.
     *
     * @param initialQuantities Lista de cantidades iniciales a establecer.
     */
    public void setInitialQuantities(List<Integer> initialQuantities) { this.initialQuantities = initialQuantities; }
}
