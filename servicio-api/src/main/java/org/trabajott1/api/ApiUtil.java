package org.trabajott1.api;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.request.NativeWebRequest;

import java.io.IOException;

/**
 * Clase de utilidades para el manejo de respuestas de ejemplo en la API.
 * Se usa principalmente en las implementaciones por defecto de los delegados
 * generados por OpenAPI para devolver respuestas de prueba según el tipo de contenido aceptado.
 *
 * @author Lucas, Ana, Clara, Santiago
 * @version 1.0
 */
public class ApiUtil {

    /**
     * Escribe una respuesta de ejemplo directamente en el {@link HttpServletResponse} de la petición actual.
     * Configura la codificación a UTF-8, añade la cabecera {@code Content-Type} y escribe el contenido de ejemplo.
     *
     * @param req         la solicitud web nativa de la que se obtiene la respuesta HTTP subyacente
     * @param contentType el tipo de contenido de la respuesta (por ejemplo, {@code "application/json"})
     * @param example     el texto de ejemplo que se escribirá en el cuerpo de la respuesta
     * @throws RuntimeException si ocurre un error de E/S al escribir en la respuesta
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    public static void setExampleResponse(NativeWebRequest req, String contentType, String example) {
        try {
            HttpServletResponse res = req.getNativeResponse(HttpServletResponse.class);
            if (res != null) {
                res.setCharacterEncoding("UTF-8");
                res.addHeader("Content-Type", contentType);
                res.getWriter().print(example);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
