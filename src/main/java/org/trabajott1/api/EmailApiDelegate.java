package org.trabajott1.api;

import jakarta.annotation.Generated;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;
import org.trabajott1.model.EmailResponse;

import java.util.Optional;

/**
 * Delegado para {@link EmailApiController}: contiene la lógica de negocio del endpoint de email.
 * Implementa esta interfaz con una clase anotada con {@link org.springframework.stereotype.Service}
 * para proporcionar la implementación real.
 * Generada automáticamente por OpenAPI Generator.
 *
 * @author Lucas, Ana, Clara, Santiago
 * @version 1.0
 */
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-04-14T15:27:37.393153+02:00[Europe/Madrid]", comments = "Generator version: 7.21.0")
public interface EmailApiDelegate {

    /**
     * Devuelve la petición web nativa actual, si está disponible.
     * Sirve para acceder a las cabeceras HTTP (por ejemplo, el header {@code Accept}).
     *
     * @return un {@link Optional} con la petición actual, vacío si no hay contexto de petición
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * Implementación por defecto del endpoint {@code POST /Email}.
     * Devuelve respuestas de ejemplo según el tipo de contenido solicitado en la cabecera {@code Accept}.
     * Las subclases deben sobreescribir este método para la lógica real.
     *
     * @param emailAddress la dirección de correo del destinatario (opcional)
     * @param message      el contenido del mensaje a enviar (opcional)
     * @return código 201 si el correo se envió correctamente, o 400 si los datos son incorrectos
     * @see EmailApi#emailPost
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    default ResponseEntity<EmailResponse> emailPost(String emailAddress,
                                                    String message) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType : MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"errorMessage\" : \"errorMessage\", \"done\" : true }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("text/json"))) {
                    String exampleString = "Custom MIME type example not yet supported: text/json";
                    ApiUtil.setExampleResponse(request, "text/json", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("text/plain"))) {
                    String exampleString = "Custom MIME type example not yet supported: text/plain";
                    ApiUtil.setExampleResponse(request, "text/plain", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"instance\" : \"instance\", \"detail\" : \"detail\", \"type\" : \"type\", \"title\" : \"title\", \"status\" : 0 }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("text/json"))) {
                    String exampleString = "Custom MIME type example not yet supported: text/json";
                    ApiUtil.setExampleResponse(request, "text/json", exampleString);
                    break;
                }
                if (mediaType.isCompatibleWith(MediaType.valueOf("text/plain"))) {
                    String exampleString = "Custom MIME type example not yet supported: text/plain";
                    ApiUtil.setExampleResponse(request, "text/plain", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}
