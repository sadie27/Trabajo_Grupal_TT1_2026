package org.trabajott1.configuration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests de capa web para {@link HomeController}.
 * Verifica que {@code GET /} devuelve una redirección hacia swagger-ui.html.
 *
 * @author Lucas, Ana, Clara, Santiago
 * @version 1.0
 */
@WebMvcTest(
        value = HomeController.class,
        excludeAutoConfiguration = {
                RabbitAutoConfiguration.class,
                DataSourceAutoConfiguration.class,
                DataSourceTransactionManagerAutoConfiguration.class,
                HibernateJpaAutoConfiguration.class
        }
)
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Verifica que {@code GET /} responde con HTTP 302 y la cabecera Location
     * apunta a swagger-ui.html.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void getRoot_RedirectsToSwaggerUi() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("swagger-ui.html"));
    }

    /**
     * Verifica que la redirección no es permanente (HTTP 302, no 301),
     * de modo que los clientes repitan la petición en cada visita.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void getRoot_RedirectIsTemporary() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isFound());
    }
}
