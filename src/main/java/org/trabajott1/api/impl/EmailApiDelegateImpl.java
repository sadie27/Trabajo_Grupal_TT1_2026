package org.trabajott1.api.impl;

import org.trabajott1.api.EmailApiDelegate;
import org.trabajott1.model.EmailResponse;
import org.trabajott1.service.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class EmailApiDelegateImpl implements EmailApiDelegate {

    private final EmailService emailService;

    public EmailApiDelegateImpl(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public ResponseEntity<EmailResponse> emailPost(String emailAddress, String message) {
        EmailResponse response = emailService.send(emailAddress, message);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
