package org.trabajott1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.trabajott1.persistence.entity.SolicitudEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface SolicitudRepository extends JpaRepository<SolicitudEntity, Long> {
    Optional<SolicitudEntity> findByTokenSolicitud(Integer tokenSolicitud);
    List<SolicitudEntity> findByNombreUsuario(String nombreUsuario);
}
