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
 * Configuración de la infraestructura de mensajería RabbitMQ.
 *
 * Define los componentes necesarios para el envío y recepción
 * de mensajes de simulación de forma asíncrona:
 * - La cola donde se depositan las tareas pendientes
 * - El exchange que recibe los mensajes y los enruta
 * - El binding que conecta el exchange con la cola
 * - El conversor JSON para serializar/deserializar los mensajes
 */
@Configuration
public class RabbitMQConfig {

    /** Nombre de la cola donde se encolan las tareas de simulación */
    public static final String SIMULATION_QUEUE = "simulation.queue";

    /** Nombre del exchange que recibe y distribuye los mensajes */
    public static final String SIMULATION_EXCHANGE = "simulation.exchange";

    /** Clave de enrutamiento que conecta el exchange con la cola */
    public static final String SIMULATION_ROUTING_KEY = "simulation.routingKey";

    /**
     * Crea la cola de simulación como durable (persistente).
     * Al ser durable, los mensajes no se pierden si RabbitMQ se reinicia.
     *
     * @return la cola configurada
     */
    @Bean
    public Queue simulationQueue() {
        return new Queue(SIMULATION_QUEUE, true);
    }

    /**
     * Crea el exchange de tipo Direct.
     * Un DirectExchange enruta cada mensaje a la cola cuya
     * routing key coincide exactamente con la del mensaje.
     *
     * @return el exchange configurado
     */
    @Bean
    public DirectExchange simulationExchange() {
        return new DirectExchange(SIMULATION_EXCHANGE);
    }

    /**
     * Vincula la cola con el exchange usando la routing key definida.
     * Sin este binding, los mensajes enviados al exchange
     * no llegarían a ninguna cola.
     *
     * @param queue    la cola de simulación
     * @param exchange el exchange de simulación
     * @return el binding que conecta ambos componentes
     */
    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(SIMULATION_ROUTING_KEY);
    }

    /**
     * Configura el conversor de mensajes a formato JSON.
     * Permite enviar y recibir objetos Java (como SimulationMessage)
     * serializados automáticamente como JSON en la cola.
     *
     * @return el conversor Jackson para mensajes AMQP
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}