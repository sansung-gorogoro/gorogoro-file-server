package com.sansung_gorogoro.file_server.infrastructure.messaging.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(RabbitEventsProperties.class)
public class RabbitConfig {

    private final RabbitEventsProperties props;

    @Bean
    public TopicExchange courseExchange() {
        return new TopicExchange(props.exchange(), true, false);
    }

    @Bean
    public Queue fileCleanupQueue() {
        RabbitEventsProperties.QueueSpec spec = props.queues().get("course-deleted");

        return QueueBuilder.durable(spec.name()).build();
    }

    @Bean
    public Declarables bindCleanup (Queue fileCleanupQueue, TopicExchange courseExchange) {
        RabbitEventsProperties.QueueSpec spec = props.queues().get("course-deleted");

        return new Declarables(
                spec.bindings().stream()
                        .map(rk -> BindingBuilder.bind(fileCleanupQueue)
                                .to(courseExchange)
                                .with(rk))
                        .toList()
        );
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            Jackson2JsonMessageConverter converter
    ) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(converter);
        return factory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(
            ConnectionFactory connectionFactory,
            Jackson2JsonMessageConverter converter
    ) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter);
        return template;
    }
}
