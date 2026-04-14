package org.openapitools.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.lang.Nullable;
import java.util.NoSuchElementException;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Solicitud
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-04-14T15:27:37.393153+02:00[Europe/Madrid]", comments = "Generator version: 7.21.0")
public class Solicitud {

  @Valid
  private JsonNullable<List<Integer>> cantidadesIniciales = JsonNullable.<List<Integer>>undefined();

  @Valid
  private JsonNullable<List<String>> nombreEntidades = JsonNullable.<List<String>>undefined();

  public Solicitud cantidadesIniciales(List<Integer> cantidadesIniciales) {
    this.cantidadesIniciales = JsonNullable.of(cantidadesIniciales);
    return this;
  }

  public Solicitud addCantidadesInicialesItem(Integer cantidadesInicialesItem) {
    if (this.cantidadesIniciales == null || !this.cantidadesIniciales.isPresent()) {
      this.cantidadesIniciales = JsonNullable.of(new ArrayList<>());
    }
    this.cantidadesIniciales.get().add(cantidadesInicialesItem);
    return this;
  }

  /**
   * Get cantidadesIniciales
   * @return cantidadesIniciales
   */
  
  @Schema(name = "cantidadesIniciales", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cantidadesIniciales")
  public JsonNullable<List<Integer>> getCantidadesIniciales() {
    return cantidadesIniciales;
  }

  public void setCantidadesIniciales(JsonNullable<List<Integer>> cantidadesIniciales) {
    this.cantidadesIniciales = cantidadesIniciales;
  }

  public Solicitud nombreEntidades(List<String> nombreEntidades) {
    this.nombreEntidades = JsonNullable.of(nombreEntidades);
    return this;
  }

  public Solicitud addNombreEntidadesItem(String nombreEntidadesItem) {
    if (this.nombreEntidades == null || !this.nombreEntidades.isPresent()) {
      this.nombreEntidades = JsonNullable.of(new ArrayList<>());
    }
    this.nombreEntidades.get().add(nombreEntidadesItem);
    return this;
  }

  /**
   * Get nombreEntidades
   * @return nombreEntidades
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

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(hashCodeNullable(cantidadesIniciales), hashCodeNullable(nombreEntidades));
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
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

