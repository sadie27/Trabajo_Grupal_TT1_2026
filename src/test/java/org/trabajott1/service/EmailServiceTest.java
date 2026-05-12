package org.trabajott1.service;

import org.junit.jupiter.api.Test;
import org.trabajott1.model.EmailResponse;

import static org.junit.jupiter.api.Assertions.*;

class EmailServiceTest {

    private final EmailService emailService = new EmailService();

    @Test
    void send_NullEmail_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> emailService.send(null, "message"));
    }

    @Test
    void send_EmptyEmail_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> emailService.send("", "message"));
    }

    @Test
    void send_Success() {
        EmailResponse response = emailService.send("test@example.com", "Hello");
        assertTrue(response.getDone());
    }
}
