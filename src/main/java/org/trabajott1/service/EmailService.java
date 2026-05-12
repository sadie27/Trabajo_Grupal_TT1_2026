package org.trabajott1.service;

import org.springframework.stereotype.Service;
import org.trabajott1.model.EmailResponse;

/**
 * Servicio para el envío de correos electrónicos.
 */
@Service
public class EmailService {

    /**
     * Envía un correo electrónico con el mensaje especificado.
     *
     * @param email   La dirección de correo electrónico del destinatario.
     * @param message El mensaje a enviar.
     * @return Un objeto EmailResponse indicando el resultado de la operación.
     * @throws IllegalArgumentException Si el email es nulo o está vacío.
     */
    public EmailResponse send(String email, String message) {
        // validaciones
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email vacío");
        }

        // lógica real

        return new EmailResponse()
                .done(true);
    }
}
