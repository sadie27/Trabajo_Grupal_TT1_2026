package org.trabajott1;

import com.fasterxml.jackson.databind.Module;
import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Clase principal de la aplicación Spring Boot.
 */
@SpringBootApplication(
        nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class
)
@EnableAsync
@ComponentScan(
        basePackages = {"org.trabajott1", "org.trabajott1.api", "org.trabajott1.api.impl", "org.trabajott1.configuration", "org.trabajott1.model", "org.trabajott1.repository", "org.trabajott1.service"},
        nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class
)
public class ServicioTT1Application {

    /**
     * Punto de entrada principal de la aplicación.
     * @param args Argumentos de la línea de comandos.
     */
    public static void main(String[] args) {
        SpringApplication.run(ServicioTT1Application.class, args);
    }

    /**
     * Bean para el manejo de tipos JsonNullable de OpenAPI.
     * @return Módulo de Jackson para JsonNullable.
     */
    @Bean(name = "org.trabajott1.ServicioTT1Application.jsonNullableModule")
    public Module jsonNullableModule() {
        return new JsonNullableModule();
    }

}