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
 * Servicio encargado de ejecutar la simulación de vida artificial basada en autómatas celulares.
 * Las entidades (especies) se colocan en un grid de 8x8, se mueven aleatoriamente en cada paso de tiempo,
 * se comen entre ellas según una jerarquía por colores (cada color come al siguiente en la lista)
 * y se reproducen cuando una célula ha comido y colisiona con otra de su misma especie.
 * El resultado se serializa como texto y se guarda en la base de datos asociado a la solicitud.
 *
 * @author Lucas, Ana, Clara, Santiago
 * @version 1.0
 */
@Service
public class SimulationService {

    private static final Logger log = LoggerFactory.getLogger(SimulationService.class);

    /** Tamaño del lado del grid cuadrado donde viven las células. */
    private static final int GRID_SIZE = 8;

    /** Número de pasos de tiempo que dura la simulación. */
    private static final int MAX_TIME = 10;

    /** Colores asignados a las especies en orden: cada color puede comerse al siguiente. */
    private static final String[] COLORS = {"red", "blue", "green", "yellow", "orange", "purple"};

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
        }

        /**
         * Crea una copia independiente de esta célula con el mismo nombre, color y estado de alimentación.
         *
         * @return una nueva instancia {@link Cell} con los mismos valores
         */
        Cell copy() {
            Cell c = new Cell(name, color);
            c.hasEaten = this.hasEaten;
            return c;
        }
    }

    /** Repositorio para buscar y guardar las solicitudes de simulación en la base de datos. */
    private final SolicitudRepository solicitudRepository;

    /**
     * Crea el servicio inyectando el repositorio de solicitudes.
     *
     * @param solicitudRepository el repositorio para acceder a los datos de las solicitudes
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    public SimulationService(SolicitudRepository solicitudRepository) {
        this.solicitudRepository = solicitudRepository;
    }

    /**
     * Ejecuta la simulación de vida artificial y guarda los resultados en la base de datos.
     * Si la solicitud no existe, no hace nada. Al terminar, marca la solicitud como "FINALIZADA".
     *
     * @param solicitudId       el ID interno de la solicitud en la base de datos
     * @param entityNames       lista de nombres de las entidades participantes (entre 1 y 6)
     * @param initialQuantities lista de cantidades iniciales de cada entidad, en el mismo orden
     * @throws IllegalArgumentException si el número de entidades no está entre 1 y 6
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Transactional
    public void executeSimulation(Integer solicitudId, List<String> entityNames, List<Integer> initialQuantities) {
        if (entityNames.size() < 1 || entityNames.size() > 6) {
            throw new IllegalArgumentException("El número de entidades debe estar entre 1 y 6, se recibieron: " + entityNames.size());
        }

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
        if (entityNames.size() < 1 || entityNames.size() > 6) {
            throw new IllegalArgumentException("El número de entidades debe estar entre 1 y 6, se recibieron: " + entityNames.size());
        }
        int numSpecies = Math.min(entityNames.size(), 6);
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
                                    // Ninguno come, ambos sobreviven
                                    placeInFreeSpot(nextGrid, current, nextX, nextY);
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
        }
        return sb.toString();
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
        log.info("Append: T={}, Pos=[{}, {}], Color={}", t, y, x, color);
        sb.append(t).append(",").append(y).append(",").append(x).append(",").append(color).append("\n");
    }
}
