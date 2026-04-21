package org.trabajott1.service;

import org.springframework.stereotype.Service;
import org.trabajott1.model.EmailResponse;

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
