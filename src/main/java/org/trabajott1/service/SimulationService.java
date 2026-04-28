package org.trabajott1.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trabajott1.persistence.entity.ResultadoEntity;
import org.trabajott1.persistence.entity.SolicitudEntity;
import org.trabajott1.repository.SolicitudRepository;

import java.util.*;

@Service
public class SimulationService {

    private static final int GRID_SIZE = 8;
    private static final int MAX_TIME = 10;
    private static final String[] COLORS = {"red", "yellow", "blue", "green", "purple", "orange", "pink"};

    private final SolicitudRepository solicitudRepository;

    public SimulationService(SolicitudRepository solicitudRepository) {
        this.solicitudRepository = solicitudRepository;
    }

    @Async
    @Transactional
    public void runSimulationAsync(Integer solicitudId, List<String> entityNames, List<Integer> initialQuantities) {
        // Simulamos un pequeño retraso para que se note la asincronía
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

    private String generateSimulationData(List<String> entityNames, List<Integer> initialQuantities) {
        StringBuilder sb = new StringBuilder();
        sb.append(GRID_SIZE).append("\n");

        Map<String, String> colorMap = new HashMap<>();
        for (int i = 0; i < entityNames.size(); i++) {
            colorMap.put(entityNames.get(i), COLORS[i % COLORS.length]);
        }

        String[][] grid = new String[GRID_SIZE][GRID_SIZE];
        Random rand = new Random();

        // Tiempo 0
        for (int i = 0; i < entityNames.size(); i++) {
            String name = entityNames.get(i);
            int count = initialQuantities.get(i);
            for (int j = 0; j < count; j++) {
                int x = rand.nextInt(GRID_SIZE);
                int y = rand.nextInt(GRID_SIZE);
                if (grid[y][x] == null) {
                    grid[y][x] = name;
                    appendCell(sb, 0, y, x, colorMap.get(name));
                }
            }
        }

        // Tiempos 1..MAX_TIME
        for (int t = 1; t <= MAX_TIME; t++) {
            String[][] nextGrid = new String[GRID_SIZE][GRID_SIZE];
            for (int y = 0; y < GRID_SIZE; y++) {
                for (int x = 0; x < GRID_SIZE; x++) {
                    if (grid[y][x] != null) {
                        int nextX = Math.max(0, Math.min(GRID_SIZE - 1, x + rand.nextInt(3) - 1));
                        int nextY = Math.max(0, Math.min(GRID_SIZE - 1, y + rand.nextInt(3) - 1));
                        if (nextGrid[nextY][nextX] == null) {
                            nextGrid[nextY][nextX] = grid[y][x];
                        } else if (!nextGrid[nextY][nextX].equals(grid[y][x])) {
                            if (rand.nextBoolean()) nextGrid[nextY][nextX] = grid[y][x];
                        } else {
                            reproducir(nextGrid, grid[y][x], nextX, nextY);
                        }
                    }
                }
            }
            grid = nextGrid;
            for (int y = 0; y < GRID_SIZE; y++) {
                for (int x = 0; x < GRID_SIZE; x++) {
                    if (grid[y][x] != null) {
                        appendCell(sb, t, y, x, colorMap.get(grid[y][x]));
                    }
                }
            }
        }
        return sb.toString();
    }

    private void reproducir(String[][] grid, String name, int x, int y) {
        Random rand = new Random();
        int rx = Math.max(0, Math.min(GRID_SIZE - 1, x + rand.nextInt(3) - 1));
        int ry = Math.max(0, Math.min(GRID_SIZE - 1, y + rand.nextInt(3) - 1));
        if (grid[ry][rx] == null) grid[ry][rx] = name;
    }

    private void appendCell(StringBuilder sb, int t, int y, int x, String color) {
        sb.append(t).append(",").append(y).append(",").append(x).append(",").append(color).append("\n");
    }
}
