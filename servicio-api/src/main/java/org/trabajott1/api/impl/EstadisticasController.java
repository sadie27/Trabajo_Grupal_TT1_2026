package org.trabajott1.api.impl;

import org.trabajott1.service.EstadisticasService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para el endpoint GET /Estadisticas.
 * Devuelve estadísticas agregadas de una simulación identificada por su token.
 */
@RestController
public class EstadisticasController {

    private final EstadisticasService estadisticasService;

    public EstadisticasController(EstadisticasService estadisticasService) {
        this.estadisticasService = estadisticasService;
    }

    @GetMapping("/Estadisticas")
    public ResponseEntity<EstadisticasService.EstadisticasResponse> getEstadisticas(
            @RequestParam Integer tok) {

        var response = estadisticasService.obtenerEstadisticas(tok);
        return response.done()
            ? ResponseEntity.ok(response)
            : ResponseEntity.status(404).body(response);
    }
}
