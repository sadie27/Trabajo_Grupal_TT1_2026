package org.trabajott1.api.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.trabajott1.service.EstadisticasService;
import org.trabajott1.service.EstadisticasService.EstadisticasResponse;
import org.trabajott1.service.ResultadosService;
import org.trabajott1.service.SolicitudService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests de capa web para {@link EstadisticasController}.
 * Verifica los códigos HTTP y el cuerpo de respuesta para los casos de éxito (200),
 * token sin datos (404) y ausencia del parámetro obligatorio (400).
 *
 * @author Lucas, Ana, Clara, Santiago
 * @version 1.0
 */
@WebMvcTest(
        value = EstadisticasController.class,
        excludeAutoConfiguration = {
                RabbitAutoConfiguration.class,
                DataSourceAutoConfiguration.class,
                DataSourceTransactionManagerAutoConfiguration.class,
                HibernateJpaAutoConfiguration.class
        }
)
class EstadisticasControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EstadisticasService estadisticasService;

    @MockBean
    private ResultadosService resultadosService;

    @MockBean
    private SolicitudService solicitudService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Verifica que cuando el servicio devuelve {@code done = true} el endpoint
     * responde con HTTP 200 y el cuerpo JSON contiene el token.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void getEstadisticas_Success_Returns200() throws Exception {
        Integer tok = 12345;
        EstadisticasResponse okResponse = new EstadisticasResponse(
                true, null, tok,
                List.of(new EstadisticasService.EntidadDto("Rojo", "red")),
                List.of(new EstadisticasService.PoblacionPasoDto(0, "red", 10)),
                "red", "yellow", "red");

        when(estadisticasService.obtenerEstadisticas(tok)).thenReturn(okResponse);

        mockMvc.perform(get("/Estadisticas")
                        .param("tok", tok.toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.done").value(true))
                .andExpect(jsonPath("$.tokenSolicitud").value(tok))
                .andExpect(jsonPath("$.colorMasReproducido").value("red"))
                .andExpect(jsonPath("$.colorMasDominante").value("red"));
    }

    /**
     * Verifica que cuando el servicio devuelve {@code done = false} (token no encontrado)
     * el endpoint responde con HTTP 404 y el cuerpo contiene el mensaje de error.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void getEstadisticas_NotFound_Returns404() throws Exception {
        Integer tok = 99999;
        EstadisticasResponse errorResponse = new EstadisticasResponse(
                false, "No se encontraron estadísticas para el token " + tok,
                tok, null, null, null, null, null);

        when(estadisticasService.obtenerEstadisticas(tok)).thenReturn(errorResponse);

        mockMvc.perform(get("/Estadisticas")
                        .param("tok", tok.toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.done").value(false))
                .andExpect(jsonPath("$.errorMessage").isNotEmpty());
    }

    /**
     * Verifica que cuando el servicio devuelve {@code done = false} por token null
     * (pasando tok=null como string vacío), el endpoint devuelve HTTP 400 al no poder
     * convertir el parámetro a Integer.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void getEstadisticas_MissingTok_Returns400() throws Exception {
        mockMvc.perform(get("/Estadisticas")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * Verifica que cuando el servicio devuelve {@code done = false} por token null
     * el endpoint responde con HTTP 404 y el cuerpo tiene done=false.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void getEstadisticas_NullToken_Returns404() throws Exception {
        EstadisticasResponse errorResponse = new EstadisticasResponse(
                false, "Token requerido", null, null, null, null, null, null);

        when(estadisticasService.obtenerEstadisticas(null)).thenReturn(errorResponse);

        mockMvc.perform(get("/Estadisticas")
                        .param("tok", (String) null)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
