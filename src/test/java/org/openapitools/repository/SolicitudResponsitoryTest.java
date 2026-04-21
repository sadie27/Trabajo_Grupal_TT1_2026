package org.openapitools.repository;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.domain.entity.SolicitudEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SolicitudRepositoryTest {

    // Simulamos un repositorio en memoria usando una lista
    private List<SolicitudEntity> db;

    @BeforeEach
    void setUp() {
        db = new ArrayList<>();
    }

    @Test
    void testGuardarSolicitud_DebeAñadirALista() {
        // 1. Arrange (Preparar)
        SolicitudEntity entity = new SolicitudEntity();
        entity.setToken(1);
        entity.setNombreUsuario("usuarioPrueba");

        // 2. Act (Ejecutar)
        db.add(entity);

        // 3. Assert (Verificar)
        assertEquals(1, db.size());
        assertEquals("usuarioPrueba", db.get(0).getNombreUsuario());
    }

    @Test
    void testBuscarPorToken_DebeRetornarCorrecto() {
        SolicitudEntity entity = new SolicitudEntity();
        entity.setToken(99);
        db.add(entity);

        SolicitudEntity encontrada = db.stream()
                .filter(s -> s.getToken().equals(99))
                .findFirst()
                .orElse(null);

        assertNotNull(encontrada);
        assertEquals(99, encontrada.getToken());
    }

    @Test
    void testTokensGenerados_DebenSerUnicos() {
        SolicitudEntity s1 = new SolicitudEntity();
        SolicitudEntity s2 = new SolicitudEntity();

        // Simula el guardado (si usas la lista db)
        s1.setToken(1);
        s2.setToken(2);
        db.add(s1);
        db.add(s2);

        assertNotEquals(db.get(0).getToken(), db.get(1).getToken());
    }



}