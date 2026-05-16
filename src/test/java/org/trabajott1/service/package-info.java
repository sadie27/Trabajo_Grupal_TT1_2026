/**
 * Paquete que contiene los tests unitarios y de regresión de los servicios de negocio.
 *
 * <p>Incluye tests para:
 * <ul>
 *   <li>Validación de parámetros en {@code EmailService}, {@code ResultadosService} y {@code SolicitudService}.</li>
 *   <li>Lógica de comprobación y creación de solicitudes con mocks de repositorio y RabbitMQ.</li>
 *   <li>Comportamiento del motor de simulación: jerarquía alimentaria, reproducción y ausencia de pérdidas incorrectas de células.</li>
 *   <li>Delegación correcta del {@code SimulationListener} hacia {@code SimulationService} y manejo de excepciones.</li>
 * </ul>
 *
 * @author Lucas, Ana, Clara, Santiago
 * @version 1.0
 */
package org.trabajott1.service;
