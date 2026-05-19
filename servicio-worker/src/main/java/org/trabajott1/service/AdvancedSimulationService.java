package org.trabajott1.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trabajott1.persistence.entity.EstadisticaPoblacionEntity;
import org.trabajott1.persistence.entity.ResultadoEntity;
import org.trabajott1.persistence.entity.SolicitudEntity;
import org.trabajott1.repository.EstadisticaPoblacionRepository;
import org.trabajott1.repository.SolicitudRepository;

import java.util.*;

/**
 * Implementación avanzada del servicio de simulación con nuevas reglas de cadena alimentaria y compartición de comida.
 *
 * @author Lucas, Ana, Clara, Santiago
 * @version 1.0
 */
@Service
@ConditionalOnProperty(name = "simulation.mode", havingValue = "advanced")
public class AdvancedSimulationService implements ISimulationService {

    private static final Logger log = LoggerFactory.getLogger(AdvancedSimulationService.class);

    /** Tamaño del lado del grid cuadrado donde viven las células. */
    private static final int GRID_SIZE = 8;

    /** Número de pasos de tiempo que dura la simulación. */
    private static final int MAX_TIME = 10;

    /** Colores asignados a las especies. */
    private static final String[] COLORS = {"red", "blue", "green", "yellow"};

    /**
     * Representa una célula en el grid de simulación.
     * Guarda a qué especie pertenece, su color y si ha comido en el paso actual.
     */
    private static class Cell {
        /** Nombre de la especie a la que pertenece la célula. */
        String name;

        /** Color de la célula, que determina su posición en la jerarquía alimentaria. */
        String color;

        /** Indica si la célula ha comido en el paso de tiempo actual, lo que le permite reproducirse. */
        boolean hasEaten;

        /** Indica si la célula tiene comida para compartir con otra de su especie. */
        boolean hasFoodToShare;

        /**
         * Crea una nueva célula con el nombre y color indicados.
         *
         * @param name  nombre de la especie
         * @param color color asignado a la especie
         */
        Cell(String name, String color) {
            this.name = name;
            this.color = color;
            this.hasEaten = false;
            this.hasFoodToShare = false;
        }

        /**
         * Crea una copia independiente de esta célula con el mismo nombre, color y estado de alimentación.
         *
         * @return una nueva instancia {@link Cell} con los mismos valores
         */
        Cell copy() {
            Cell c = new Cell(name, color);
            c.hasEaten = this.hasEaten;
            c.hasFoodToShare = this.hasFoodToShare;
            return c;
        }
    }

    /** Repositorio para buscar y guardar las solicitudes de simulación en la base de datos. */
    private final SolicitudRepository solicitudRepository;
    private final EstadisticaPoblacionRepository estadisticaPoblacionRepository;

    /**
     * Crea el servicio inyectando el repositorio de solicitudes y de estadísticas.
     *
     * @param solicitudRepository el repositorio para acceder a los datos de las solicitudes
     * @param estadisticaPoblacionRepository el repositorio para las estadísticas
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    public AdvancedSimulationService(SolicitudRepository solicitudRepository, EstadisticaPoblacionRepository estadisticaPoblacionRepository) {
        this.solicitudRepository = solicitudRepository;
        this.estadisticaPoblacionRepository = estadisticaPoblacionRepository;
    }

    /**
     * Ejecuta la simulación de vida artificial y guarda los resultados en la base de datos.
     * Si la solicitud no existe, no hace nada. Al terminar, marca la solicitud como "FINALIZADA".
     *
     * @param solicitudId       el ID interno de la solicitud en la base de datos
     * @param entityNames       lista de nombres de las entidades participantes (entre 1 y 4)
     * @param initialQuantities lista de cantidades iniciales de cada entidad, en el mismo orden
     * @throws IllegalArgumentException si el número de entidades no está entre 1 y 4
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Override
    @Transactional
    public void executeSimulation(Integer solicitudId, List<String> entityNames, List<Integer> initialQuantities) {
        if (entityNames.size() < 1 || entityNames.size() > 4) {
            throw new IllegalArgumentException("El número de entidades debe estar entre 1 y 4, se recibieron: " + entityNames.size());
        }

        List<EstadisticaPoblacionEntity> stats = new ArrayList<>();
        String resultadoSimulacion = generateSimulationData(entityNames, initialQuantities, stats);

        Optional<SolicitudEntity> solicitudOpt = solicitudRepository.findById(solicitudId);
        if (solicitudOpt.isPresent()) {
            SolicitudEntity entity = solicitudOpt.get();
            
            ResultadoEntity resultadoEntity = new ResultadoEntity();
            resultadoEntity.setSolicitud(entity);
            resultadoEntity.setDatosResultado(resultadoSimulacion);
            
            entity.setResultado(resultadoEntity);
            entity.setEstado("FINALIZADA");
            
            solicitudRepository.save(entity);

            // Guardar estadísticas
            for (EstadisticaPoblacionEntity stat : stats) {
                stat.setSolicitud(entity);
            }
            estadisticaPoblacionRepository.saveAll(stats);
        }
    }

    /**
     * Nueva jerarquía: Red -> {Blue, Green} -> Yellow
     * 1. Yellow no come (base).
     * 2. Red no es comida (cima).
     * 3. Blue y Green comen Yellow.
     * 4. Red come Blue y Green.
     */
    private boolean canEat(String color1, String color2) {
        if (color1.equals("yellow")) return false; // Las amarillas no comen
        if (color2.equals("red")) return false;    // Las rojas no son comida

        if (color1.equals("red") && color2.equals("blue")) return true;
        if (color1.equals("red") && color2.equals("green")) return true;
        if (color1.equals("green") && color2.equals("yellow")) return true;
        if (color1.equals("blue") && color2.equals("yellow")) return true;

        return false;
    }

