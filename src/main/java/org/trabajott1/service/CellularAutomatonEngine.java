package org.trabajott1.service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Motor de autómata celular para simular especies en competencia.
 * <p>
 * Implementa reglas de "Juego de la Vida" adaptadas:
 * - Células nacen con 3 vecinos exactos
 * - Células mueren si tienen menos de 2 o más de 4 vecinos
 * - Competencia: una especie puede "comer" a otra si la rodea
 * </p>
 *
 * @author ana
 * @version 1.0
 */
public class CellularAutomatonEngine {

    private static final int GRID_SIZE = 8;
    private static final int SURVIVAL_MIN = 2;
    private static final int SURVIVAL_MAX = 3;
    private static final int BIRTH_NEIGHBORS = 3;

    /**
     * Representa una célula en el grid.
     */
    public static class Cell {
        public int x;
        public int y;
        public String species; // "Servidores", "Clientes", etc.
        public String color;

        public Cell(int x, int y, String species, String color) {
            this.x = x;
            this.y = y;
            this.species = species;
            this.color = color;
        }

        @Override
        public String toString() {
            return String.format("Cell(%d,%d,%s,%s)", x, y, species, color);
        }
    }

    private List<Cell> currentGeneration;
    private Map<String, String> speciesColors;

    public CellularAutomatonEngine(List<Cell> initialCells, Map<String, String> speciesColors) {
        this.currentGeneration = new ArrayList<>(initialCells);
        this.speciesColors = speciesColors;
    }

    /**
     * Ejecuta un paso de la simulación.
     * Aplica reglas de nacimiento, supervivencia y competencia.
     */
    public void step() {
        Map<String, Integer> cellCounts = new HashMap<>();
        cellCounts.put("empty", GRID_SIZE * GRID_SIZE);

        // Contar células actuales
        for (Cell cell : currentGeneration) {
            cellCounts.put(cell.species, cellCounts.getOrDefault(cell.species, 0) + 1);
            cellCounts.put("empty", cellCounts.get("empty") - 1);
        }

        List<Cell> nextGeneration = new ArrayList<>();

        // 1. Aplicar reglas a células existentes
        for (Cell cell : currentGeneration) {
            int neighbors = countNeighbors(cell.x, cell.y, cell.species);
            int competitorNeighbors = countCompetitorNeighbors(cell.x, cell.y, cell.species);

            // ¿Sobrevive?
            if (neighbors >= SURVIVAL_MIN && neighbors <= SURVIVAL_MAX) {
                // Sobrevive si no hay demasiados competidores
                if (competitorNeighbors < 4) {
                    nextGeneration.add(cell);
                }
            }
        }

        // 2. Crear nuevas células donde hay espacio
        for (int x = 0; x < GRID_SIZE; x++) {
            for (int y = 0; y < GRID_SIZE; y++) {
                if (!isCellOccupied(x, y, nextGeneration)) {
                    // Hay espacio. ¿Nace algo?
                    String newSpecies = checkBirth(x, y);
                    if (newSpecies != null) {
                        nextGeneration.add(new Cell(x, y, newSpecies, speciesColors.get(newSpecies)));
                    }
                }
            }
        }

        // 3. Aplicar movimiento aleatorio (opcional, comentar si no)
        // moveRandomly(nextGeneration);

        this.currentGeneration = nextGeneration;
    }

    /**
     * Cuenta vecinos de la misma especie alrededor de una célula.
     */
    private int countNeighbors(int x, int y, String species) {
        int count = 0;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                int nx = x + dx;
                int ny = y + dy;
                if (isInBounds(nx, ny)) {
                    for (Cell cell : currentGeneration) {
                        if (cell.x == nx && cell.y == ny && cell.species.equals(species)) {
                            count++;
                            break;
                        }
                    }
                }
            }
        }
        return count;
    }

    /**
     * Cuenta vecinos de especies DIFERENTES (competidores).
     */
    private int countCompetitorNeighbors(int x, int y, String species) {
        int count = 0;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                int nx = x + dx;
                int ny = y + dy;
                if (isInBounds(nx, ny)) {
                    for (Cell cell : currentGeneration) {
                        if (cell.x == nx && cell.y == ny && !cell.species.equals(species)) {
                            count++;
                            break;
                        }
                    }
                }
            }
        }
        return count;
    }

    /**
     * Determina si nace una célula en una posición vacía.
     * Nace si hay exactamente 3 vecinos de la misma especie.
     * Si hay múltiples especies, gana la más representada.
     */
    private String checkBirth(int x, int y) {
        Map<String, Integer> speciesCount = new HashMap<>();

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                int nx = x + dx;
                int ny = y + dy;
                if (isInBounds(nx, ny)) {
                    for (Cell cell : currentGeneration) {
                        if (cell.x == nx && cell.y == ny) {
                            speciesCount.put(cell.species,
                                    speciesCount.getOrDefault(cell.species, 0) + 1);
                            break;
                        }
                    }
                }
            }
        }

        // Nace si hay especie con exactamente 3 vecinos
        for (Map.Entry<String, Integer> entry : speciesCount.entrySet()) {
            if (entry.getValue() == BIRTH_NEIGHBORS) {
                return entry.getKey();
            }
        }
        return null;
    }

    private boolean isCellOccupied(int x, int y, List<Cell> cells) {
        return cells.stream().anyMatch(c -> c.x == x && c.y == y);
    }

    private boolean isInBounds(int x, int y) {
        return x >= 0 && x < GRID_SIZE && y >= 0 && y < GRID_SIZE;
    }

    public List<Cell> getCurrentGeneration() {
        return new ArrayList<>(currentGeneration);
    }

    /**
     * Exporta estado actual como CSV.
     * @param timeStep Número del paso de tiempo
     * @return String con formato: time,y,x,color
     */
    public String exportToCSV(int timeStep) {
        return currentGeneration.stream()
                .map(cell -> String.format("%d,%d,%d,%s", timeStep, cell.y, cell.x, cell.color))
                .collect(Collectors.joining("\n"));
    }
}
