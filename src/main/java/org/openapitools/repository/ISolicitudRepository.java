package org.openapitools.repository;

import org.openapitools.domain.entity.SolicitudEntity;

import java.util.List;
import java.util.Optional;

public interface ISolicitudRepository {

    SolicitudEntity save(SolicitudEntity entity);

    Optional<SolicitudEntity> findByToken(Integer token);

    List<SolicitudEntity> findByNombreUsuario(String nombreUsuario);

    List<SolicitudEntity> findAll();
}