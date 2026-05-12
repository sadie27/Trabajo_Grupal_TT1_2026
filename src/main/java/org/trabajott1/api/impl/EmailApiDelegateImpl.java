package org.trabajott1.api.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.trabajott1.api.EmailApiDelegate;
import org.trabajott1.model.EmailResponse;
import org.trabajott1.service.EmailService;

/**
 * Implementación del delegado para la API de envío de correos.
 */
@Service
public class EmailApiDelegateImpl implements EmailApiDelegate {

    private final EmailService emailService;

    /**
     * Constructor de EmailApiDelegateImpl.
     *
     * @param emailService El servicio de correo electrónico a utilizar.
     */
    public EmailApiDelegateImpl(EmailService emailService) {
        this.emailService = emailService;
    }

    /**
     * Punto de entrada para la petición POST de envío de correo.
     *
     * @param emailAddress La dirección de correo.
     * @param message      El contenido del mensaje.
     * @return Una respuesta con el resultado de la operación.
     */
    @Override
    public ResponseEntity<EmailResponse> emailPost(String emailAddress, String message) {
        try {
            EmailResponse response = emailService.send(emailAddress, message);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
