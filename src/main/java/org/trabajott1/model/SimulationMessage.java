package org.trabajott1.model;

import java.io.Serializable;
import java.util.List;
/**
 * Mensaje serializable que representa una tarea de simulación
 * pendiente de ejecutar.
 *
 * Este objeto se envía a la cola RabbitMQ cuando se crea una solicitud.
 * Contiene toda la información necesaria para que SimulationListener
 * pueda ejecutar la simulación de forma independiente y asíncrona,
 * sin necesidad de consultar la base de datos para obtener los parámetros.
 *
 * Implementa Serializable para poder ser transmitido por la cola de mensajes.
 */

public class SimulationMessage implements Serializable {

    /**
     * Identificador único de la solicitud en base de datos.
     * Se usa para recuperar y actualizar la entidad
     * una vez finalizada la simulación.
     */
    private Integer solicitudId;

    /**
     * Lista con los nombres de las especies/entidades a simular.
     * El orden de esta lista se corresponde con el de initialQuantities.
     * Ejemplo: ["leon", "cebra", "jirafa"]
     */
    private List<String> entityNames;

    /**
     * Lista con las cantidades iniciales de cada especie.
     * El índice i corresponde a la cantidad inicial de entityNames.get(i).
     * Ejemplo: [5, 10, 3] → 5 leones, 10 cebras, 3 jirafas
     */
    private List<Integer> initialQuantities;

    /**
     * Constructor vacío requerido por el deserializador Jackson
     * para reconstruir el objeto al recibirlo de la cola.
     */
    public SimulationMessage() {}

    /**
     * Constructor principal para crear un mensaje de simulación.
     *
     * @param solicitudId       ID de la solicitud que origina la simulación
     * @param entityNames       nombres de las especies a simular
     * @param initialQuantities cantidades iniciales de cada especie
     */
    public SimulationMessage(Integer solicitudId,
                             List<String> entityNames,
                             List<Integer> initialQuantities) {
        this.solicitudId = solicitudId;
        this.entityNames = entityNames;
        this.initialQuantities = initialQuantities;
    }

    /**
     * @return el ID de la solicitud asociada a esta tarea
     */
    public Integer getSolicitudId() { return solicitudId; }

    /**
     * @param solicitudId el ID de la solicitud a establecer
     */
    public void setSolicitudId(Integer solicitudId) { this.solicitudId = solicitudId; }

    /**
     * @return la lista de nombres de entidades a simular
     */
    public List<String> getEntityNames() { return entityNames; }

    /**
     * @param entityNames la lista de nombres de entidades a establecer
     */
    public void setEntityNames(List<String> entityNames) { this.entityNames = entityNames; }

    /**
     * @return la lista de cantidades iniciales de cada especie
     */
    public List<Integer> getInitialQuantities() { return initialQuantities; }

    /**
     * @param initialQuantities las cantidades iniciales a establecer
     */
    public void setInitialQuantities(List<Integer> initialQuantities) {
        this.initialQuantities = initialQuantities;
    }
}