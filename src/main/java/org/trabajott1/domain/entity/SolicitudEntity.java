package org.trabajott1.domain.entity;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidad de dominio para Solicitud.
 * Representa una solicitud de simulación en el sistema.
 */
public class SolicitudEntity {

    private Integer token;
    private String nombreUsuario;
    private List<Integer> cantidadesIniciales;
    private List<String> nombreEntidades;
    private String estado; 
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    public SolicitudEntity() {
        this.fechaCreacion = LocalDateTime.now();
        this.estado = "PENDIENTE";
    }

    public Integer getToken() { return token; }
    public void setToken(Integer token) { this.token = token; }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public List<Integer> getCantidadesIniciales() { return cantidadesIniciales; }
    public void setCantidadesIniciales(List<Integer> cantidadesIniciales) {
        this.cantidadesIniciales = cantidadesIniciales;
    }

    public List<String> getNombreEntidades() { return nombreEntidades; }
    public void setNombreEntidades(List<String> nombreEntidades) {
        this.nombreEntidades = nombreEntidades;
    }

    public String getEstado() { return estado; }
    public void setEstado(String estado) {
        this.fechaActualizacion = LocalDateTime.now();
        this.estado = estado;
    }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
}