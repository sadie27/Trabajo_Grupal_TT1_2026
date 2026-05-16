package org.trabajott1.service;

import org.springframework.stereotype.Service;
import org.trabajott1.model.EmailResponse;

/**
 * Servicio encargado de gestionar el envío de correos electrónicos.
 * Valida los parámetros de entrada y devuelve una respuesta con el resultado de la operación.
 *
 * @author Lucas, Ana, Clara, Santiago
 * @version 1.0
 */
@Service
public class EmailService {

    /**
     * Envía un correo electrónico al destinatario indicado con el mensaje proporcionado.
     *
     * @param email   la dirección de correo electrónico del destinatario
     * @param message el texto del mensaje a enviar
     * @return un {@link EmailResponse} con {@code done = true} si el envío fue exitoso
     * @throws IllegalArgumentException si el email es nulo o está vacío
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
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
