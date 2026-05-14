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

public class SolicitudDeserializer extends StdDeserializer<Solicitud> {

    private static final TypeReference<List<Integer>> INTEGER_LIST = new TypeReference<>() {};
    private static final TypeReference<List<String>> STRING_LIST = new TypeReference<>() {};

    public SolicitudDeserializer() {
        super(Solicitud.class);
    }

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
