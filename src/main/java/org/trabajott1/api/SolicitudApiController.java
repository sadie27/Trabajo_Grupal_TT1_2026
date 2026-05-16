package org.trabajott1.api;

import jakarta.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

/**
 * Controlador Spring MVC para los endpoints de gestión de solicitudes de simulación.
 * Recibe las peticiones HTTP y las delega en {@link SolicitudApiDelegate}.
 * Generado automáticamente por OpenAPI Generator.
 *
 * @author Lucas, Ana, Clara, Santiago
 * @version 1.0
 */
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-04-14T15:27:37.393153+02:00[Europe/Madrid]", comments = "Generator version: 7.21.0")
@Controller
@RequestMapping("${openapi.servicioConsumible.base-path:}")
public class SolicitudApiController implements SolicitudApi {

    /** Delegado que contiene la lógica real de los endpoints de solicitud. */
    private final SolicitudApiDelegate delegate;

    /**
     * Crea el controlador inyectando el delegado correspondiente.
     * Si no hay ningún delegado disponible en el contexto, usa la implementación por defecto.
     *
     * @param delegate el delegado que implementa la lógica del endpoint, puede ser {@code null}
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    public SolicitudApiController(@Autowired(required = false) SolicitudApiDelegate delegate) {
        this.delegate = Optional.ofNullable(delegate).orElse(new SolicitudApiDelegate() {
        });
    }

    /**
     * Devuelve el delegado configurado para este controlador.
     *
     * @return el {@link SolicitudApiDelegate} activo
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Override
    public SolicitudApiDelegate getDelegate() {
        return delegate;
    }

}
