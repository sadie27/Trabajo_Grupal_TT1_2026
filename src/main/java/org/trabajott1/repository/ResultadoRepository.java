package org.trabajott1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.trabajott1.persistence.entity.ResultadoEntity;

import java.util.Optional;

/**
 * Repositorio JPA para acceder y gestionar los datos de {@link ResultadoEntity} en la base de datos.
 * Extiende {@link JpaRepository} con operaciones CRUD estándar y añade una búsqueda por token.
 *
 * @author Lucas, Ana, Clara, Santiago
 * @version 1.0
 */
@Repository
public interface ResultadoRepository extends JpaRepository<ResultadoEntity, Integer> {

    /**
     * Busca el resultado de una simulación a partir del token único de su solicitud asociada.
     *
     * @param tokenSolicitud el token numérico de 5 dígitos que identifica la solicitud
     * @return un {@link Optional} con el resultado si existe, o vacío si no se encontró
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    Optional<ResultadoEntity> findBySolicitud_TokenSolicitud(Integer tokenSolicitud);
}
