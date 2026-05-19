package org.trabajott1.api.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests de capa web para {@link DashboardController}.
 * Dado que {@link RestTemplate} se instancia como campo privado (no inyectado por Spring),
 * se sustituye vía {@link ReflectionTestUtils} después de obtener el bean del contexto.
 * Se verifican: respuesta normal (200 con cuerpo JSON), fallback cuando RabbitMQ no
 * es accesible (200 con todos los campos a cero) y estructura del JSON de respuesta.
 *
 * @author Lucas, Ana, Clara, Santiago
 * @version 1.0
 */
@WebMvcTest(DashboardController.class)
class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DashboardController dashboardController;

    private RestTemplate mockRestTemplate;

    /**
     * Sustituye el RestTemplate interno del controlador por un mock antes de cada test.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @BeforeEach
    void setUp() {
        mockRestTemplate = mock(RestTemplate.class);
        ReflectionTestUtils.setField(dashboardController, "restTemplate", mockRestTemplate);
    }

    /**
     * Verifica que cuando RabbitMQ no es accesible (RestTemplate lanza excepción),
     * el endpoint responde con HTTP 200 y todos los campos numéricos a cero (fallback).
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void getStats_RabbitMQUnavailable_Returns200WithZeros() throws Exception {
        when(mockRestTemplate.exchange(any(), any(), any(), eq(java.util.Map.class)))
                .thenThrow(new RestClientException("Connection refused"));
        when(mockRestTemplate.exchange(any(String.class), any(), any(), eq(java.util.List.class)))
                .thenThrow(new RestClientException("Connection refused"));

        mockMvc.perform(get("/Dashboard/Stats")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.queueDepth").value(0))
                .andExpect(jsonPath("$.deliverRate").value(0.0))
                .andExpect(jsonPath("$.publishRate").value(0.0))
                .andExpect(jsonPath("$.consumerCount").value(0))
                .andExpect(jsonPath("$.channels").isArray());
    }

    /**
     * Verifica que el endpoint siempre devuelve HTTP 200 independientemente de si
     * RabbitMQ está disponible o no (el controlador no propaga errores externos).
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void getStats_AlwaysReturns200() throws Exception {
        when(mockRestTemplate.exchange(any(), any(), any(), eq(java.util.Map.class)))
                .thenThrow(new RestClientException("timeout"));
        when(mockRestTemplate.exchange(any(String.class), any(), any(), eq(java.util.List.class)))
                .thenThrow(new RestClientException("timeout"));

        mockMvc.perform(get("/Dashboard/Stats"))
                .andExpect(status().isOk());
    }

    /**
     * Verifica que la respuesta contiene los campos esperados en el JSON
     * (queueDepth, deliverRate, publishRate, consumerCount, channels).
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void getStats_ResponseContainsAllExpectedFields() throws Exception {
        when(mockRestTemplate.exchange(any(), any(), any(), eq(java.util.Map.class)))
                .thenThrow(new RestClientException("unavailable"));
        when(mockRestTemplate.exchange(any(String.class), any(), any(), eq(java.util.List.class)))
                .thenThrow(new RestClientException("unavailable"));

        mockMvc.perform(get("/Dashboard/Stats")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.queueDepth").exists())
                .andExpect(jsonPath("$.deliverRate").exists())
                .andExpect(jsonPath("$.publishRate").exists())
                .andExpect(jsonPath("$.consumerCount").exists())
                .andExpect(jsonPath("$.channels").exists());
    }

    /**
     * Verifica que cuando RabbitMQ responde con datos de cola, los valores se mapean
     * correctamente al JSON de respuesta.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    @SuppressWarnings("unchecked")
    void getStats_RabbitMQResponds_MapsQueueDepthCorrectly() throws Exception {
        java.util.Map<String, Object> queueData = new java.util.HashMap<>();
        queueData.put("messages", 7L);
        queueData.put("consumers", 2L);
        java.util.Map<String, Object> deliverDetails = new java.util.HashMap<>();
        deliverDetails.put("rate", 1.5);
        queueData.put("deliver_get_details", deliverDetails);
        java.util.Map<String, Object> publishDetails = new java.util.HashMap<>();
        publishDetails.put("rate", 0.8);
        queueData.put("publish_details", publishDetails);

        when(mockRestTemplate.exchange(any(java.net.URI.class), any(), any(), eq(java.util.Map.class)))
                .thenReturn(new ResponseEntity<>(queueData, HttpStatus.OK));
        when(mockRestTemplate.exchange(any(String.class), any(), any(), eq(java.util.List.class)))
                .thenReturn(new ResponseEntity<>(java.util.List.of(), HttpStatus.OK));

        mockMvc.perform(get("/Dashboard/Stats")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.queueDepth").value(7))
                .andExpect(jsonPath("$.consumerCount").value(2))
                .andExpect(jsonPath("$.deliverRate").value(1.5))
                .andExpect(jsonPath("$.publishRate").value(0.8))
                .andExpect(jsonPath("$.channels").isArray());
    }
}
