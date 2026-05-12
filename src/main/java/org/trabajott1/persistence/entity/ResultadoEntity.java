package org.trabajott1.persistence.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Entidad que representa el resultado de una simulación procesada.
 */
@Entity
@Table(name = "resultados")
public class ResultadoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_resultado")
    private Integer idResultado;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_solicitud", nullable = false, unique = true)
    private SolicitudEntity solicitud;

    @Column(name = "datos_resultado", columnDefinition = "TEXT")
    private String datosResultado;

    @Column(name = "fecha_procesamiento")
    private LocalDateTime fechaProcesamiento;

    /**
     * Constructor vacío para JPA.
     */
    public ResultadoEntity() {
    }

    @PrePersist
    protected void onCreate() {
        fechaProcesamiento = LocalDateTime.now();
    }

    /**
     * Obtiene el ID del resultado.
     * @return El ID del resultado.
     */
    public Integer getIdResultado() {
        return idResultado;
    }

    /**
     * Establece el ID del resultado.
     * @param idResultado El ID a establecer.
     */
    public void setIdResultado(Integer idResultado) {
        this.idResultado = idResultado;
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
     * Obtiene los datos del resultado.
     * @return Los datos del resultado.
     */
    public String getDatosResultado() {
        return datosResultado;
    }

    /**
     * Establece los datos del resultado.
     * @param datosResultado Los datos a establecer.
     */
    public void setDatosResultado(String datosResultado) {
        this.datosResultado = datosResultado;
    }

    /**
     * Obtiene la fecha de procesamiento.
     * @return La fecha de procesamiento.
     */
    public LocalDateTime getFechaProcesamiento() {
        return fechaProcesamiento;
    }

    /**
     * Establece la fecha de procesamiento.
     * @param fechaProcesamiento La fecha a establecer.
     */
    public void setFechaProcesamiento(LocalDateTime fechaProcesamiento) {
        this.fechaProcesamiento = fechaProcesamiento;
    }
}
