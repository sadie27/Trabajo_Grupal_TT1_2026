package org.trabajott1.service;

import java.util.List;

/**
 * Interfaz para los servicios de simulación.
 */
public interface ISimulationService {
    /**
     * Ejecuta el proceso de simulación y guarda los resultados en la base de datos.
     *
     * @param solicitudId       El ID de la solicitud.
     * @param entityNames      Lista de nombres de las entidades participantes.
     * @param initialQuantities Lista de cantidades iniciales de cada entidad.
     */
    void executeSimulation(Integer solicitudId, List<String> entityNames, List<Integer> initialQuantities);
}
