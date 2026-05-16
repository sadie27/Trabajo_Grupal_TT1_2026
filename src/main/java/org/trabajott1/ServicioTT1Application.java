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
 * Clase principal de la aplicación Spring Boot del Servicio Consumible TT1 2026.
 * Arranca el contexto de Spring, habilita la ejecución asíncrona y registra
 * todos los paquetes necesarios para que la inyección de dependencias funcione correctamente.
 *
 * @author Lucas, Ana, Clara, Santiago
 * @version 1.0
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
     * Punto de entrada principal de la aplicación. Inicia el servidor Spring Boot.
     *
     * @param args argumentos de la línea de comandos (no se usan habitualmente)
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    public static void main(String[] args) {
        SpringApplication.run(ServicioTT1Application.class, args);
    }

    /**
     * Registra el módulo de Jackson que permite serializar y deserializar
     * los tipos {@code JsonNullable} generados por OpenAPI.
     *
     * @return una instancia de {@link com.fasterxml.jackson.databind.Module} para {@code JsonNullable}
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Bean(name = "org.trabajott1.ServicioTT1Application.jsonNullableModule")
    public Module jsonNullableModule() {
        return new JsonNullableModule();
    }

}