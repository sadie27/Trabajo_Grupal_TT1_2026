package org.trabajott1.service;

import org.springframework.stereotype.Service;
import org.trabajott1.model.Solicitud;
import org.trabajott1.model.SolicitudResponse;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class SolicitudService {

    public List<Integer> comprobarSolicitud(String nombreUsuario, Integer tok) {
        // validaciones
        if (nombreUsuario == null || nombreUsuario.isEmpty()) {
            throw new IllegalArgumentException("nombreUsuario es obligatorio");
        }

        if (tok == null) {
            throw new IllegalArgumentException("tok es obligatorio");
        }

        // lógica real

        return Arrays.asList(1, 0);
    }

    public List<Integer> getSolicitudesUsuario(String nombreUsuario) {
        // validaciones
        if (nombreUsuario == null || nombreUsuario.isEmpty()) {
            throw new IllegalArgumentException("nombreUsuario es obligatorio");
        }

        // lógica real

        return Arrays.asList(101, 102, 103);
    }

    public SolicitudResponse crearSolicitud(String nombreUsuario, Solicitud solicitud) {
        // validaciones
        if (nombreUsuario == null || nombreUsuario.isEmpty()) {
            throw new IllegalArgumentException("nombreUsuario es obligatorio");
        }

        if (solicitud == null) {
            throw new IllegalArgumentException("solicitud es obligatoria");
        }

        // lógica real

        int tokenGenerado = new Random().nextInt(10000);

        return new SolicitudResponse()
                .done(true)
                .tokenSolicitud(tokenGenerado)
                .data(true)
                .errorMessage(null);
    }
}
