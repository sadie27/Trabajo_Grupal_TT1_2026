package org.trabajott1.persistence.entity;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para {@link SolicitudEntity}.
 * Verifica los métodos addEntidad() y addEntidades(), incluyendo
 * la relación bidireccional, el manejo de listas de tamaño distinto y listas nulas.
 *
 * @author Lucas, Ana, Clara, Santiago
 * @version 1.0
 */
class SolicitudEntityTest {

    // --- addEntidad ---

    /**
     * Verifica que addEntidad() añade la entidad a la lista y establece la relación
     * bidireccional (entidad.getSolicitud() apunta a this).
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void addEntidad_HappyPath_AddsAndSetsBackReference() {
        SolicitudEntity solicitud = new SolicitudEntity();
        EntidadSolicitudEntity entidad = new EntidadSolicitudEntity();
        entidad.setNombreEntidad("Rojo");
        entidad.setCantidadInicial(10);

        solicitud.addEntidad(entidad);

        assertEquals(1, solicitud.getEntidades().size());
        assertSame(solicitud, entidad.getSolicitud());
        assertEquals("Rojo", solicitud.getEntidades().get(0).getNombreEntidad());
    }

    /**
     * Verifica que addEntidad() llamado varias veces acumula todas las entidades.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void addEntidad_MultipleCalls_AccumulatesAll() {
        SolicitudEntity solicitud = new SolicitudEntity();

        EntidadSolicitudEntity e1 = new EntidadSolicitudEntity();
        e1.setNombreEntidad("Rojo");
        EntidadSolicitudEntity e2 = new EntidadSolicitudEntity();
        e2.setNombreEntidad("Azul");

        solicitud.addEntidad(e1);
        solicitud.addEntidad(e2);

        assertEquals(2, solicitud.getEntidades().size());
        assertSame(solicitud, e1.getSolicitud());
        assertSame(solicitud, e2.getSolicitud());
    }

    // --- addEntidades: happy path ---

    /**
     * Verifica que addEntidades() con listas del mismo tamaño crea y enlaza todas las entidades.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void addEntidades_HappyPath_CreatesAllEntities() {
        SolicitudEntity solicitud = new SolicitudEntity();
        List<String> nombres = List.of("Rojo", "Azul", "Verde");
        List<Integer> cantidades = List.of(5, 10, 15);

        solicitud.addEntidades(nombres, cantidades);

        assertEquals(3, solicitud.getEntidades().size());

        EntidadSolicitudEntity primera = solicitud.getEntidades().get(0);
        assertEquals("Rojo", primera.getNombreEntidad());
        assertEquals(5, primera.getCantidadInicial());
        assertSame(solicitud, primera.getSolicitud());

        EntidadSolicitudEntity segunda = solicitud.getEntidades().get(1);
        assertEquals("Azul", segunda.getNombreEntidad());
        assertEquals(10, segunda.getCantidadInicial());

        EntidadSolicitudEntity tercera = solicitud.getEntidades().get(2);
        assertEquals("Verde", tercera.getNombreEntidad());
        assertEquals(15, tercera.getCantidadInicial());
    }

    // --- addEntidades: tamaños distintos ---

    /**
     * Verifica que addEntidades() con listas de distinto tamaño no añade ninguna entidad
     * (fallo silencioso definido en la implementación).
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void addEntidades_MismatchedSizes_AddsNothing() {
        SolicitudEntity solicitud = new SolicitudEntity();
        List<String> nombres = List.of("Rojo", "Azul");
        List<Integer> cantidades = List.of(5);

        solicitud.addEntidades(nombres, cantidades);

        assertTrue(solicitud.getEntidades().isEmpty());
    }

    /**
     * Verifica que addEntidades() con nombres vacío y cantidades no vacío no añade ninguna entidad.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void addEntidades_EmptyNombresNonEmptyCantidades_AddsNothing() {
        SolicitudEntity solicitud = new SolicitudEntity();

        solicitud.addEntidades(List.of(), List.of(5));

        assertTrue(solicitud.getEntidades().isEmpty());
    }

    // --- addEntidades: listas nulas ---

    /**
     * Verifica que addEntidades() con nombres null no lanza excepción y no añade entidades.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void addEntidades_NullNombres_AddsNothing() {
        SolicitudEntity solicitud = new SolicitudEntity();

        solicitud.addEntidades(null, List.of(5, 10));

        assertTrue(solicitud.getEntidades().isEmpty());
    }

    /**
     * Verifica que addEntidades() con cantidades null no lanza excepción y no añade entidades.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void addEntidades_NullCantidades_AddsNothing() {
        SolicitudEntity solicitud = new SolicitudEntity();

        solicitud.addEntidades(List.of("Rojo", "Azul"), null);

        assertTrue(solicitud.getEntidades().isEmpty());
    }

    /**
     * Verifica que addEntidades() con ambas listas nulas no lanza excepción.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void addEntidades_BothNull_AddsNothing() {
        SolicitudEntity solicitud = new SolicitudEntity();

        solicitud.addEntidades(null, null);

        assertTrue(solicitud.getEntidades().isEmpty());
    }

    // --- addEntidades: listas vacías válidas ---

    /**
     * Verifica que addEntidades() con dos listas vacías no añade entidades pero tampoco falla.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Test
    void addEntidades_BothEmpty_AddsNothing() {
        SolicitudEntity solicitud = new SolicitudEntity();

        solicitud.addEntidades(List.of(), List.of());

        assertTrue(solicitud.getEntidades().isEmpty());
    }
}
