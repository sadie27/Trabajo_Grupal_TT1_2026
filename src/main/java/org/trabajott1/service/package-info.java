/**
 * Paquete que contiene la lógica de negocio de la aplicación.
 *
 * <p>Cada servicio encapsula una responsabilidad concreta:
 * <ul>
 * <li>{@link org.trabajott1.service.SolicitudService} — gestiona el ciclo de vida de las
 * solicitudes: creación, asignación de token y consulta de estado.</li>
 * <li>{@link org.trabajott1.service.ResultadosService} — consulta y devuelve los resultados
 * de simulaciones ya procesadas.</li>
 * <li>{@link org.trabajott1.service.EmailService} — gestiona el envío de correos electrónicos.</li>
 * <li>{@link org.trabajott1.service.ISimulationService} — define el contrato para la ejecución de la simulación
 * de vida artificial basada en autómatas celulares sobre un grid de 8×8 durante 10 pasos de tiempo.</li>
 * <li>{@link org.trabajott1.service.SimulationListenerA} y {@link org.trabajott1.service.SimulationListenerB} —
 * Workers independientes que escuchan sus respectivas colas de RabbitMQ (patrón muchos-a-muchos)
 * y delegan el procesamiento asíncrono en el servicio de simulación.</li>
 * </ul>
 *
 * @author Lucas, Ana, Clara, Santiago
 * @version 2.0
 */
package org.trabajott1.service;