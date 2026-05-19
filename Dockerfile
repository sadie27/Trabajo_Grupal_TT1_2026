# Etapa de construcción común
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copiar pom raíz y poms de módulos
COPY pom.xml .
COPY servicio-common/pom.xml servicio-common/
COPY servicio-api/pom.xml servicio-api/
COPY servicio-worker/pom.xml servicio-worker/

# Descargar dependencias (cacheable)
RUN mvn dependency:go-offline -B

# Copiar código fuente
COPY servicio-common/src servicio-common/src
COPY servicio-api/src servicio-api/src
COPY servicio-worker/src servicio-worker/src

# Compilar todo
RUN mvn clean package -DskipTests

# Imagen para el API
FROM eclipse-temurin:21-jre AS api
WORKDIR /app
COPY --from=build /app/servicio-api/target/servicio-api-*.jar api.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "api.jar"]

# Imagen para el Worker
FROM eclipse-temurin:21-jre AS worker
WORKDIR /app
COPY --from=build /app/servicio-worker/target/servicio-worker-*.jar worker.jar
ENTRYPOINT ["java", "-jar", "worker.jar"]
