package org.trabajott1.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.openapitools.jackson.nullable.JsonNullable;

import java.io.IOException;
import java.util.List;

/**
 * Deserializador personalizado para el modelo {@link Solicitud}.
 * Permite parsear tanto el formato directo ({@code {"cantidadesIniciales": [...], ...}})
 * como el formato envuelto ({@code {"solicitud": {"cantidadesIniciales": [...], ...}}})
 * que puede enviar el cliente.
 *
 * @author Lucas, Ana, Clara, Santiago
 * @version 1.0
 */
public class SolicitudDeserializer extends StdDeserializer<Solicitud> {

    /** Referencia de tipo para deserializar listas de enteros. */
    private static final TypeReference<List<Integer>> INTEGER_LIST = new TypeReference<>() {};

    /** Referencia de tipo para deserializar listas de cadenas de texto. */
    private static final TypeReference<List<String>> STRING_LIST = new TypeReference<>() {};

    /**
     * Crea el deserializador indicando que maneja objetos de tipo {@link Solicitud}.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    public SolicitudDeserializer() {
        super(Solicitud.class);
    }

    /**
     * Deserializa el JSON de entrada en un objeto {@link Solicitud}.
     * Si el JSON tiene una clave "solicitud", extrae el objeto anidado antes de parsear.
     *
     * @param p   el parser JSON que lee el flujo de entrada
     * @param ctx el contexto de deserialización de Jackson
     * @return un objeto {@link Solicitud} con los datos parseados
     * @throws IOException si ocurre un error al leer el JSON
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Override
    public Solicitud deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        JsonNode root = mapper.readTree(p);

        JsonNode node = root.has("solicitud") ? root.get("solicitud") : root;

        Solicitud solicitud = new Solicitud();

        if (node.has("cantidadesIniciales") && !node.get("cantidadesIniciales").isNull()) {
            solicitud.setCantidadesIniciales(
                    JsonNullable.of(mapper.convertValue(node.get("cantidadesIniciales"), INTEGER_LIST)));
        }

        if (node.has("nombreEntidades") && !node.get("nombreEntidades").isNull()) {
            solicitud.setNombreEntidades(
                    JsonNullable.of(mapper.convertValue(node.get("nombreEntidades"), STRING_LIST)));
        }

        return solicitud;
    }
}
