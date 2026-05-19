package org.trabajott1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.trabajott1.persistence.entity.EstadisticaPoblacionEntity;

/**
 * Repositorio para la entidad EstadisticaPoblacionEntity.
 * 
 * @author Lucas, Ana, Clara, Santiago
 * @version 1.0
 */
@Repository
public interface EstadisticaPoblacionRepository extends JpaRepository<EstadisticaPoblacionEntity, Integer> {
}
