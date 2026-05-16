/**
 * Paquete que contiene la lógica de negocio de la aplicación.
 *
 * <p>Cada servicio encapsula una responsabilidad concreta:
 * <ul>
 *   <li>{@link org.trabajott1.service.SolicitudService} — gestiona el ciclo de vida de las
 *       solicitudes: creación, asignación de token y consulta de estado.</li>
 *   <li>{@link org.trabajott1.service.ResultadosService} — consulta y devuelve los resultados
 *       de simulaciones ya procesadas.</li>
 *   <li>{@link org.trabajott1.service.EmailService} — gestiona el envío de correos electrónicos.</li>
 *   <li>{@link org.trabajott1.service.SimulationService} — ejecuta la simulación de vida artificial
 *       basada en autómatas celulares sobre un grid de 8×8 durante 10 pasos de tiempo.</li>
 *   <li>{@link org.trabajott1.service.SimulationListener} — escucha la cola de RabbitMQ y
 *       delega la ejecución de cada simulación en {@code SimulationService}.</li>
 * </ul>
 *
 * @author Lucas, Ana, Clara, Santiago
 * @version 1.0
 */
package org.trabajott1.service;
