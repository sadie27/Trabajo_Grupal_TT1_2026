package org.trabajott1.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de SpringDoc para la generación de documentación OpenAPI (Swagger).
 */
@Configuration
public class SpringDocConfiguration {

    /**
     * Define la información básica de la API para la documentación.
     * @return Instancia de OpenAPI con la info configurada.
     */
    @Bean(name = "org.openapitools.configuration.SpringDocConfiguration.apiInfo")
    OpenAPI apiInfo() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("ServicioConsumible")
                                .description("Servicio de simulación para el Trabajo Grupal TT1 2026")
                                .version("1.0")
                )
                ;
    }
}