package org.trabajott1.service;

import org.trabajott1.persistence.entity.EntidadSolicitudEntity;
import org.trabajott1.persistence.entity.EstadisticaPoblacionEntity;
import org.trabajott1.repository.EntidadSolicitudRepository;
import org.trabajott1.repository.EstadisticaPoblacionRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Servicio para obtener estadísticas agregadas de una simulación a partir de los datos
 * almacenados en estadisticas_poblacion.
 *
 * @author Lucas, Ana, Clara, Santiago
 * @version 1.0
 */
@Service
public class EstadisticasService {

    /** Colores asignados a las entidades en orden de aparición. */
    private static final String[] COLORS = {"red", "blue", "green", "yellow", "orange", "purple"};

    /** Cadena alimentaria estándar: mapea cada color a su depredador directo. */
    private static final Map<String, String> DEPREDADOR_DE = Map.of(
        "blue",   "red",
        "green",  "blue",
        "yellow", "green",
        "red",    "yellow"
    );

    /** Repositorio para acceder a los registros de estadísticas de población. */
    private final EstadisticaPoblacionRepository estadisticaRepo;

    /** Repositorio para acceder a las entidades participantes de una solicitud. */
    private final EntidadSolicitudRepository entidadRepo;

    /**
     * Crea una instancia del servicio con los repositorios necesarios.
     *
     * @param estadisticaRepo repositorio de estadísticas de población
     * @param entidadRepo     repositorio de entidades de solicitud
     */
    public EstadisticasService(EstadisticaPoblacionRepository estadisticaRepo,
                               EntidadSolicitudRepository entidadRepo) {
        this.estadisticaRepo = estadisticaRepo;
        this.entidadRepo = entidadRepo;
    }

    /**
     * Calcula y devuelve las estadísticas agregadas de una simulación identificada por su token.
     *
     * @param tok token entero de la solicitud cuyas estadísticas se quieren obtener
     * @return {@link EstadisticasResponse} con los datos de población, el color más reproducido,
     *         el que más comió y el dominante al final; o un objeto de error si el token es nulo
     *         o no existe información asociada
     */
    public EstadisticasResponse obtenerEstadisticas(Integer tok) {
        if (tok == null) {
            return EstadisticasResponse.error(null, "Token requerido");
        }

        List<EstadisticaPoblacionEntity> stats =
            estadisticaRepo.findBySolicitud_TokenSolicitud(tok);

        if (stats.isEmpty()) {
            return EstadisticasResponse.error(tok, "No se encontraron estadísticas para el token " + tok);
        }

        List<EntidadSolicitudEntity> entidadesEntities =
            entidadRepo.findBySolicitud_TokenSolicitudOrderByIdAsc(tok);
        List<EntidadDto> entidades = new ArrayList<>();
        for (int i = 0; i < entidadesEntities.size(); i++) {
            String color = i < COLORS.length ? COLORS[i] : "unknown";
            entidades.add(new EntidadDto(entidadesEntities.get(i).getNombreEntidad(), color));
        }

        List<PoblacionPasoDto> poblacion = stats.stream()
            .map(e -> new PoblacionPasoDto(e.getPasoTiempo(), e.getColor(), e.getCantidad()))
            .sorted(Comparator.comparingInt(PoblacionPasoDto::pasoTiempo)
                              .thenComparing(PoblacionPasoDto::color))
            .toList();

        Map<String, List<EstadisticaPoblacionEntity>> porColor = stats.stream()
            .collect(Collectors.groupingBy(EstadisticaPoblacionEntity::getColor));

        // Color que más se reprodujo: mayor suma de incrementos positivos entre pasos
        String masReproducido = porColor.entrySet().stream()
            .max(Comparator.comparingInt(e -> totalIncrementos(e.getValue())))
            .map(Map.Entry::getKey).orElse(null);

        // Color que más comió: depredador del color que más disminuyó
        String masDisminuido = porColor.entrySet().stream()
            .max(Comparator.comparingInt(e -> totalDecrementos(e.getValue())))
            .map(Map.Entry::getKey).orElse(null);
        String masComio = masDisminuido != null
            ? DEPREDADOR_DE.getOrDefault(masDisminuido, "desconocido") : null;

        // Color dominante al final: mayor cantidad en el último paso
        int ultimoPaso = stats.stream()
            .mapToInt(EstadisticaPoblacionEntity::getPasoTiempo).max().orElse(0);
        String masDominante = stats.stream()
            .filter(e -> e.getPasoTiempo() == ultimoPaso)
            .max(Comparator.comparingInt(EstadisticaPoblacionEntity::getCantidad))
            .map(EstadisticaPoblacionEntity::getColor).orElse(null);

        return new EstadisticasResponse(true, null, tok, entidades, poblacion,
                                        masReproducido, masComio, masDominante);
    }

