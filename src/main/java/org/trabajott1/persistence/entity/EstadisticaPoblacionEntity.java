package org.trabajott1.persistence.entity;

import jakarta.persistence.*;

/**
 * Entidad que almacena las estadísticas de población de una simulación.
 * Representa la cantidad de individuos de un color específico en un paso de tiempo determinado.
 */
@Entity
@Table(name = "estadisticas_poblacion")
public class EstadisticaPoblacionEntity {

    /**
     * ID único de la estadística.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Solicitud de simulación a la que pertenece esta estadística.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_solicitud")
    private SolicitudEntity solicitud;

    /**
     * El paso de tiempo (iteración) en el que se tomó la muestra.
     */
    @Column(name = "paso_tiempo")
    private Integer pasoTiempo;

    /**
     * El color que identifica a la población.
     */
    private String color;

    /**
     * La cantidad de individuos de ese color en ese paso de tiempo.
     */
    private Integer cantidad;

    /**
     * Constructor vacío para JPA.
     */
    public EstadisticaPoblacionEntity() {}

    /**
     * Crea una nueva instancia de estadística de población.
     * @param solicitud  La solicitud asociada.
     * @param pasoTiempo El paso de tiempo de la muestra.
     * @param color      El color de la población.
     * @param cantidad   La cantidad de individuos.
     */
    public EstadisticaPoblacionEntity(SolicitudEntity solicitud, Integer pasoTiempo, String color, Integer cantidad) {
        this.solicitud = solicitud;
        this.pasoTiempo = pasoTiempo;
        this.color = color;
        this.cantidad = cantidad;
    }

    /**
     * Obtiene el ID de la estadística.
     * @return El ID de la estadística.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Establece el ID de la estadística.
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
     * Obtiene el paso de tiempo de la muestra.
     * @return El paso de tiempo.
     */
    public Integer getPasoTiempo() {
        return pasoTiempo;
    }

    /**
     * Establece el paso de tiempo de la muestra.
     * @param pasoTiempo El paso de tiempo a establecer.
     */
    public void setPasoTiempo(Integer pasoTiempo) {
        this.pasoTiempo = pasoTiempo;
    }

    /**
     * Obtiene el color de la población.
     * @return El color.
     */
    public String getColor() {
        return color;
    }

    /**
     * Establece el color de la población.
     * @param color El color a establecer.
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Obtiene la cantidad de individuos.
     * @return La cantidad de individuos.
     */
    public Integer getCantidad() {
        return cantidad;
    }

    /**
     * Establece la cantidad de individuos.
     * @param cantidad La cantidad a establecer.
     */
    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}
