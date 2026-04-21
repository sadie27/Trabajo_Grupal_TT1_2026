package org.openapitools.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SolicitudCustomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void cuandoEnvioJsonConWrapper_debeRetornarCreated() throws Exception {
        String json = "{\"solicitud\": {\"cantidadesIniciales\": [5, 3], \"nombreEntidades\": [\"A\", \"B\"]}}";

        mockMvc.perform(post("/Solicitud/Solicitar")
                        .param("nombreUsuario", "ana")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }
}