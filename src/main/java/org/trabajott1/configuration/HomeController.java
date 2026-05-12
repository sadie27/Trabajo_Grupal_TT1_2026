package org.trabajott1.configuration;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controlador para redirigir la ruta raíz a la documentación de la API (Swagger UI).
 */
@Controller
public class HomeController {

    /**
     * Redirige las peticiones a la raíz ("/") hacia swagger-ui.html.
     *
     * @return Una cadena de redirección a la página de Swagger UI.
     */
    @RequestMapping("/")
    public String index() {
        return "redirect:swagger-ui.html";
    }

}