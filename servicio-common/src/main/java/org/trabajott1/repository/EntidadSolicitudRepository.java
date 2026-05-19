package org.trabajott1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.trabajott1.persistence.entity.EntidadSolicitudEntity;

import java.util.List;

/**
 * Repositorio para la entidad EntidadSolicitudEntity.
 */
@Repository
public interface EntidadSolicitudRepository extends JpaRepository<EntidadSolicitudEntity, Integer> {

    List<EntidadSolicitudEntity> findBySolicitud_TokenSolicitudOrderByIdAsc(Integer token);
}
