package org.trabajott1.model;

import java.io.Serializable;
import java.util.List;

public class SimulationMessage implements Serializable {
    private Integer solicitudId;
    private List<String> entityNames;
    private List<Integer> initialQuantities;

    public SimulationMessage() {}

    public SimulationMessage(Integer solicitudId, List<String> entityNames, List<Integer> initialQuantities) {
        this.solicitudId = solicitudId;
        this.entityNames = entityNames;
        this.initialQuantities = initialQuantities;
    }

    public Integer getSolicitudId() { return solicitudId; }
    public void setSolicitudId(Integer solicitudId) { this.solicitudId = solicitudId; }

    public List<String> getEntityNames() { return entityNames; }
    public void setEntityNames(List<String> entityNames) { this.entityNames = entityNames; }

    public List<Integer> getInitialQuantities() { return initialQuantities; }
    public void setInitialQuantities(List<Integer> initialQuantities) { this.initialQuantities = initialQuantities; }
}
