package org.trabajott1.persistence.entity;

import jakarta.persistence.*;

/**
 * Entidad que representa una de las especies o tipos de células participantes en una solicitud de simulación.
 */
@Entity
@Table(name = "entidades_solicitud")
public class EntidadSolicitudEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_solicitud", nullable = false)
    private SolicitudEntity solicitud;

    @Column(name = "nombre_entidad", nullable = false)
    private String nombreEntidad;

    @Column(name = "cantidad_inicial", nullable = false)
    private Integer cantidadInicial;

    /**
     * Constructor vacío para JPA.
     */
    public EntidadSolicitudEntity() {
    }

    /**
     * Obtiene el ID.
     * @return El ID.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Establece el ID.
     * @param id El ID a establecer.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Obtiene la solicitud asociada.
     * @return La solicitud asociada.
     */
    public SolicitudEntity getSolicitud() {
        return solicitud;
    }

    /**
     * Establece la solicitud asociada.
     * @param solicitud La solicitud a establecer.
     */
    public void setSolicitud(SolicitudEntity solicitud) {
        this.solicitud = solicitud;
    }

    /**
     * Obtiene el nombre de la entidad.
     * @return El nombre de la entidad.
     */
    public String getNombreEntidad() {
        return nombreEntidad;
    }

    /**
     * Establece el nombre de la entidad.
     * @param nombreEntidad El nombre a establecer.
     */
    public void setNombreEntidad(String nombreEntidad) {
        this.nombreEntidad = nombreEntidad;
    }

    /**
     * Obtiene la cantidad inicial.
     * @return La cantidad inicial.
     */
    public Integer getCantidadInicial() {
        return cantidadInicial;
    }

    /**
     * Establece la cantidad inicial.
     * @param cantidadInicial La cantidad a establecer.
     */
    public void setCantidadInicial(Integer cantidadInicial) {
        this.cantidadInicial = cantidadInicial;
    }
}
