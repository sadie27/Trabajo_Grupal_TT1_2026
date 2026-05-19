package org.trabajott1.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de RabbitMQ para la mensajería asíncrona del sistema de simulación.
 * Implementa un patrón muchos-a-muchos usando un TopicExchange y múltiples colas
 * independientes. Cada cola representa un worker distinto que recibe todos los mensajes
 * publicados, permitiendo que varios consumidores procesen cada simulación en paralelo.
 *
 * @author Lucas, Ana, Clara, Santiago
 * @version 2.0
 */
@Configuration
public class RabbitMQConfig {

    /** Exchange de tipo Topic al que se publican los mensajes de simulación. */
    public static final String SIMULATION_EXCHANGE = "simulation.exchange";

    /** Routing key usada al publicar mensajes (coincide con el patrón de binding). */
    public static final String SIMULATION_ROUTING_KEY = "simulation.task";

    /** Cola del worker A: recibe todos los mensajes de simulación. */
    public static final String SIMULATION_QUEUE_A = "simulation.queue.A";

    /** Cola del worker B: recibe todos los mensajes de simulación. */
    public static final String SIMULATION_QUEUE_B = "simulation.queue.B";

    /**
     * Declara el exchange de tipo Topic.
     * A diferencia del DirectExchange anterior, un TopicExchange permite que un único
     * mensaje publicado llegue a múltiples colas según sus patrones de binding.
     *
     * @return el exchange de simulación configurado
     * @author Lucas, Ana, Clara, Santiago
     * @version 2.0
     */
    @Bean
    public TopicExchange simulationExchange() {
        return new TopicExchange(SIMULATION_EXCHANGE);
    }

    /**
     * Declara la cola durable del worker A.
     *
     * @return cola del worker A
     * @author Lucas, Ana, Clara, Santiago
     * @version 2.0
     */
    @Bean
    public Queue simulationQueueA() {
        return new Queue(SIMULATION_QUEUE_A, true);
    }

    /**
     * Declara la cola durable del worker B.
     *
     * @return cola del worker B
     * @author Lucas, Ana, Clara, Santiago
     * @version 2.0
     */
    @Bean
    public Queue simulationQueueB() {
        return new Queue(SIMULATION_QUEUE_B, true);
    }

    /**
     * Vincula la cola A al exchange usando el patrón "simulation.#".
     * El carácter # en un TopicExchange hace que coincida con cualquier routing key
     * que empiece por "simulation.", por lo que esta cola recibe todos los mensajes.
     *
     * @param simulationQueueA la cola del worker A
     * @param simulationExchange el exchange de simulación
     * @return el binding de la cola A
     * @author Lucas, Ana, Clara, Santiago
     * @version 2.0
     */
    @Bean
    public Binding bindingA(Queue simulationQueueA, TopicExchange simulationExchange) {
        return BindingBuilder.bind(simulationQueueA).to(simulationExchange).with("simulation.#");
    }

    /**
     * Vincula la cola B al exchange usando el mismo patrón "simulation.#".
     * Gracias a esto, el mismo mensaje publicado llega tanto a la cola A como a la B
     * (patrón muchos-a-muchos: muchos productores, múltiples colas/consumidores).
     *
     * @param simulationQueueB la cola del worker B
     * @param simulationExchange el exchange de simulación
     * @return el binding de la cola B
     * @author Lucas, Ana, Clara, Santiago
     * @version 2.0
     */
    @Bean
    public Binding bindingB(Queue simulationQueueB, TopicExchange simulationExchange) {
        return BindingBuilder.bind(simulationQueueB).to(simulationExchange).with("simulation.#");
    }

    /**
     * Configura el convertidor de mensajes para serializar y deserializar en formato JSON.
     *
     * @return el convertidor de mensajes JSON basado en Jackson
     * @author Lucas, Ana, Clara, Santiago
     * @version 2.0
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
