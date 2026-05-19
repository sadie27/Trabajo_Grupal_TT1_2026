package org.trabajott1.api.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.net.URI;
import java.util.Base64;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST que expone estadísticas de RabbitMQ en tiempo real a través del endpoint
 * {@code GET /Dashboard/Stats}. Los datos se obtienen llamando a la API de gestión de RabbitMQ
 * y se devuelven como un {@link DashboardStats} consumido por {@code dashboard.html}.
 *
 * @author Lucas, Ana, Clara, Santiago
 */
@RestController
public class DashboardController {

    private static final Logger log = LoggerFactory.getLogger(DashboardController.class);

    @Value("${rabbitmq.management.url:http://rabbitmq:15672}")
    private String rabbitManagementUrl;

    private static final String CREDENTIALS = "guest:guest";

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Devuelve las métricas actuales de la cola {@code simulation.queue} y de los canales activos.
     * Si la API de gestión de RabbitMQ no es accesible, los campos numéricos se devuelven a cero
     * en lugar de propagar el error.
     *
     * @return {@code 200 OK} con un {@link DashboardStats} que contiene profundidad de la cola,
     *         tasas de publicación/entrega, número de consumidores y lista de canales activos
     */
    @GetMapping("/Dashboard/Stats")
    public ResponseEntity<DashboardStats> getStats() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString(CREDENTIALS.getBytes()));
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String rabbitBase = rabbitManagementUrl + "/api";

        Map<?, ?> queue = null;
        URI queueUri = URI.create(rabbitBase + "/queues/%2F/simulation.queue");
        try {
            log.info("Fetching queue stats: {}", queueUri);
            queue = restTemplate.exchange(queueUri, HttpMethod.GET, entity, Map.class).getBody();
        } catch (Exception e) {
            log.error("Failed to fetch queue stats from {}: {}", queueUri, e.getMessage());
        }

        List<?> channels = null;
        String channelsUrl = rabbitBase + "/channels/";
        try {
            log.info("Fetching channels: {}", channelsUrl);
            channels = restTemplate.exchange(channelsUrl, HttpMethod.GET, entity, List.class).getBody();
        } catch (Exception e) {
            log.error("Failed to fetch channels from {}: {}", channelsUrl, e.getMessage());
        }

        long queueDepth = toLong(queue, "messages");
        double deliverRate = toRate(queue, "deliver_get_details");
        double publishRate = toRate(queue, "publish_details");
        long consumerCount = toLong(queue, "consumers");

        List<ChannelInfo> channelInfos = channels == null ? List.of() : channels.stream()
                .map(c -> (Map<?, ?>) c)
                .map(c -> new ChannelInfo(
                        connectionName(c),
                        toLong(c, "consumer_count"),
                        toLong(c, "messages_unacknowledged")))
                .collect(java.util.stream.Collectors.groupingBy(ChannelInfo::connectionName))
                .entrySet().stream()
                .filter(e -> e.getValue().stream().anyMatch(ch -> ch.consumerCount() >= 1))
                .flatMap(e -> e.getValue().stream())
                .toList();

        return ResponseEntity.ok(new DashboardStats(
                queueDepth, deliverRate, publishRate, consumerCount, channelInfos));
    }

    /**
     * Extrae un valor {@code long} de un mapa de respuesta JSON de RabbitMQ.
     *
     * @param map mapa que puede ser {@code null}
     * @param key clave a buscar
     * @return valor numérico como {@code long}, o {@code 0} si el mapa es nulo o la clave no existe
     */
    private long toLong(Map<?, ?> map, String key) {
        if (map == null) return 0L;
        Object v = map.get(key);
        if (v instanceof Number n) return n.longValue();
        return 0L;
    }

    /**
     * Extrae la tasa ({@code rate}) de un campo de detalles de RabbitMQ del tipo {@code *_details}.
     *
     * @param map mapa que puede ser {@code null}
     * @param key clave del campo {@code *_details}
     * @return valor de la tasa como {@code double}, o {@code 0.0} si no está disponible
     */
    private double toRate(Map<?, ?> map, String key) {
        if (map == null) return 0.0;
        Object detail = map.get(key);
        if (detail instanceof Map<?, ?> d) {
            Object rate = d.get("rate");
            if (rate instanceof Number n) return n.doubleValue();
        }
        return 0.0;
    }

    /**
     * Extrae el nombre de la conexión del mapa de un canal de RabbitMQ.
     *
     * @param channel mapa del canal tal como lo devuelve {@code /api/channels/}
     * @return nombre de la conexión, o {@code "unknown"} si no está presente
     */
    private String connectionName(Map<?, ?> channel) {
        Object conn = channel.get("connection_details");
        if (conn instanceof Map<?, ?> d) {
            Object name = d.get("name");
            if (name != null) return name.toString();
        }
        return "unknown";
    }

    /**
     * Información de un canal RabbitMQ activo.
     *
     * @param connectionName     nombre de la conexión TCP a la que pertenece el canal
     * @param consumerCount      número de consumidores activos en el canal
     * @param unackedCount       número de mensajes entregados pero aún sin confirmar
     */
    public record ChannelInfo(String connectionName, long consumerCount, long unackedCount) {}

    /**
     * Métricas de RabbitMQ devueltas por {@code GET /Dashboard/Stats}.
     *
     * @param queueDepth     número de mensajes pendientes en {@code simulation.queue}
     * @param deliverRate    tasa de entrega de mensajes (mensajes/s)
     * @param publishRate    tasa de publicación de mensajes (mensajes/s)
     * @param consumerCount  número de consumidores conectados a la cola
     * @param channels       lista de canales activos que tienen al menos un consumidor
     */
    public record DashboardStats(
            long queueDepth,
            double deliverRate,
            double publishRate,
            long consumerCount,
            List<ChannelInfo> channels) {}
}
