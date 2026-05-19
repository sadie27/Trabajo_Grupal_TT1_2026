package org.trabajott1.persistence.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Entidad JPA que almacena el resultado de una simulación ya procesada.
 * Cada resultado está vinculado de forma única a una solicitud y contiene
 * los datos de la simulación en texto y la fecha en que fue procesada.
 *
 * @author Lucas, Ana, Clara, Santiago
 * @version 1.0
 */
@Entity
@Table(name = "resultados")
public class ResultadoEntity {

    /** Identificador único del resultado en la base de datos. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_resultado")
    private Integer idResultado;

    /** Solicitud de simulación a la que corresponde este resultado. */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_solicitud", nullable = false, unique = true)
    private SolicitudEntity solicitud;

    /** Datos de la simulación en formato de texto (series temporales del grid). */
    @Column(name = "datos_resultado", columnDefinition = "TEXT")
    private String datosResultado;

    /** Fecha y hora en que la simulación fue procesada y el resultado guardado. */
    @Column(name = "fecha_procesamiento")
    private LocalDateTime fechaProcesamiento;

    /**
     * Constructor vacío para JPA.
     */
    public ResultadoEntity() {
    }

    /**
     * Establece automáticamente la fecha de procesamiento justo antes de guardar el resultado por primera vez.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
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
