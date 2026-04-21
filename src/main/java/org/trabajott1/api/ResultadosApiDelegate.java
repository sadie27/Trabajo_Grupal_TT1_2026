package org.trabajott1.api;

import jakarta.annotation.Generated;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;
import org.trabajott1.model.ResultsResponse;

import java.util.Optional;

/**
 * A delegate to be called by the {@link ResultadosApiController}}.
 * Implement this interface with a {@link org.springframework.stereotype.Service} annotated class.
 */
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-04-14T15:27:37.393153+02:00[Europe/Madrid]", comments = "Generator version: 7.21.0")
public interface ResultadosApiDelegate {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * POST /Resultados
     *
     * @param nombreUsuario (optional)
     * @param tok           (optional)
     * @return Created (status code 201)
     * or Bad Request (status code 400)
     * @see ResultadosApi#resultadosPost
     */
    default ResponseEntity<ResultsResponse> resultadosPost(String nombreUsuario,
                                                           Integer tok) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType : MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"data\" : \"data\", \"tokenSolicitud\" : 0, \"errorMessage\" : \"errorMessage\", \"done\" : true }";
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