    /**
     * Suma todos los incrementos positivos entre pasos consecutivos para una lista de registros
     * de población ordenada cronológicamente.
     *
     * @param lista registros de estadística de población de un único color
     * @return suma de los deltas positivos entre pasos consecutivos
     */
    private int totalIncrementos(List<EstadisticaPoblacionEntity> lista) {
        var sorted = lista.stream()
            .sorted(Comparator.comparingInt(EstadisticaPoblacionEntity::getPasoTiempo)).toList();
        int total = 0;
        for (int i = 1; i < sorted.size(); i++) {
            int delta = sorted.get(i).getCantidad() - sorted.get(i - 1).getCantidad();
            if (delta > 0) total += delta;
        }
        return total;
    }

    /**
     * Suma todos los decrementos positivos entre pasos consecutivos para una lista de registros
     * de población ordenada cronológicamente.
     *
     * @param lista registros de estadística de población de un único color
     * @return suma de los deltas negativos (expresados como valor positivo) entre pasos consecutivos
     */
    private int totalDecrementos(List<EstadisticaPoblacionEntity> lista) {
        var sorted = lista.stream()
            .sorted(Comparator.comparingInt(EstadisticaPoblacionEntity::getPasoTiempo)).toList();
        int total = 0;
        for (int i = 1; i < sorted.size(); i++) {
            int delta = sorted.get(i - 1).getCantidad() - sorted.get(i).getCantidad();
            if (delta > 0) total += delta;
        }
        return total;
    }

    // --- DTOs internos (records Java 21) ---

    /**
     * DTO que representa una entidad participante en la simulación junto con el color que le fue
     * asignado.
     *
     * @param nombre nombre de la entidad
     * @param color  color asignado a la entidad en la simulación
     */
    public record EntidadDto(String nombre, String color) {}

    /**
     * DTO que representa la cantidad de individuos de un color en un paso de tiempo concreto.
     *
     * @param pasoTiempo número de paso de tiempo
     * @param color      color de la especie
     * @param cantidad   cantidad de individuos en ese paso
     */
    public record PoblacionPasoDto(Integer pasoTiempo, String color, Integer cantidad) {}

    /**
     * DTO de respuesta con todas las estadísticas calculadas para una simulación.
     *
     * @param done                indica si la operación fue exitosa
     * @param errorMessage        mensaje de error en caso de fallo; {@code null} si fue exitoso
     * @param tokenSolicitud      token identificador de la solicitud
     * @param entidades           lista de entidades participantes con sus colores asignados
     * @param poblacionPorPaso    evolución de la población de cada color paso a paso
     * @param colorMasReproducido color que acumuló más incrementos de población durante la simulación
     * @param colorMasComio       color depredador del que más disminuyó su presa
     * @param colorMasDominante   color con mayor cantidad de individuos en el último paso
     */
    public record EstadisticasResponse(
        boolean done,
        String errorMessage,
        Integer tokenSolicitud,
        List<EntidadDto> entidades,
        List<PoblacionPasoDto> poblacionPorPaso,
        String colorMasReproducido,
        String colorMasComio,
        String colorMasDominante
    ) {
        /**
         * Crea una respuesta de error con los campos de estadísticas en {@code null}.
         *
         * @param tok token de la solicitud afectada, puede ser {@code null}
         * @param msg mensaje descriptivo del error
         * @return instancia de {@link EstadisticasResponse} representando el error
         */
        static EstadisticasResponse error(Integer tok, String msg) {
            return new EstadisticasResponse(false, msg, tok, null, null, null, null, null);
        }
    }
}
