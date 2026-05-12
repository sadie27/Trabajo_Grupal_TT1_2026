package org.trabajott1.persistence.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa una solicitud de simulación en la base de datos.
 */
@Entity
@Table(name = "solicitudes")
public class SolicitudEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_solicitud")
    private Integer idSolicitud;

    @Column(name = "nombre_usuario", nullable = false)
    private String nombreUsuario;

    @Column(name = "token_solicitud", unique = true, nullable = false)
    private Integer tokenSolicitud;

    @Column(name = "estado", nullable = false, length = 50)
    private String estado = "PENDIENTE";

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @OneToMany(mappedBy = "solicitud", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<EntidadSolicitudEntity> entidades = new ArrayList<>();

    @OneToOne(mappedBy = "solicitud", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private ResultadoEntity resultado;

    /**
     * Constructor vacío para JPA.
     */
    public SolicitudEntity() {
    }

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }

    /**
     * Obtiene el ID de la solicitud.
     * @return El ID de la solicitud.
     */
    public Integer getIdSolicitud() {
        return idSolicitud;
    }

    /**
     * Establece el ID de la solicitud.
     * @param idSolicitud El ID a establecer.
     */
    public void setIdSolicitud(Integer idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    /**
     * Obtiene el nombre del usuario.
     * @return El nombre del usuario.
     */
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    /**
     * Establece el nombre del usuario.
     * @param nombreUsuario El nombre a establecer.
     */
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    /**
     * Obtiene el token de la solicitud.
     * @return El token de la solicitud.
     */
    public Integer getTokenSolicitud() {
        return tokenSolicitud;
    }

    /**
     * Establece el token de la solicitud.
     * @param tokenSolicitud El token a establecer.
     */
    public void setTokenSolicitud(Integer tokenSolicitud) {
        this.tokenSolicitud = tokenSolicitud;
    }

    /**
     * Obtiene el estado de la solicitud.
     * @return El estado de la solicitud.
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Establece el estado de la solicitud.
     * @param estado El estado a establecer.
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * Obtiene la fecha de creación.
     * @return La fecha de creación.
     */
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    /**
     * Establece la fecha de creación.
     * @param fechaCreacion La fecha a establecer.
     */
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    /**
     * Obtiene la fecha de actualización.
     * @return La fecha de actualización.
     */
    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    /**
     * Establece la fecha de actualización.
     * @param fechaActualizacion La fecha a establecer.
     */
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    /**
     * Obtiene la lista de entidades asociadas.
     * @return La lista de entidades.
     */
    public List<EntidadSolicitudEntity> getEntidades() {
        return entidades;
    }

    /**
     * Establece la lista de entidades asociadas.
     * @param entidades La lista de entidades a establecer.
     */
    public void setEntidades(List<EntidadSolicitudEntity> entidades) {
        this.entidades = entidades;
    }

    /**
     * Añade una entidad a la solicitud y establece la relación bidireccional.
     * @param entidad La entidad a añadir.
     */
    public void addEntidad(EntidadSolicitudEntity entidad) {
        entidades.add(entidad);
        entidad.setSolicitud(this);
    }

    /**
     * Obtiene el resultado asociado.
     * @return El resultado asociado.
     */
    public ResultadoEntity getResultado() {
        return resultado;
    }

    /**
     * Establece el resultado asociado.
     * @param resultado El resultado a establecer.
     */
    public void setResultado(ResultadoEntity resultado) {
        this.resultado = resultado;
    }

    /**
     * Helper para añadir entidades desde listas de la API.
     * @param nombres    Lista de nombres de las entidades.
     * @param cantidades Lista de cantidades iniciales correspondientes.
     */
    public void addEntidades(List<String> nombres, List<Integer> cantidades) {
        if (nombres == null || cantidades == null || nombres.size() != cantidades.size()) {
            return;
        }
        for (int i = 0; i < nombres.size(); i++) {
            EntidadSolicitudEntity entidad = new EntidadSolicitudEntity();
            entidad.setNombreEntidad(nombres.get(i));
            entidad.setCantidadInicial(cantidades.get(i));
            this.addEntidad(entidad);
        }
    }
}
