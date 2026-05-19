package org.trabajott1.api;

import jakarta.annotation.Generated;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;
import org.trabajott1.model.Solicitud;
import org.trabajott1.model.SolicitudResponse;

import java.util.List;
import java.util.Optional;

/**
 * Delegado para {@link SolicitudApiController}: contiene la lógica de negocio de los endpoints de solicitud.
 * Implementa esta interfaz con una clase anotada con {@link org.springframework.stereotype.Service}
 * para proporcionar la implementación real.
 * Generada automáticamente por OpenAPI Generator.
 *
 * @author Lucas, Ana, Clara, Santiago
 * @version 1.0
 */
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-04-14T15:27:37.393153+02:00[Europe/Madrid]", comments = "Generator version: 7.21.0")
public interface SolicitudApiDelegate {

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
     * Implementación por defecto del endpoint {@code GET /Solicitud/ComprobarSolicitud}.
     * Devuelve respuestas de ejemplo según el tipo de contenido aceptado.
     * Las subclases deben sobreescribir este método para la lógica real.
     *
     * @param nombreUsuario el nombre del usuario propietario de la solicitud (opcional)
     * @param tok           el token numérico que identifica la solicitud (opcional)
     * @return lista de enteros indicando el estado: [1,0] finalizada, [0,1] en proceso, [0,0] no encontrada
     * @see SolicitudApi#solicitudComprobarSolicitudGet
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    default ResponseEntity<List<Integer>> solicitudComprobarSolicitudGet(String nombreUsuario,
                                                                         Integer tok) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType : MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "[ 0, 0 ]";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
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

    /**
     * Implementación por defecto del endpoint {@code GET /Solicitud/GetSolicitudesUsuario}.
     * Devuelve respuestas de ejemplo según el tipo de contenido aceptado.
     * Las subclases deben sobreescribir este método para la lógica real.
     *
     * @param nombreUsuario el nombre del usuario del que se quieren listar las solicitudes (opcional)
     * @return lista de tokens de todas las solicitudes del usuario
     * @see SolicitudApi#solicitudGetSolicitudesUsuarioGet
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    default ResponseEntity<List<Integer>> solicitudGetSolicitudesUsuarioGet(String nombreUsuario) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType : MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "[ 0, 0 ]";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
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

    /**
     * Implementación por defecto del endpoint {@code POST /Solicitud/Solicitar}.
     * Devuelve respuestas de ejemplo según el tipo de contenido aceptado.
     * Las subclases deben sobreescribir este método para la lógica real.
     *
     * @param nombreUsuario el nombre del usuario que realiza la solicitud (opcional)
     * @param solicitud     los datos de la simulación: nombres de entidades y cantidades iniciales (opcional)
     * @return respuesta con el token asignado a la nueva solicitud, o error si los datos son incorrectos
     * @see SolicitudApi#solicitudSolicitarPost
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    default ResponseEntity<SolicitudResponse> solicitudSolicitarPost(String nombreUsuario,
                                                                     Solicitud solicitud) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType : MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"data\" : true, \"tokenSolicitud\" : 0, \"errorMessage\" : \"errorMessage\", \"done\" : true }";
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
