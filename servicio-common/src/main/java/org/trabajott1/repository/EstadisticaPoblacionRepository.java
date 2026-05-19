package org.trabajott1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.trabajott1.persistence.entity.EstadisticaPoblacionEntity;

import java.util.List;

/**
 * Repositorio Spring Data JPA para {@link EstadisticaPoblacionEntity}.
 * Proporciona acceso a la tabla {@code estadisticas_poblacion}.
 *
 * @author Lucas, Ana, Clara, Santiago
 * @version 1.0
 */
@Repository
public interface EstadisticaPoblacionRepository extends JpaRepository<EstadisticaPoblacionEntity, Integer> {

    /**
     * Devuelve todos los registros de estadísticas de población asociados al token de solicitud dado.
     *
     * @param token token de la solicitud
     * @return lista de registros de estadística; vacía si el token no existe o no hay datos
     */
    List<EstadisticaPoblacionEntity> findBySolicitud_TokenSolicitud(Integer token);
}
