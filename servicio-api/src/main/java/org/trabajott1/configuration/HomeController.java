package org.trabajott1.configuration;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controlador de conveniencia que redirige la ruta raíz hacia la interfaz de Swagger UI.
 * Permite acceder fácilmente a la documentación interactiva de la API al entrar en la URL base.
 *
 * @author Lucas, Ana, Clara, Santiago
 * @version 1.0
 */
@Controller
public class HomeController {

    /**
     * Redirige las peticiones a la ruta raíz ({@code "/"}) hacia la página de Swagger UI.
     *
     * @return cadena de redirección hacia {@code swagger-ui.html}
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @RequestMapping("/")
    public String index() {
        return "redirect:swagger-ui.html";
    }

}