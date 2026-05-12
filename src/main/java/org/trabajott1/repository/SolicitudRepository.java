package org.trabajott1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.trabajott1.persistence.entity.SolicitudEntity;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad {@link SolicitudEntity}.
 */
@Repository
public interface SolicitudRepository extends JpaRepository<SolicitudEntity, Integer> {
    /**
     * Busca una solicitud por su token único.
     * @param tokenSolicitud Token de la solicitud.
     * @return Opcional con la solicitud encontrada.
     */
    Optional<SolicitudEntity> findByTokenSolicitud(Integer tokenSolicitud);

    /**
     * Busca todas las solicitudes asociadas a un nombre de usuario.
     * @param nombreUsuario Nombre del usuario.
     * @return Lista de solicitudes del usuario.
     */
    List<SolicitudEntity> findByNombreUsuario(String nombreUsuario);
}
