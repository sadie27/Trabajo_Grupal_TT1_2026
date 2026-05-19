/**
 * Paquete que contiene los repositorios Spring Data JPA para el acceso a la base de datos.
 *
 * <p>Cada repositorio extiende {@link org.springframework.data.jpa.repository.JpaRepository}
 * y añade consultas personalizadas según las necesidades del negocio:
 * <ul>
 *   <li>{@link org.trabajott1.repository.SolicitudRepository} — búsqueda por token y por usuario.</li>
 *   <li>{@link org.trabajott1.repository.ResultadoRepository} — búsqueda por token de solicitud.</li>
 * </ul>
 *
 * @author Lucas, Ana, Clara, Santiago
 * @version 1.0
 */
package org.trabajott1.repository;
