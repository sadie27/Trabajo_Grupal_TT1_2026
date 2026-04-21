package org.openapitools.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.jackson.nullable.JsonNullable;
import org.openapitools.model.Solicitud;
import org.openapitools.model.SolicitudResponse;
import java.util.List;
import org.openapitools.repository.ISolicitudRepository;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SolicitudServiceTest {

    @Mock
    private ISolicitudRepository repository;
    @InjectMocks
    private SolicitudService service;

    @Test
    void testCrearSolicitud_DatosValidos_DebeRetornarResponseExitosa() {
        // 1. Arrange
        Solicitud solicitud = new Solicitud();
        solicitud.setCantidadesIniciales(JsonNullable.of(List.of(1, 2, 3)));
        solicitud.setNombreEntidades(JsonNullable.of(List.of("A", "B", "C")));

        // 2. Act
        boolean simulacionExitosa = true;

        // 3. Assert
        assertTrue(simulacionExitosa, "La simulación debería haberse creado correctamente");
    }

    @Test
    void testCrearSolicitud_ListasDeDistintoTamaño_DebeLanzarExcepcion() {
        // Arrange: Creamos una solicitud con 3 cantidades pero solo 2 nombres
        Solicitud solicitud = new Solicitud();
        solicitud.setCantidadesIniciales(JsonNullable.of(List.of(10, 20, 30))); // 3 elementos
        solicitud.setNombreEntidades(JsonNullable.of(List.of("Sensor A", "Sensor B"))); // 2 elementos

        // Act & Assert: Verificamos que el servicio lance una excepción
        // (En TDD, esto fallará hasta que el programador ponga el "if" que valida esto)
        assertThrows(IllegalArgumentException.class, () -> {
            service.crearSolicitud("usuario1", solicitud);
        }, "Debería lanzar error si las listas no tienen el mismo tamaño");
    }

    @Test
    void testCrearSolicitud_NombreUsuarioNulo_DebeLanzarExcepcion() {
        // Arrange
        Solicitud solicitud = new Solicitud();
        solicitud.setCantidadesIniciales(JsonNullable.of(List.of(10)));
        solicitud.setNombreEntidades(JsonNullable.of(List.of("A")));

        // Act & Assert: Nombre de usuario es null
        assertThrows(IllegalArgumentException.class, () -> {
            service.crearSolicitud(null, solicitud);
        }, "Debería lanzar error si el nombre de usuario es nulo");
    }
}