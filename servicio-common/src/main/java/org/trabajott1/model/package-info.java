/**
 * Paquete que contiene los modelos de datos de la API REST.
 *
 * <p>Incluye las clases de entrada y salida de los endpoints, generadas en su mayoría
 * por OpenAPI Generator y ajustadas donde fue necesario:
 * <ul>
 *   <li>{@link org.trabajott1.model.Solicitud} — datos de entrada para crear una simulación.</li>
 *   <li>{@link org.trabajott1.model.SolicitudResponse} — respuesta al crear una solicitud.</li>
 *   <li>{@link org.trabajott1.model.ResultsResponse} — respuesta con los resultados de una simulación.</li>
 *   <li>{@link org.trabajott1.model.EmailResponse} — respuesta del endpoint de correo electrónico.</li>
 *   <li>{@link org.trabajott1.model.ProblemDetails} — modelo de error estándar (RFC 7807).</li>
 *   <li>{@link org.trabajott1.model.SimulationMessage} — mensaje serializable para RabbitMQ.</li>
 *   <li>{@link org.trabajott1.model.SolicitudDeserializer} — deserializador personalizado de {@code Solicitud}.</li>
 * </ul>
 *
 * @author Lucas, Ana, Clara, Santiago
 * @version 1.0
 */
package org.trabajott1.model;
