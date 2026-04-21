package org.openapitools.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

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

    @PrePersist
    protected void onCreate() {
        fechaProcesamiento = LocalDateTime.now();
    }

    public ResultadoEntity() {
    }

    public Integer getIdResultado() {
        return idResultado;
    }

    public void setIdResultado(Integer idResultado) {
        this.idResultado = idResultado;
    }

    public SolicitudEntity getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(SolicitudEntity solicitud) {
        this.solicitud = solicitud;
    }

    public String getDatosResultado() {
        return datosResultado;
    }

    public void setDatosResultado(String datosResultado) {
        this.datosResultado = datosResultado;
    }

    public LocalDateTime getFechaProcesamiento() {
        return fechaProcesamiento;
    }

    public void setFechaProcesamiento(LocalDateTime fechaProcesamiento) {
        this.fechaProcesamiento = fechaProcesamiento;
    }
}
