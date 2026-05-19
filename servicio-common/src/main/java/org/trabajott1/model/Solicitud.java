package org.trabajott1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Representa los datos de entrada para una solicitud de simulación de vida artificial.
 * Contiene la lista de nombres de entidades (especies) y sus cantidades iniciales.
 * Se deserializa de forma personalizada para soportar tanto el formato directo
 * como el formato envuelto en un objeto "solicitud".
 *
 * @author Lucas, Ana, Clara, Santiago
 * @version 1.0
 */
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-04-14T15:27:37.393153+02:00[Europe/Madrid]", comments = "Generator version: 7.21.0")
@JsonDeserialize(using = SolicitudDeserializer.class)
public class Solicitud {

    /** Lista de cantidades iniciales de cada entidad, en el mismo orden que {@code nombreEntidades}. */
    @Valid
    private JsonNullable<List<Integer>> cantidadesIniciales = JsonNullable.<List<Integer>>undefined();

    /** Lista de nombres de las entidades (especies) que participarán en la simulación. */
    @Valid
    private JsonNullable<List<String>> nombreEntidades = JsonNullable.<List<String>>undefined();

    private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
        return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
    }

    private static <T> int hashCodeNullable(JsonNullable<T> a) {
        if (a == null) {
            return 1;
        }
        return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
    }

    /**
     * Establece la lista de cantidades iniciales usando el patrón builder.
     *
     * @param cantidadesIniciales lista con la cantidad inicial de cada entidad
     * @return esta misma instancia para encadenamiento de llamadas
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    public Solicitud cantidadesIniciales(List<Integer> cantidadesIniciales) {
        this.cantidadesIniciales = JsonNullable.of(cantidadesIniciales);
        return this;
    }

    /**
     * Añade un elemento a la lista de cantidades iniciales.
     * Si la lista no existe aún, la crea.
     *
     * @param cantidadesInicialesItem la cantidad inicial a añadir
     * @return esta misma instancia para encadenamiento de llamadas
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    public Solicitud addCantidadesInicialesItem(Integer cantidadesInicialesItem) {
        if (this.cantidadesIniciales == null || !this.cantidadesIniciales.isPresent()) {
            this.cantidadesIniciales = JsonNullable.of(new ArrayList<>());
        }
        this.cantidadesIniciales.get().add(cantidadesInicialesItem);
        return this;
    }

    /**
     * Devuelve la lista de cantidades iniciales de cada entidad.
     *
     * @return lista de cantidades iniciales envuelta en {@link JsonNullable}
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */

    @Schema(name = "cantidadesIniciales", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("cantidadesIniciales")
    public JsonNullable<List<Integer>> getCantidadesIniciales() {
        return cantidadesIniciales;
    }

    public void setCantidadesIniciales(JsonNullable<List<Integer>> cantidadesIniciales) {
        this.cantidadesIniciales = cantidadesIniciales;
    }

    /**
     * Establece la lista de nombres de entidades usando el patrón builder.
     *
     * @param nombreEntidades lista con los nombres de las entidades participantes
     * @return esta misma instancia para encadenamiento de llamadas
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    public Solicitud nombreEntidades(List<String> nombreEntidades) {
        this.nombreEntidades = JsonNullable.of(nombreEntidades);
        return this;
    }

    /**
     * Añade un elemento a la lista de nombres de entidades.
     * Si la lista no existe aún, la crea.
     *
     * @param nombreEntidadesItem el nombre de la entidad a añadir
     * @return esta misma instancia para encadenamiento de llamadas
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    public Solicitud addNombreEntidadesItem(String nombreEntidadesItem) {
        if (this.nombreEntidades == null || !this.nombreEntidades.isPresent()) {
            this.nombreEntidades = JsonNullable.of(new ArrayList<>());
        }
        this.nombreEntidades.get().add(nombreEntidadesItem);
        return this;
    }

    /**
     * Devuelve la lista de nombres de entidades de la solicitud.
     *
     * @return lista de nombres de entidades envuelta en {@link JsonNullable}
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */

    @Schema(name = "nombreEntidades", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("nombreEntidades")
    public JsonNullable<List<String>> getNombreEntidades() {
        return nombreEntidades;
    }

    public void setNombreEntidades(JsonNullable<List<String>> nombreEntidades) {
        this.nombreEntidades = nombreEntidades;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Solicitud solicitud = (Solicitud) o;
        return equalsNullable(this.cantidadesIniciales, solicitud.cantidadesIniciales) &&
                equalsNullable(this.nombreEntidades, solicitud.nombreEntidades);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hashCodeNullable(cantidadesIniciales), hashCodeNullable(nombreEntidades));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Solicitud {\n");
        sb.append("    cantidadesIniciales: ").append(toIndentedString(cantidadesIniciales)).append("\n");
        sb.append("    nombreEntidades: ").append(toIndentedString(nombreEntidades)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(@Nullable Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }
}

