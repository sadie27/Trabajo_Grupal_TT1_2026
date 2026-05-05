package org.trabajott1.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String SIMULATION_QUEUE = "simulation.queue";
    public static final String SIMULATION_EXCHANGE = "simulation.exchange";
    public static final String SIMULATION_ROUTING_KEY = "simulation.routingKey";

    @Bean
    public Queue simulationQueue() {
        return new Queue(SIMULATION_QUEUE, true);
    }

    @Bean
    public DirectExchange simulationExchange() {
        return new DirectExchange(SIMULATION_EXCHANGE);
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(SIMULATION_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
