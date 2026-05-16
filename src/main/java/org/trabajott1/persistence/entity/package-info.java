/**
 * Paquete que contiene las entidades JPA mapeadas a las tablas de la base de datos.
 *
 * <p>Cada entidad representa una tabla y sus relaciones:
 * <ul>
 *   <li>{@link org.trabajott1.persistence.entity.SolicitudEntity} — tabla {@code solicitudes},
 *       almacena las solicitudes de simulación con su estado y token único.</li>
 *   <li>{@link org.trabajott1.persistence.entity.EntidadSolicitudEntity} — tabla {@code entidades_solicitud},
 *       almacena las especies participantes y sus cantidades iniciales por solicitud.</li>
 *   <li>{@link org.trabajott1.persistence.entity.ResultadoEntity} — tabla {@code resultados},
 *       almacena los datos de la simulación procesada y la fecha de procesamiento.</li>
 * </ul>
 *
 * @author Lucas, Ana, Clara, Santiago
 * @version 1.0
 */
package org.trabajott1.persistence.entity;
