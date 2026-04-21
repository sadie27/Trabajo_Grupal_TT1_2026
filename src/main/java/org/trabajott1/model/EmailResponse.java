package org.trabajott1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.lang.Nullable;

import java.util.Arrays;
import java.util.Objects;

/**
 * EmailResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-04-14T15:27:37.393153+02:00[Europe/Madrid]", comments = "Generator version: 7.21.0")
public class EmailResponse {

    private @Nullable Boolean done;

    private JsonNullable<String> errorMessage = JsonNullable.<String>undefined();

    private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
        return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
    }

    private static <T> int hashCodeNullable(JsonNullable<T> a) {
        if (a == null) {
            return 1;
        }
        return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
    }

    public EmailResponse done(@Nullable Boolean done) {
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

    public EmailResponse errorMessage(String errorMessage) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EmailResponse emailResponse = (EmailResponse) o;
        return Objects.equals(this.done, emailResponse.done) &&
                equalsNullable(this.errorMessage, emailResponse.errorMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(done, hashCodeNullable(errorMessage));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class EmailResponse {\n");
        sb.append("    done: ").append(toIndentedString(done)).append("\n");
        sb.append("    errorMessage: ").append(toIndentedString(errorMessage)).append("\n");
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

