package org.trabajott1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.lang.Nullable;

import java.util.Arrays;
import java.util.Objects;

/**
 * ResultsResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-04-14T15:27:37.393153+02:00[Europe/Madrid]", comments = "Generator version: 7.21.0")
public class ResultsResponse {

    private @Nullable Boolean done;

    private @Nullable Integer tokenSolicitud;

    private JsonNullable<String> errorMessage = JsonNullable.<String>undefined();

    private JsonNullable<String> data = JsonNullable.<String>undefined();

    private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
        return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
    }

    private static <T> int hashCodeNullable(JsonNullable<T> a) {
        if (a == null) {
            return 1;
        }
        return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
    }

    public ResultsResponse done(@Nullable Boolean done) {
        this.done = done;
        return this;
    }

    /**
     * Get done
     *
     * @return done
     */

    @Schema(name = "done", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("done")
    public @Nullable Boolean getDone() {
        return done;
    }

    @JsonProperty("done")
    public void setDone(@Nullable Boolean done) {
        this.done = done;
    }

    public ResultsResponse tokenSolicitud(@Nullable Integer tokenSolicitud) {
        this.tokenSolicitud = tokenSolicitud;
        return this;
    }

    /**
     * Get tokenSolicitud
     *
     * @return tokenSolicitud
     */

    @Schema(name = "tokenSolicitud", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("tokenSolicitud")
    public @Nullable Integer getTokenSolicitud() {
        return tokenSolicitud;
    }

    @JsonProperty("tokenSolicitud")
    public void setTokenSolicitud(@Nullable Integer tokenSolicitud) {
        this.tokenSolicitud = tokenSolicitud;
    }

    public ResultsResponse errorMessage(String errorMessage) {
        this.errorMessage = JsonNullable.of(errorMessage);
        return this;
    }

    /**
     * Get errorMessage
     *
     * @return errorMessage
     */

    @Schema(name = "errorMessage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("errorMessage")
    public JsonNullable<String> getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(JsonNullable<String> errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ResultsResponse data(String data) {
        this.data = JsonNullable.of(data);
        return this;
    }

    /**
     * Get data
     *
     * @return data
     */

    @Schema(name = "data", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("data")
    public JsonNullable<String> getData() {
        return data;
    }

    public void setData(JsonNullable<String> data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ResultsResponse resultsResponse = (ResultsResponse) o;
        return Objects.equals(this.done, resultsResponse.done) &&
                Objects.equals(this.tokenSolicitud, resultsResponse.tokenSolicitud) &&
                equalsNullable(this.errorMessage, resultsResponse.errorMessage) &&
                equalsNullable(this.data, resultsResponse.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(done, tokenSolicitud, hashCodeNullable(errorMessage), hashCodeNullable(data));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ResultsResponse {\n");
        sb.append("    done: ").append(toIndentedString(done)).append("\n");
        sb.append("    tokenSolicitud: ").append(toIndentedString(tokenSolicitud)).append("\n");
        sb.append("    errorMessage: ").append(toIndentedString(errorMessage)).append("\n");
        sb.append("    data: ").append(toIndentedString(data)).append("\n");
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

