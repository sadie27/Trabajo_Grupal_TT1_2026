package org.trabajott1;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.openapitools.jackson.nullable.JsonNullable;
import org.trabajott1.model.Solicitud;
import org.trabajott1.model.SolicitudResponse;
import org.trabajott1.persistence.entity.SolicitudEntity;
import org.trabajott1.repository.SolicitudRepository;
import org.trabajott1.service.SolicitudService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PersistenceIntegrationTest {

    @Autowired
    private SolicitudService solicitudService;

    @Autowired
    private SolicitudRepository solicitudRepository;

    @Test
    void testFlujoAsincrono() throws InterruptedException {
        // 1. Crear solicitud
        Solicitud solicitud = new Solicitud();
        solicitud.setNombreEntidades(JsonNullable.of(Arrays.asList("Entidad1", "Entidad2")));
        solicitud.setCantidadesIniciales(JsonNullable.of(Arrays.asList(10, 20)));

        SolicitudResponse response = solicitudService.crearSolicitud("usuario_async", solicitud);
        Integer token = response.getTokenSolicitud();
        assertThat(token).isNotNull();

        // 2. Comprobar que inicialmente está procesando
        List<Integer> estadoInicial = solicitudService.comprobarSolicitud("usuario_async", token);
        assertThat(estadoInicial).isEqualTo(List.of(0, 1)); // [0, 1] significa procesando

        // 3. Esperar a que la simulación termine (pusimos 2 segundos de delay)
        int intentos = 0;
        List<Integer> estadoFinal = List.of(0, 1);
        while (intentos < 10 && estadoFinal.equals(List.of(0, 1))) {
            Thread.sleep(1000);
            estadoFinal = solicitudService.comprobarSolicitud("usuario_async", token);
            intentos++;
        }

        // 4. Verificar que terminó correctamente
        assertThat(estadoFinal).isEqualTo(List.of(1, 0)); // [1, 0] significa finalizada

        Optional<SolicitudEntity> saved = solicitudRepository.findByTokenSolicitud(token);
        assertThat(saved).isPresent();
        assertThat(saved.get().getEstado()).isEqualTo("FINALIZADA");
        assertThat(saved.get().getResultado()).isNotNull();
    }
}
