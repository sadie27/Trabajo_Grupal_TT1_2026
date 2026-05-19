package org.trabajott1.service;

import org.junit.jupiter.api.Test;
import org.trabajott1.model.EmailResponse;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests extendidos para {@link EmailService}.
 * Complementa {@link EmailServiceTest} cubriendo los casos de mensaje null y vacío,
 * documentando el comportamiento actual de la implementación stub.
 *
 * @author Lucas, Ana, Clara, Santiago
 * @version 1.0
 */
class EmailServiceExtendedTest {

    private final EmailService emailService = new EmailService();

    /**
     * Verifica que pasar un mensaje {@code null} con un email válido NO lanza excepción.
     * La implementación actual no valida el mensaje, por lo que devuelve {@code done = true}.
     * Este test documenta el comportamiento actual: si en el futuro se añade validación
     * del mensaje, este test fallará como señal de que hay que actualizarlo.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void send_NullMessage_DoesNotThrowAndReturnsDone() {
        EmailResponse response = emailService.send("user@example.com", null);
        assertTrue(response.getDone(),
                "Con mensaje null la implementación stub devuelve done=true");
    }

    /**
     * Verifica que pasar un mensaje vacío con un email válido NO lanza excepción.
     * La implementación actual no valida el contenido del mensaje.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void send_EmptyMessage_DoesNotThrowAndReturnsDone() {
        EmailResponse response = emailService.send("user@example.com", "");
        assertTrue(response.getDone(),
                "Con mensaje vacío la implementación stub devuelve done=true");
    }

    /**
     * Verifica que un email válido con mensaje de texto normal devuelve {@code done = true}.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void send_ValidEmailAndMessage_ReturnsDone() {
        EmailResponse response = emailService.send("destino@test.com", "Hola, esta es tu simulación.");
        assertTrue(response.getDone());
    }
}
