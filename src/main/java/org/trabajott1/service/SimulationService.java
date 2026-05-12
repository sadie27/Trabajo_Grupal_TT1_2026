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
 * Servicio encargado de ejecutar la simulación de vida artificial.
 */
@Service
public class SimulationService {

    private static final Logger log = LoggerFactory.getLogger(SimulationService.class);
    private static final int GRID_SIZE = 8;
    private static final int MAX_TIME = 10;
    private static final String[] COLORS = {"red", "blue", "green", "yellow"};

    private static class Cell {
        String name;
        String color;
        boolean hasEaten;

        Cell(String name, String color) {
            this.name = name;
            this.color = color;
            this.hasEaten = false;
        }

        Cell copy() {
            Cell c = new Cell(name, color);
            c.hasEaten = this.hasEaten;
            return c;
        }
    }

    private final SolicitudRepository solicitudRepository;

    /**
     * Constructor de SimulationService.
     *
     * @param solicitudRepository El repositorio para acceder a los datos de las solicitudes.
     */
    public SimulationService(SolicitudRepository solicitudRepository) {
        this.solicitudRepository = solicitudRepository;
    }

    /**
     * Ejecuta el proceso de simulación y guarda los resultados en la base de datos.
     *
     * @param solicitudId       El ID de la solicitud.
     * @param entityNames      Lista de nombres de las entidades participantes.
     * @param initialQuantities Lista de cantidades iniciales de cada entidad.
     */
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
                                    nextGrid[nextY][nextX] = winner;
                                } else if (canEat(occupant.color, current.color)) {
                                    log.info("Comer: {} ({}) se comió a {} ({}) en [{}, {}]", occupant.name, occupant.color, current.name, current.color, nextY, nextX);
                                    occupant.hasEaten = true;
                                } else {
                                    // Ninguno come, desempate aleatorio
                                    if (rand.nextBoolean()) nextGrid[nextY][nextX] = current.copy();
                                }
                            } else {
                                // Misma especie - Reproducción solo si uno ha comido
                                if (current.hasEaten) {
                                    if (reproducir(nextGrid, current, nextX, nextY)) {
                                        current.hasEaten = false;
                                    }
                                } else if (occupant.hasEaten) {
                                    if (reproducir(nextGrid, occupant, nextX, nextY)) {
                                        occupant.hasEaten = false;
                                    }
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
        log.info("Append: T={}, Pos=[{}, {}], Color={}", t, y, x, color);
        sb.append(t).append(",").append(y).append(",").append(x).append(",").append(color).append("\n");
    }
}
