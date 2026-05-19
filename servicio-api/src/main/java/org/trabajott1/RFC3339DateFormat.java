package org.trabajott1;

import com.fasterxml.jackson.databind.util.StdDateFormat;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Formateador de fechas que implementa el estándar RFC3339 (basado en ISO 8601) para la API.
 * Usa la zona horaria UTC y añade los dos puntos en el offset de zona horaria (p.ej. +00:00).
 *
 * @author Lucas, Ana, Clara, Santiago
 * @version 1.0
 */
public class RFC3339DateFormat extends DateFormat {
    private static final long serialVersionUID = 1L;

    /** Zona horaria UTC usada en todas las conversiones de fecha. */
    private static final TimeZone TIMEZONE_Z = TimeZone.getTimeZone("UTC");

    /** Formateador interno de Jackson configurado con UTC y el colon en la zona horaria. */
    private final StdDateFormat fmt = new StdDateFormat()
            .withTimeZone(TIMEZONE_Z)
            .withColonInTimeZone(true);

    /**
     * Crea una nueva instancia del formateador con el calendario gregoriano por defecto.
     *
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    public RFC3339DateFormat() {
        this.calendar = new GregorianCalendar();
    }

    /**
     * Parsea una cadena de texto en formato RFC3339 y la convierte en un objeto {@link Date}.
     *
     * @param source la cadena de texto con la fecha a parsear
     * @param pos    la posición de inicio del parseo dentro de la cadena
     * @return el objeto {@link Date} resultante, o {@code null} si el parseo falla
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Override
    public Date parse(String source, ParsePosition pos) {
        return fmt.parse(source, pos);
    }

    /**
     * Formatea un objeto {@link Date} como cadena de texto en formato RFC3339.
     *
     * @param date          la fecha a formatear
     * @param toAppendTo    el buffer donde se añade la cadena resultante
     * @param fieldPosition información sobre el campo formateado (no se usa habitualmente)
     * @return el buffer con la fecha formateada añadida
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Override
    public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
        return fmt.format(date, toAppendTo, fieldPosition);
    }

    /**
     * Devuelve esta misma instancia al clonar, ya que el formateador es inmutable.
     *
     * @return esta misma instancia
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Override
    public Object clone() {
        return this;
    }
}