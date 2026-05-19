package org.trabajott1.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trabajott1.model.SimulationMessage;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests extendidos para {@link SimulationListener}.
 * Complementa {@link SimulationListenerTest} (que cubre delegación y excepción genérica)
 * añadiendo: mensaje null, interrupción del hilo durante Thread.sleep, e
 * {@link IllegalArgumentException} del servicio.
 *
 * @author Lucas, Ana, Clara, Santiago
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
class SimulationListenerExtendedTest {

    @Mock
    private ISimulationService simulationService;

    @InjectMocks
    private SimulationListener simulationListener;

    // ── Mensaje null ──────────────────────────────────────────────────────────

    /**
     * Verifica que pasar un mensaje null lanza {@link NullPointerException} antes de
     * llegar al servicio, dado que el listener accede a message.getSolicitudId()
     * sin null-check previo.
     * Este test documenta el comportamiento actual para que cualquier cambio futuro
     * que añada null-check sea detectado.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void receiveMessage_NullMessage_ThrowsNullPointerBeforeService() {
        assertThrows(NullPointerException.class,
                () -> simulationListener.receiveMessage(null));

        verifyNoInteractions(simulationService);
    }

    // ── Interrupción del hilo ─────────────────────────────────────────────────

    /**
     * Verifica que cuando el hilo es interrumpido durante el Thread.sleep de 5 s,
     * el flag de interrupción queda restaurado (Thread.currentThread().interrupt())
     * y la simulación se ejecuta igualmente.
     *
     * La estrategia: ejecutar receiveMessage en un hilo separado, interrumpirlo
     * justo después de arrancar (antes de que termine el sleep) y comprobar que:
     * 1) el flag interrupted queda a true al salir, y
     * 2) el servicio fue invocado de todos modos.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void receiveMessage_ThreadInterrupted_InterruptFlagRestoredAndServiceStillCalled()
            throws InterruptedException {

        SimulationMessage message = new SimulationMessage(1, List.of("A"), List.of(5));

        AtomicReference<Boolean> interruptedAfter = new AtomicReference<>(false);

        Thread worker = new Thread(() -> {
            simulationListener.receiveMessage(message);
            interruptedAfter.set(Thread.currentThread().isInterrupted());
        });

        worker.start();
        // Interrumpir en cuanto el hilo arranque, antes de que acabe el sleep(5000)
        worker.interrupt();
        worker.join(10_000);

        assertTrue(interruptedAfter.get(),
                "El flag interrupted debe quedar restaurado tras la InterruptedException");
        verify(simulationService).executeSimulation(1, List.of("A"), List.of(5));
    }

    // ── IllegalArgumentException del servicio ────────────────────────────────

    /**
     * Verifica que cuando el servicio lanza {@link IllegalArgumentException}
     * (p. ej. número de entidades inválido), el listener la captura sin propagarla.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void receiveMessage_ServiceThrowsIllegalArgument_ExceptionContained() {
        SimulationMessage message = new SimulationMessage(2, List.of("A"), List.of(1));
        doThrow(new IllegalArgumentException("Entidades fuera de rango"))
                .when(simulationService).executeSimulation(anyInt(), anyList(), anyList());

        assertDoesNotThrow(() -> simulationListener.receiveMessage(message));

        verify(simulationService).executeSimulation(anyInt(), anyList(), anyList());
    }

    // ── Delegación correcta de campos del mensaje ─────────────────────────────

    /**
     * Verifica que los tres campos del mensaje (solicitudId, entityNames, initialQuantities)
     * se pasan al servicio exactamente como llegan, sin transformación.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void receiveMessage_CorrectFieldsDelegatedToService() {
        List<String> names = List.of("Rojo", "Azul", "Verde");
        List<Integer> qtys  = List.of(10, 20, 5);
        SimulationMessage message = new SimulationMessage(42, names, qtys);

        simulationListener.receiveMessage(message);

        verify(simulationService).executeSimulation(42, names, qtys);
    }
}
