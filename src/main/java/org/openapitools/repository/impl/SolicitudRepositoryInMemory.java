package org.openapitools.repository.impl;

import org.openapitools.domain.entity.SolicitudEntity;
import org.openapitools.repository.ISolicitudRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Implementación en memoria del repositorio de Solicitud.
 * Sin base de datos.
 */
@Repository
public class SolicitudRepositoryInMemory implements ISolicitudRepository {

    private final Map<Integer, SolicitudEntity> storage = new ConcurrentHashMap<>();
    private final AtomicInteger tokenGenerator = new AtomicInteger(1000);

    @Override
    public SolicitudEntity save(SolicitudEntity entity) {
        if (entity.getToken() == null) {
            entity.setToken(tokenGenerator.getAndIncrement());
        }
        storage.put(entity.getToken(), entity);
        return entity;
    }

    @Override
    public Optional<SolicitudEntity> findByToken(Integer token) {
        return Optional.ofNullable(storage.get(token));
    }

    @Override
    public List<SolicitudEntity> findByNombreUsuario(String nombreUsuario) {
        return storage.values().stream()
            .filter(s -> s.getNombreUsuario() != null &&
                        s.getNombreUsuario().equals(nombreUsuario))
            .collect(Collectors.toList());
    }

    @Override
    public List<SolicitudEntity> findAll() {
        return new ArrayList<>(storage.values());
    }
}
