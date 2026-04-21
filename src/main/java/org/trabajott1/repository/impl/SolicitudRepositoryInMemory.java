package org.trabajott1.repository.impl;

import org.springframework.stereotype.Repository;
import org.trabajott1.domain.entity.SolicitudEntity;
import org.trabajott1.repository.ISolicitudRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
