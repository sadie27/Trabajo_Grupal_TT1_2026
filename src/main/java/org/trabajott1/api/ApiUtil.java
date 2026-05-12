package org.trabajott1.api;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.request.NativeWebRequest;

import java.io.IOException;

/**
 * Utilidades para el manejo de respuestas en la API.
 */
public class ApiUtil {
    /**
     * Establece una respuesta de ejemplo en el objeto {@link HttpServletResponse}.
     *
     * @param req La solicitud web nativa.
     * @param contentType El tipo de contenido de la respuesta (por ejemplo, "application/json").
     * @param example El contenido de ejemplo que se escribirá en la respuesta.
     * @throws RuntimeException Si ocurre un error de E/S al escribir en la respuesta.
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
