package org.trabajott1.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trabajott1.model.ResultsResponse;
import org.trabajott1.persistence.entity.ResultadoEntity;
import org.trabajott1.persistence.entity.SolicitudEntity;
import org.trabajott1.repository.ResultadoRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests extendidos para {@link ResultadosService}.
 * Complementa {@link ResultadosServiceTest} cubriendo el caso en que
 * {@link ResultadoEntity#getDatosResultado()} devuelve {@code null}
 * (simulación guardada sin datos de resultado).
 *
 * @author Lucas, Ana, Clara, Santiago
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
class ResultadosServiceExtendedTest {

    @Mock
    private ResultadoRepository resultadoRepository;

    @InjectMocks
    private ResultadosService resultadosService;

    /**
     * Verifica que cuando el resultado existe y pertenece al usuario pero
     * {@code datosResultado} es {@code null} (simulación aún sin datos),
     * la respuesta es {@code done = true} con {@code data} wrapping null.
     *
     * Esto ocurre si la solicitud se marcó FINALIZADA pero el campo TEXT
     * no fue escrito (p. ej. error parcial del worker).
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void obtenerResultados_NullDatosResultado_ReturnsDoneWithNullData() {
        String username = "user";
        Integer token   = 55555;

        SolicitudEntity solicitud = new SolicitudEntity();
        solicitud.setNombreUsuario(username);
        solicitud.setTokenSolicitud(token);

        ResultadoEntity resultado = new ResultadoEntity();
        resultado.setSolicitud(solicitud);
        resultado.setDatosResultado(null);

        when(resultadoRepository.findBySolicitud_TokenSolicitud(token))
                .thenReturn(Optional.of(resultado));

        ResultsResponse response = resultadosService.obtenerResultados(username, token);

        assertTrue(response.getDone(),
                "done debe ser true aunque datosResultado sea null");
        assertEquals(token, response.getTokenSolicitud());
        // data wrappea null: JsonNullable.of(null) != undefined
        assertNotNull(response.getData(),
                "El campo data no debe ser null (debe ser JsonNullable wrapping null)");
        assertNull(response.getData().orElse(null),
                "El valor dentro de data debe ser null");
    }

    /**
     * Verifica que cuando el resultado existe pero el campo es una cadena vacía
     * (no null), la respuesta sigue siendo {@code done = true} con {@code data}
     * wrapping la cadena vacía.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void obtenerResultados_EmptyDatosResultado_ReturnsDoneWithEmptyData() {
        String username = "user";
        Integer token   = 66666;

        SolicitudEntity solicitud = new SolicitudEntity();
        solicitud.setNombreUsuario(username);
        solicitud.setTokenSolicitud(token);

        ResultadoEntity resultado = new ResultadoEntity();
        resultado.setSolicitud(solicitud);
        resultado.setDatosResultado("");

        when(resultadoRepository.findBySolicitud_TokenSolicitud(token))
                .thenReturn(Optional.of(resultado));

        ResultsResponse response = resultadosService.obtenerResultados(username, token);

        assertTrue(response.getDone());
        assertEquals("", response.getData().orElse(null));
    }
}
