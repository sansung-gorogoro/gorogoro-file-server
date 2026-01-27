package com.sansung_gorogoro.file_server.infrastructure.messaging.rabbitmq;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "rabbit.events")
public record RabbitEventsProperties(
        String exchange,
        Map<String, QueueSpec> queues
) {
    public record QueueSpec(String name, List<String> bindings){
    }
}
