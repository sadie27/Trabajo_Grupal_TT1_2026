package org.trabajott1.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trabajott1.persistence.entity.ResultadoEntity;
import org.trabajott1.persistence.entity.SolicitudEntity;
import org.trabajott1.repository.SolicitudRepository;

import java.util.*;

/**
 * Servicio que ejecuta la lógica de simulación de entidades en un tablero.
 *
 * Gestiona una cuadrícula de GRID_SIZE x GRID_SIZE (8x8) durante MAX_TIME
 * pasos de tiempo (timesteps). En cada paso, las células se mueven,
 * interactúan entre sí siguiendo una jerarquía alimenticia cíclica,
 * y pueden reproducirse si cumplen las condiciones necesarias.
 *
 * Se limita a un máximo de 4 especies, cada una representada por un color:
 * red, blue, green, yellow.
 */
@Service
public class SimulationService {

    private static final Logger log = LoggerFactory.getLogger(SimulationService.class);
    private static final int GRID_SIZE = 8;
    private static final int MAX_TIME = 10;
    private static final String[] COLORS = {"red", "blue", "green", "yellow"};

    /**
     * Representa una célula individual en el tablero de simulación.
     *
     * Cada célula pertenece a una especie (identificada por nombre y color)
     * y mantiene un estado interno que indica si ha depredado recientemente.
     * Este estado es imprescindible para controlar la reproducción:
     * una célula solo puede reproducirse si ella u otra de su especie
     * ha comido antes.
     */
    private static class Cell {
        /** Nombre de la entidad/especie. Ejemplo: "leon" */
        String name;

        /**
         * Color asignado a la especie (pos. en la jerarquía alimenticia) . Valores: red, blue, green, yellow.
         */
        String color;

        /**
         * Indica si esta célula ha depredado a otra en el timestep actual.
         * Se pone a true al ganar un encuentro de depredación.Se resetea a false tras reproducirse.
         */
        boolean hasEaten;

        /**
         * Crea una nueva célula con el nombre y color dados.
         * El estado hasEaten se inicializa a false.
         *
         * @param name  nombre de la especie
         * @param color color que identifica la especie en la jerarquía
         */
        Cell(String name, String color) {
            this.name = name;
            this.color = color;
            this.hasEaten = false;
        }

        /**
         * Crea una copia independiente de esta célula,
         * preservando nombre, color y estado hasEaten.
         * Se usa al mover células al siguiente timestep
         * para no compartir referencias entre grids.
         *
         * @return nueva instancia Cell con los mismos valores
         */
        Cell copy() {
            Cell c = new Cell(name, color);
            c.hasEaten = this.hasEaten;
            return c;
        }
    }

    private final SolicitudRepository solicitudRepository;


    public SimulationService(SolicitudRepository solicitudRepository) {
        this.solicitudRepository = solicitudRepository;
    }

