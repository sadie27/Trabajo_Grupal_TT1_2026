package org.trabajott1.api.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.trabajott1.model.EmailResponse;
import org.trabajott1.service.EmailService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailApiDelegateImplTest {

    @Mock
    private EmailService emailService;

    private EmailApiDelegateImpl emailApiDelegate;

    @BeforeEach
    void setUp() {
        emailApiDelegate = new EmailApiDelegateImpl(emailService);
    }

    @Test
    void emailPost_Success() {
        String email = "test@example.com";
        String message = "Hello";
        EmailResponse mockResponse = new EmailResponse().done(true);

        when(emailService.send(email, message)).thenReturn(mockResponse);

        ResponseEntity<EmailResponse> response = emailApiDelegate.emailPost(email, message);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());
    }

    @Test
    void emailPost_BadRequest() {
        when(emailService.send(anyString(), anyString())).thenThrow(new IllegalArgumentException("Invalid email"));

        ResponseEntity<EmailResponse> response = emailApiDelegate.emailPost("invalid", "message");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
