package org.trabajott1.api.impl;

import org.trabajott1.service.EstadisticasService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para el endpoint {@code GET /Estadisticas}.
 * Delega en {@link EstadisticasService} para calcular y devolver las estadísticas
 * agregadas de una simulación identificada por su token.
 *
 * @author Lucas, Ana, Clara, Santiago
 */
@RestController
public class EstadisticasController {

    private final EstadisticasService estadisticasService;

    /**
     * Crea el controlador inyectando el servicio de estadísticas.
     *
     * @param estadisticasService servicio que calcula las estadísticas de simulación
     */
    public EstadisticasController(EstadisticasService estadisticasService) {
        this.estadisticasService = estadisticasService;
    }

    /**
     * Devuelve las estadísticas agregadas de la simulación identificada por {@code tok}.
     *
     * @param tok token entero de la solicitud cuyas estadísticas se quieren consultar
     * @return {@code 200 OK} con el cuerpo {@link EstadisticasService.EstadisticasResponse} si la
     *         simulación tiene datos; {@code 404 Not Found} con el mensaje de error en caso contrario
     */
    @GetMapping("/Estadisticas")
    public ResponseEntity<EstadisticasService.EstadisticasResponse> getEstadisticas(
            @RequestParam Integer tok) {

        var response = estadisticasService.obtenerEstadisticas(tok);
        return response.done()
            ? ResponseEntity.ok(response)
            : ResponseEntity.status(404).body(response);
    }
}
