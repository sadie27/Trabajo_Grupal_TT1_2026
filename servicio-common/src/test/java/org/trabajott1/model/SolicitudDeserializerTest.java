package org.trabajott1.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullableModule;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para {@link SolicitudDeserializer}.
 * Verifica el parseo de JSON en formato directo, envuelto en "solicitud",
 * con campos omitidos y con JSON malformado.
 *
 * @author Lucas, Ana, Clara, Santiago
 * @version 1.0
 */
class SolicitudDeserializerTest {

    private ObjectMapper mapper;

    /**
     * Configura el {@link ObjectMapper} con el módulo {@link JsonNullableModule}
     * y el deserializador personalizado registrado en {@link Solicitud}.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JsonNullableModule());
    }

    /**
     * Verifica que el formato directo (sin clave "solicitud") se deserializa correctamente.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void deserialize_DirectFormat_ParsesBothFields() throws Exception {
        String json = """
                {
                  "cantidadesIniciales": [10, 20, 5],
                  "nombreEntidades": ["Rojo", "Verde", "Azul"]
                }
                """;

        Solicitud result = mapper.readValue(json, Solicitud.class);

        assertTrue(result.getCantidadesIniciales().isPresent());
        assertEquals(List.of(10, 20, 5), result.getCantidadesIniciales().get());
        assertTrue(result.getNombreEntidades().isPresent());
        assertEquals(List.of("Rojo", "Verde", "Azul"), result.getNombreEntidades().get());
    }

    /**
     * Verifica que el formato envuelto (con clave "solicitud") se deserializa correctamente.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void deserialize_WrappedFormat_ParsesBothFields() throws Exception {
        String json = """
                {
                  "solicitud": {
                    "cantidadesIniciales": [3, 7],
                    "nombreEntidades": ["Alfa", "Beta"]
                  }
                }
                """;

        Solicitud result = mapper.readValue(json, Solicitud.class);

        assertTrue(result.getCantidadesIniciales().isPresent());
        assertEquals(List.of(3, 7), result.getCantidadesIniciales().get());
        assertTrue(result.getNombreEntidades().isPresent());
        assertEquals(List.of("Alfa", "Beta"), result.getNombreEntidades().get());
    }

    /**
     * Verifica que cuando falta el campo "nombreEntidades" el campo queda como undefined
     * y "cantidadesIniciales" se parsea sin error.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void deserialize_MissingNombreEntidades_FieldRemainsUndefined() throws Exception {
        String json = """
                {
                  "cantidadesIniciales": [1, 2]
                }
                """;

        Solicitud result = mapper.readValue(json, Solicitud.class);

        assertTrue(result.getCantidadesIniciales().isPresent());
        assertEquals(List.of(1, 2), result.getCantidadesIniciales().get());
        assertFalse(result.getNombreEntidades().isPresent());
    }

    /**
     * Verifica que un JSON malformado lanza una excepción de Jackson durante la deserialización.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void deserialize_MalformedJson_ThrowsException() {
        String json = "{ cantidadesIniciales: [1, 2] INVALID }";

        assertThrows(Exception.class, () -> mapper.readValue(json, Solicitud.class));
    }
}
