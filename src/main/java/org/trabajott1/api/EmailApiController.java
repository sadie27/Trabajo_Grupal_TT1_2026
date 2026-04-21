package org.trabajott1.api;

import jakarta.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-04-14T15:27:37.393153+02:00[Europe/Madrid]", comments = "Generator version: 7.21.0")
@Controller
@RequestMapping("${openapi.servicioConsumible.base-path:}")
public class EmailApiController implements EmailApi {

    private final EmailApiDelegate delegate;

    public EmailApiController(@Autowired(required = false) EmailApiDelegate delegate) {
        this.delegate = Optional.ofNullable(delegate).orElse(new EmailApiDelegate() {
        });
    }

    @Override
    public EmailApiDelegate getDelegate() {
        return delegate;
    }

}
