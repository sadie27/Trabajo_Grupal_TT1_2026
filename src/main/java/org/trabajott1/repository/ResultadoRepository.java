package org.trabajott1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.trabajott1.persistence.entity.ResultadoEntity;

import java.util.Optional;

@Repository
public interface ResultadoRepository extends JpaRepository<ResultadoEntity, Integer> {
    Optional<ResultadoEntity> findBySolicitud_TokenSolicitud(Integer tokenSolicitud);
}
