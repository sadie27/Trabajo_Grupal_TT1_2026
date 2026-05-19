package org.trabajott1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;

/**
 * Clase principal que arranca el microservicio de Worker para la ejecución de simulaciones.
 * Este módulo escucha tareas de RabbitMQ, ejecuta la lógica de simulación y persiste los resultados.
 *
 * @author Lucas, Ana, Clara, Santiago
 * @version 1.0
 */
@SpringBootApplication(
        nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class
)
@ComponentScan(
        basePackages = {"org.trabajott1.configuration", "org.trabajott1.service", "org.trabajott1.repository", "org.trabajott1.persistence"},
        nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class
)
public class WorkerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkerApplication.class, args);
    }
}
