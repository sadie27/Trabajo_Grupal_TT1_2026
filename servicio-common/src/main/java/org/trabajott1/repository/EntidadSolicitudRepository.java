package org.trabajott1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.trabajott1.persistence.entity.EntidadSolicitudEntity;

import java.util.List;

/**
 * Repositorio Spring Data JPA para {@link EntidadSolicitudEntity}.
 * Proporciona acceso a la tabla {@code entidades_solicitud}.
 *
 * @author Lucas, Ana, Clara, Santiago
 */
@Repository
public interface EntidadSolicitudRepository extends JpaRepository<EntidadSolicitudEntity, Integer> {

    /**
     * Devuelve todas las entidades pertenecientes a la solicitud indicada, ordenadas por ID ascendente.
     * El orden garantiza que el índice de color asignado en {@code EstadisticasService} sea estable.
     *
     * @param token token de la solicitud
     * @return lista de entidades ordenadas por {@code id} ascendente; vacía si el token no existe
     */
    List<EntidadSolicitudEntity> findBySolicitud_TokenSolicitudOrderByIdAsc(Integer token);
}
