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
 * Clase principal que arranca el microservicio de API para la gestión de simulaciones.
 * Este módulo se encarga de recibir las solicitudes de los usuarios y delegar el procesamiento al Worker a través de RabbitMQ.
 *
 * @author Lucas, Ana, Clara, Santiago
 * @version 1.0
 */
@SpringBootApplication(
        nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class
)
@EnableAsync
@ComponentScan(
        basePackages = {"org.trabajott1", "org.trabajott1.api", "org.trabajott1.configuration", "org.trabajott1.service"},
        nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class
)
public class ApiApplication {

    /**
     * Punto de entrada principal de la aplicación. Inicia el servidor Spring Boot.
     *
     * @param args argumentos de la línea de comandos
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

    /**
     * Registra el módulo de Jackson para JsonNullable.
     * 
     * @return una instancia de {@link com.fasterxml.jackson.databind.Module} para {@code JsonNullable}
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Bean(name = "org.trabajott1.ApiApplication.jsonNullableModule")
    public Module jsonNullableModule() {
        return new JsonNullableModule();
    }

}