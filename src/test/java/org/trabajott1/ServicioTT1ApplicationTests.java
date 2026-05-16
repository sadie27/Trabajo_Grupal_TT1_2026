package org.trabajott1;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * Test de arranque de la aplicación Spring Boot.
 * Verifica que el contexto de aplicación se carga correctamente sin errores.
 * Usa un mock de RabbitTemplate para evitar la conexión real con RabbitMQ durante el test.
 *
 * @author Lucas, Ana, Clara, Santiago
 * @version 1.0
 */
@SpringBootTest
class ServicioTT1ApplicationTests {

    @MockBean
    private RabbitTemplate rabbitTemplate;

    /**
     * Comprueba que el contexto de Spring Boot se inicializa sin lanzar ninguna excepción.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void contextLoads() {
    }

}