package org.trabajott1;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.openapitools.jackson.nullable.JsonNullable;
import org.trabajott1.configuration.RabbitMQConfig;
import org.trabajott1.model.SimulationMessage;
import org.trabajott1.model.Solicitud;
import org.trabajott1.model.SolicitudResponse;
import org.trabajott1.repository.SolicitudRepository;
import org.trabajott1.service.SolicitudService;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

/**
 * Test de integración que verifica el flujo completo de creación de una solicitud:
 * persistencia en base de datos, asignación de token y envío del mensaje a RabbitMQ.
 * Usa una base de datos H2 en memoria y un mock de RabbitTemplate para evitar dependencias externas.
 *
 * @author Lucas, Ana, Clara, Santiago
 * @version 1.0
 */
@SpringBootTest(classes = ApiApplication.class)
@ComponentScan(basePackages = {"org.trabajott1"})
class PersistenceIntegrationTest {

    @Autowired
    private SolicitudService solicitudService;

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private SolicitudRepository solicitudRepository;

    /**
     * Verifica que al crear una solicitud se persiste correctamente en la base de datos,
     * su estado inicial es "PROCESANDO" y se envía un mensaje a RabbitMQ.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void testCrearSolicitudYEnvioMensaje() {
        // 1. Crear solicitud
        Solicitud solicitud = new Solicitud();
        solicitud.setNombreEntidades(JsonNullable.of(Arrays.asList("Entidad1", "Entidad2")));
        solicitud.setCantidadesIniciales(JsonNullable.of(Arrays.asList(10, 20)));

        SolicitudResponse response = solicitudService.crearSolicitud("usuario_rabbit", solicitud);
        Integer token = response.getTokenSolicitud();
        assertThat(token).isNotNull();

        // 2. Verificar que el estado inicial es PROCESANDO
        List<Integer> estado = solicitudService.comprobarSolicitud("usuario_rabbit", token);
        assertThat(estado).isEqualTo(List.of(0, 1));

        // 3. Verificar que se envió el mensaje a RabbitMQ
        verify(rabbitTemplate).convertAndSend(
            eq(RabbitMQConfig.SIMULATION_EXCHANGE),
            eq(RabbitMQConfig.SIMULATION_ROUTING_KEY),
            any(SimulationMessage.class)
        );
    }
}
