package org.trabajott1.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trabajott1.persistence.entity.SolicitudEntity;
import org.trabajott1.repository.SolicitudRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para {@link SimulationService}.
 * Verifica que la simulación actualiza correctamente la entidad cuando la solicitud existe
 * y que no hace nada si la solicitud no se encuentra.
 *
 * @author Lucas, Ana, Clara, Santiago
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
class SimulationServiceTest {

    @Mock
    private SolicitudRepository solicitudRepository;

    @InjectMocks
    private SimulationService simulationService;

    /**
     * Verifica que cuando la solicitud existe, la simulación se ejecuta y la entidad queda con estado "FINALIZADA"
     * y con un resultado no nulo asociado.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void executeSimulation_SolicitudFound_UpdatesEntity() {
        Integer solicitudId = 1;
        List<String> names = List.of("A", "B");
        List<Integer> quantities = List.of(5, 5);

        SolicitudEntity entity = new SolicitudEntity();
        entity.setIdSolicitud(solicitudId);
        entity.setEstado("PROCESANDO");

        when(solicitudRepository.findById(solicitudId)).thenReturn(Optional.of(entity));

        simulationService.executeSimulation(solicitudId, names, quantities);

        ArgumentCaptor<SolicitudEntity> captor = ArgumentCaptor.forClass(SolicitudEntity.class);
        verify(solicitudRepository).save(captor.capture());

        SolicitudEntity savedEntity = captor.getValue();
        assertEquals("FINALIZADA", savedEntity.getEstado());
        assertNotNull(savedEntity.getResultado());
        assertNotNull(savedEntity.getResultado().getDatosResultado());
        assertEquals(savedEntity, savedEntity.getResultado().getSolicitud());
    }

    /**
     * Verifica que cuando la solicitud no existe en la base de datos, no se guarda ningún resultado.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void executeSimulation_SolicitudNotFound_DoesNothing() {
        Integer solicitudId = 1;
        when(solicitudRepository.findById(solicitudId)).thenReturn(Optional.empty());

        simulationService.executeSimulation(solicitudId, List.of("A"), List.of(1));

        verify(solicitudRepository, never()).save(any());
    }
}
