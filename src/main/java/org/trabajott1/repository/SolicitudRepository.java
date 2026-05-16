package org.trabajott1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.trabajott1.persistence.entity.SolicitudEntity;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para acceder y gestionar los datos de {@link SolicitudEntity} en la base de datos.
 * Extiende {@link JpaRepository} con operaciones CRUD estándar y añade consultas por token y por usuario.
 *
 * @author Lucas, Ana, Clara, Santiago
 * @version 1.0
 */
@Repository
public interface SolicitudRepository extends JpaRepository<SolicitudEntity, Integer> {

    /**
     * Busca una solicitud de simulación a partir de su token único.
     *
     * @param tokenSolicitud el token numérico de 5 dígitos que identifica la solicitud
     * @return un {@link Optional} con la solicitud si existe, o vacío si no se encontró
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    Optional<SolicitudEntity> findByTokenSolicitud(Integer tokenSolicitud);

    /**
     * Devuelve todas las solicitudes creadas por un usuario concreto.
     *
     * @param nombreUsuario el nombre del usuario del que se quieren recuperar las solicitudes
     * @return lista con todas las solicitudes del usuario, vacía si no tiene ninguna
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    List<SolicitudEntity> findByNombreUsuario(String nombreUsuario);
}
