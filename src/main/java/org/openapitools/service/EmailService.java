package org.openapitools.service;

import org.openapitools.model.EmailResponse;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

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
