package org.trabajott1.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de SpringDoc para la generación automática de la documentación OpenAPI (Swagger UI).
 * Define el título, descripción y versión de la API que aparecen en la interfaz de Swagger.
 *
 * @author Lucas, Ana, Clara, Santiago
 * @version 1.0
 */
@Configuration
public class SpringDocConfiguration {

    /**
     * Crea y configura el objeto {@link OpenAPI} con los metadatos básicos de la API.
     *
     * @return instancia de {@link OpenAPI} con título, descripción y versión configurados
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
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