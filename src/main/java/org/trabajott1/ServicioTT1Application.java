package org.trabajott1;

import com.fasterxml.jackson.databind.Module;
import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;

@SpringBootApplication(
        nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class
)
@ComponentScan(
        basePackages = {"org.trabajott1", "org.trabajott1.api", "org.trabajott1.api.impl", "org.trabajott1.configuration", "org.trabajott1.model", "org.trabajott1.service"},
        nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class
)
public class ServicioTT1Application {

    public static void main(String[] args) {
        SpringApplication.run(ServicioTT1Application.class, args);
    }

    @Bean(name = "org.trabajott1.ServicioTT1Application.jsonNullableModule")
    public Module jsonNullableModule() {
        return new JsonNullableModule();
    }

}