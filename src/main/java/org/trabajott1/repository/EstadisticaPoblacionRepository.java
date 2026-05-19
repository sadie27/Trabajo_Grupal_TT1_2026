package org.trabajott1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.trabajott1.persistence.entity.EstadisticaPoblacionEntity;

/**
 * Repositorio para la entidad EstadisticaPoblacionEntity.
 */
@Repository
public interface EstadisticaPoblacionRepository extends JpaRepository<EstadisticaPoblacionEntity, Integer> {
}
