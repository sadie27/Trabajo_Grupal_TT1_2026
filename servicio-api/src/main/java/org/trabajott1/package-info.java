/**
 * Paquete raíz de la aplicación ServicioConsumible TT1 2026.
 *
 * <p>Contiene la clase de arranque de Spring Boot ({@link org.trabajott1.ServicioTT1Application})
 * y el formateador de fechas RFC3339 usado por la capa de serialización JSON.
 *
 * <p>La arquitectura del proyecto sigue el patrón de capas:
 * <ul>
 *   <li>{@code api} — controladores e interfaces REST generados por OpenAPI</li>
 *   <li>{@code service} — lógica de negocio</li>
 *   <li>{@code repository} — acceso a datos vía Spring Data JPA</li>
 *   <li>{@code persistence.entity} — entidades JPA mapeadas a la base de datos</li>
 *   <li>{@code model} — modelos de entrada/salida de la API</li>
 *   <li>{@code configuration} — configuración de Spring (RabbitMQ, Swagger, etc.)</li>
 *   <li>{@code domain.dto} — objetos de transferencia de datos auxiliares</li>
 * </ul>
 *
 * @author Lucas, Ana, Clara, Santiago
 * @version 1.0
 */
package org.trabajott1;