    @Transactional
    public void executeSimulation(Integer solicitudId, List<String> entityNames, List<Integer> initialQuantities) {
        // Simulamos carga de trabajo
        try { Thread.sleep(5000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        String resultadoSimulacion = generateSimulationData(entityNames, initialQuantities);

        Optional<SolicitudEntity> solicitudOpt = solicitudRepository.findById(solicitudId);
        if (solicitudOpt.isPresent()) {
            SolicitudEntity entity = solicitudOpt.get();

            ResultadoEntity resultadoEntity = new ResultadoEntity();
            resultadoEntity.setSolicitud(entity);
            resultadoEntity.setDatosResultado(resultadoSimulacion);

            entity.setResultado(resultadoEntity);
            entity.setEstado("FINALIZADA");

            solicitudRepository.save(entity);
        }
    }

    /**
     * Determina si una célula de color1 puede comerse a una de color2
     * según la jerarquía alimenticia cíclica.
     *
     * La cadena ciclica es: red → blue → green → yellow → red
     * Es decir, cada color se come al siguiente en el array COLORS.
     *
     * Ejemplos:canEat("red", "blue")   → true  (red está antes que blue)
     *
     * @param color1 color de la célula atacante
     * @param color2 color de la célula objetivo
     * @return true si color1 puede comer a color2, false en caso contrario
     */
    private boolean canEat(String color1, String color2) {
        int i1 = -1, i2 = -1;
        for (int i = 0; i < COLORS.length; i++) {
            if (COLORS[i].equals(color1)) i1 = i;
            if (COLORS[i].equals(color2)) i2 = i;
        }
        return (i1 != -1 && i2 != -1) && (i1 + 1) % COLORS.length == i2;
    }

    private String generateSimulationData(List<String> entityNames, List<Integer> initialQuantities) {
        StringBuilder sb = new StringBuilder();
        sb.append(GRID_SIZE).append("\n");

        Map<String, String> colorMap = new HashMap<>();
        int numSpecies = Math.min(entityNames.size(), COLORS.length);
        for (int i = 0; i < numSpecies; i++) {
            colorMap.put(entityNames.get(i), COLORS[i]);
        }

        Cell[][] grid = new Cell[GRID_SIZE][GRID_SIZE];
        Random rand = new Random();

        // Tiempo 0
        int totalPlaced = 0;
        int maxSpots = GRID_SIZE * GRID_SIZE;
        for (int i = 0; i < numSpecies; i++) {
            String name = entityNames.get(i);
            int count = initialQuantities.get(i);
            String color = colorMap.get(name);
            for (int j = 0; j < count; j++) {
                if (totalPlaced >= maxSpots) break;

                int x, y;
                int attempts = 0;
                do {
                    x = rand.nextInt(GRID_SIZE);
                    y = rand.nextInt(GRID_SIZE);
                    attempts++;
                } while (grid[y][x] != null && attempts < maxSpots);

                if (grid[y][x] == null) {
                    grid[y][x] = new Cell(name, color);
                    appendCell(sb, 0, y, x, color);
                    totalPlaced++;
                } else {
                    // If random search fails, find the first available spot
                    boolean found = false;
                    for (int r = 0; r < GRID_SIZE && !found; r++) {
                        for (int c = 0; c < GRID_SIZE && !found; c++) {
                            if (grid[r][c] == null) {
                                grid[r][c] = new Cell(name, color);
                                appendCell(sb, 0, r, c, color);
                                totalPlaced++;
                                found = true;
                            }
                        }
                    }
                }
            }
        }

        // Tiempos 1..MAX_TIME
        for (int t = 1; t <= MAX_TIME; t++) {
            Cell[][] nextGrid = new Cell[GRID_SIZE][GRID_SIZE];
            for (int y = 0; y < GRID_SIZE; y++) {
                for (int x = 0; x < GRID_SIZE; x++) {
                    if (grid[y][x] != null) {
                        Cell current = grid[y][x];
                        int nextX = Math.max(0, Math.min(GRID_SIZE - 1, x + rand.nextInt(3) - 1));
                        int nextY = Math.max(0, Math.min(GRID_SIZE - 1, y + rand.nextInt(3) - 1));

                        if (nextGrid[nextY][nextX] == null) {
                            nextGrid[nextY][nextX] = current.copy();
                        } else {
                            Cell occupant = nextGrid[nextY][nextX];
                            if (!occupant.name.equals(current.name)) {
                                // Different species - Food chain hierarchy
                                if (canEat(current.color, occupant.color)) {
                                    log.info("Comer: {} ({}) se comió a {} ({}) en [{}, {}]", current.name, current.color, occupant.name, occupant.color, nextY, nextX);
                                    Cell winner = current.copy();
                                    winner.hasEaten = true;
                                    nextGrid[nextY][nextX] = winner;
                                } else if (canEat(occupant.color, current.color)) {
                                    log.info("Comer: {} ({}) se comió a {} ({}) en [{}, {}]", occupant.name, occupant.color, current.name, current.color, nextY, nextX);
                                    occupant.hasEaten = true;
                                } else {
                                    // Neither eats, random tie-breaker
                                    if (rand.nextBoolean()) nextGrid[nextY][nextX] = current.copy();
                                }
                            } else {
                                // Same species - Reproduction only if one has eaten
                                if (current.hasEaten) {
                                    reproducir(nextGrid, current, nextX, nextY);
                                    occupant.hasEaten = false;
                                } else if (occupant.hasEaten) {
                                    reproducir(nextGrid, occupant, nextX, nextY);
                                    occupant.hasEaten = false;
                                }
                            }
                        }
                    }
                }
            }
            grid = nextGrid;
            for (int y = 0; y < GRID_SIZE; y++) {
                for (int x = 0; x < GRID_SIZE; x++) {
                    if (grid[y][x] != null) {
                        appendCell(sb, t, y, x, grid[y][x].color);
                    }
                }
            }
        }
        return sb.toString();
    }

    /**
     * Intenta reproducir una célula colocando una nueva en una
     * celda adyacente libre del tablero.
     *
     * La célula hija hereda el nombre y color del progenitor,
     * y comienza con hasEaten = false (debe cazar por sí misma).
     * Si no hay ninguna celda libre cerca, la reproducción no ocurre.
     *
     * @param grid   el tablero donde se intenta colocar la nueva célula
     * @param parent la célula que se reproduce
     * @param x      columna de referencia para buscar posición adyacente
     * @param y      fila de referencia para buscar posición adyacente
     */
    private void reproducir(Cell[][] grid, Cell parent, int x, int y) {
        Random rand = new Random();
        int rx = Math.max(0, Math.min(GRID_SIZE - 1, x + rand.nextInt(3) - 1));
        int ry = Math.max(0, Math.min(GRID_SIZE - 1, y + rand.nextInt(3) - 1));
        if (grid[ry][rx] == null) {
            grid[ry][rx] = new Cell(parent.name, parent.color);
            log.info("Reproducción: Célula {} ({}) nació en [{}, {}]", parent.name, parent.color, ry, rx);
        }
    }

    private void appendCell(StringBuilder sb, int t, int y, int x, String color) {
        log.info("Append: T={}, Pos=[{}, {}], Color={}", t, y, x, color);
        sb.append(t).append(",").append(y).append(",").append(x).append(",").append(color).append("\n");
    }
}