    private String generateSimulationData(List<String> entityNames, List<Integer> initialQuantities, List<EstadisticaPoblacionEntity> statsList) {
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
                    // Si la búsqueda aleatoria falla, encuentra el primer lugar disponible
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
        collectStats(grid, 0, statsList);

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
                                // Diferentes especies - Jerarquía de la cadena alimentaria
                                if (canEat(current.color, occupant.color)) {
                                    log.info("Comer: {} ({}) se comió a {} ({}) en [{}, {}]", current.name, current.color, occupant.name, occupant.color, nextY, nextX);
                                    Cell winner = current.copy();
                                    winner.hasEaten = true;
                                    // Lógica Azul: reserva comida al comer
                                    if (current.color.equals("blue")) {
                                        winner.hasFoodToShare = true;
                                        log.info("Azul reservó comida para compartir");
                                    }
                                    nextGrid[nextY][nextX] = winner;
                                } else if (canEat(occupant.color, current.color)) {
                                    log.info("Comer: {} ({}) se comió a {} ({}) en [{}, {}]", occupant.name, occupant.color, current.name, current.color, nextY, nextX);
                                    occupant.hasEaten = true;
                                    // Lógica Azul: reserva comida al comer
                                    if (occupant.color.equals("blue")) {
                                        occupant.hasFoodToShare = true;
                                        log.info("Azul reservó comida para compartir");
                                    }
                                } else {
                                    // Ninguno come, ambos sobreviven
                                    placeInFreeSpot(nextGrid, current, nextX, nextY);
                                }
                            } else {
                                // Misma especie
                                if (current.color.equals("blue")) {
                                    // Lógica Azul: compartir comida
                                    if (current.hasFoodToShare && !occupant.hasEaten) {
                                        occupant.hasEaten = true;
                                        current.hasFoodToShare = false;
                                        log.info("Azul compartió comida con un vecino");
                                    } else if (occupant.hasFoodToShare && !current.hasEaten) {
                                        current.hasEaten = true;
                                        occupant.hasFoodToShare = false;
                                        log.info("Azul recibió comida de un vecino");
                                    }
                                }

                                if (current.hasEaten) {
                                    if (reproducir(nextGrid, current, nextX, nextY)) {
                                        current.hasEaten = false;
                                    }
                                } else if (occupant.hasEaten) {
                                    if (reproducir(nextGrid, occupant, nextX, nextY)) {
                                        occupant.hasEaten = false;
                                    }
                                }
                                placeInFreeSpot(nextGrid, current, nextX, nextY);
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
            collectStats(grid, t, statsList);
        }
        return sb.toString();
    }

    private void collectStats(Cell[][] grid, int t, List<EstadisticaPoblacionEntity> statsList) {
        Map<String, Integer> counts = new HashMap<>();
        for (String color : COLORS) counts.put(color, 0);
        
        for (int y = 0; y < GRID_SIZE; y++) {
            for (int x = 0; x < GRID_SIZE; x++) {
                if (grid[y][x] != null) {
                    String color = grid[y][x].color;
                    counts.put(color, counts.get(color) + 1);
                }
            }
        }
        
        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            statsList.add(new EstadisticaPoblacionEntity(null, t, entry.getKey(), entry.getValue()));
        }
    }

    private void placeInFreeSpot(Cell[][] grid, Cell cell, int x, int y) {
        if (grid[y][x] == null) {
            grid[y][x] = cell.copy();
            return;
        }
        List<int[]> neighbors = new ArrayList<>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue;
                int nx = x + i;
                int ny = y + j;
                if (nx >= 0 && nx < GRID_SIZE && ny >= 0 && ny < GRID_SIZE) {
                    neighbors.add(new int[]{nx, ny});
                }
            }
        }
        Collections.shuffle(neighbors);
        for (int[] pos : neighbors) {
            if (grid[pos[1]][pos[0]] == null) {
                grid[pos[1]][pos[0]] = cell.copy();
                return;
            }
        }
        // Si no hay sitio cerca, buscamos en toda la rejilla como último recurso
        for (int r = 0; r < GRID_SIZE; r++) {
            for (int c = 0; c < GRID_SIZE; c++) {
                if (grid[r][c] == null) {
                    grid[r][c] = cell.copy();
                    return;
                }
            }
        }
    }

    private boolean reproducir(Cell[][] grid, Cell parent, int x, int y) {
        List<int[]> neighbors = new ArrayList<>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue;
                int nx = x + i;
                int ny = y + j;
                if (nx >= 0 && nx < GRID_SIZE && ny >= 0 && ny < GRID_SIZE) {
                    neighbors.add(new int[]{nx, ny});
                }
            }
        }
        Collections.shuffle(neighbors);
        for (int[] pos : neighbors) {
            if (grid[pos[1]][pos[0]] == null) {
                grid[pos[1]][pos[0]] = new Cell(parent.name, parent.color);
                log.info("Reproducción: Célula {} ({}) nació en [{}, {}]", parent.name, parent.color, pos[1], pos[0]);
                return true;
            }
        }
        return false;
    }

    private void appendCell(StringBuilder sb, int t, int y, int x, String color) {
        log.debug("Append: T={}, Pos=[{}, {}], Color={}", t, y, x, color);
        sb.append(t).append(",").append(y).append(",").append(x).append(",").append(color).append("\n");
    }
}
