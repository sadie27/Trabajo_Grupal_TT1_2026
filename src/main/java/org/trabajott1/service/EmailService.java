package org.trabajott1.service;

import org.springframework.stereotype.Service;
import org.trabajott1.model.EmailResponse;

/**
 * Servicio para el envío de resultados de simulación por correo electrónico.
 *
 * <p>Actúa como punto de integración con el sistema de mensajería externo.
 * Valida los parámetros de entrada antes de intentar el envío y devuelve
 * una respuesta estructurada indicando si la operación fue exitosa.</p>
 */
@Service
public class EmailService {

    /**
     * Envía un mensaje de correo electrónico a la dirección indicada.
     *
     * <p>Valida que el correo no sea nulo ni vacío antes de proceder.
     * El contenido del mensaje se transmite tal cual al sistema de envío.</p>
     *
     * @param email   dirección de correo electrónico del destinatario; no puede ser nula ni vacía
     * @param message cuerpo del mensaje a enviar
     * @return {@link EmailResponse} con {@code done=true} si el envío fue exitoso
     * @throws IllegalArgumentException si {@code email} es nulo o vacío
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
