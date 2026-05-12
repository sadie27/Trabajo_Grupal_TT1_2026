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
 * Configuración de RabbitMQ para la mensajería asíncrona.
 */
@Configuration
public class RabbitMQConfig {

    public static final String SIMULATION_QUEUE = "simulation.queue";
    public static final String SIMULATION_EXCHANGE = "simulation.exchange";
    public static final String SIMULATION_ROUTING_KEY = "simulation.routingKey";

    /**
     * Define la cola de simulación.
     * @return Instancia de Queue.
     */
    @Bean
    public Queue simulationQueue() {
        return new Queue(SIMULATION_QUEUE, true);
    }

    /**
     * Define el exchange de tipo Direct para las simulaciones.
     * @return Instancia de DirectExchange.
     */
    @Bean
    public DirectExchange simulationExchange() {
        return new DirectExchange(SIMULATION_EXCHANGE);
    }

    /**
     * Define el binding entre la cola y el exchange usando la routing key.
     * @param queue Cola.
     * @param exchange Exchange.
     * @return Instancia de Binding.
     */
    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(SIMULATION_ROUTING_KEY);
    }

    /**
     * Define el convertidor de mensajes a formato JSON.
     * @return Instancia de MessageConverter.
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
