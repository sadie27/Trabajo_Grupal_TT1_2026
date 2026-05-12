package org.trabajott1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.trabajott1.persistence.entity.ResultadoEntity;

import java.util.Optional;

/**
 * Repositorio para la entidad {@link ResultadoEntity}.
 */
@Repository
public interface ResultadoRepository extends JpaRepository<ResultadoEntity, Integer> {
    /**
     * Busca el resultado asociado a una solicitud a través de su token.
     * @param tokenSolicitud Token de la solicitud.
     * @return Opcional con el resultado encontrado.
     */
    Optional<ResultadoEntity> findBySolicitud_TokenSolicitud(Integer tokenSolicitud);
}
