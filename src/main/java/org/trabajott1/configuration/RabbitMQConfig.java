package org.trabajott1.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de RabbitMQ para la mensajería asíncrona del sistema de simulación.
 * Define la cola, el exchange y el binding necesarios para enrutar los mensajes
 * de solicitud de simulación desde el servicio hacia el listener que los procesa.
 *
 * @author Lucas, Ana, Clara, Santiago
 * @version 1.0
 */
@Configuration
public class RabbitMQConfig {

    /** Nombre de la cola donde se encolan las solicitudes de simulación pendientes. */
    public static final String SIMULATION_QUEUE = "simulation.queue";

    /** Nombre del exchange de tipo Direct por el que se publican los mensajes de simulación. */
    public static final String SIMULATION_EXCHANGE = "simulation.exchange";

    /** Clave de enrutamiento usada para dirigir los mensajes al exchange correcto. */
    public static final String SIMULATION_ROUTING_KEY = "simulation.routingKey";

    /**
     * Declara la cola durable donde se reciben las solicitudes de simulación.
     * Al ser durable, sobrevive a reinicios del broker RabbitMQ.
     *
     * @return la cola de simulación configurada
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Bean
    public Queue simulationQueue() {
        return new Queue(SIMULATION_QUEUE, true);
    }

    /**
     * Declara el exchange de tipo Direct por el que se publican los mensajes de simulación.
     *
     * @return el exchange de simulación configurado
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Bean
    public DirectExchange simulationExchange() {
        return new DirectExchange(SIMULATION_EXCHANGE);
    }

    /**
     * Enlaza la cola de simulación con el exchange usando la routing key definida.
     * Solo los mensajes publicados con esa routing key llegarán a esta cola.
     *
     * @param queue    la cola de simulación
     * @param exchange el exchange de simulación
     * @return el binding que conecta la cola con el exchange
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(SIMULATION_ROUTING_KEY);
    }

    /**
     * Configura el convertidor de mensajes para serializar y deserializar en formato JSON.
     * Así los objetos Java se convierten automáticamente a JSON al publicar en RabbitMQ.
     *
     * @return el convertidor de mensajes JSON basado en Jackson
     * @author Lucas, Ana, Clara, Santiago
     * @version 1.0
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
