package org.trabajott1.service;

import org.junit.jupiter.api.Test;
import org.trabajott1.model.EmailResponse;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para {@link EmailService}.
 * Verifica la validación de parámetros y el comportamiento exitoso del envío de correo.
 *
 * @author Lucas, Ana, Clara, Santiago
 * @version 1.0
 */
class EmailServiceTest {

    private final EmailService emailService = new EmailService();

    /**
     * Verifica que pasar un email nulo lanza {@link IllegalArgumentException}.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void send_NullEmail_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> emailService.send(null, "message"));
    }

    /**
     * Verifica que pasar un email vacío lanza {@link IllegalArgumentException}.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void send_EmptyEmail_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> emailService.send("", "message"));
    }

    /**
     * Verifica que con un email válido el servicio devuelve una respuesta con {@code done = true}.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void send_Success() {
        EmailResponse response = emailService.send("test@example.com", "Hello");
        assertTrue(response.getDone());
    }
}
