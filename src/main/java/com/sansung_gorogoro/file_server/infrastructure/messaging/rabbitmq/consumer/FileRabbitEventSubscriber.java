package com.sansung_gorogoro.file_server.infrastructure.messaging.rabbitmq.consumer;

import com.sansung_gorogoro.file_server.application.port.in.event.CourseDeletedEvent;
import com.sansung_gorogoro.file_server.infrastructure.messaging.domain.model.EventEnvelope;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.core.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import com.rabbitmq.client.Channel;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class FileRabbitEventSubscriber {
    private static final Logger log = LoggerFactory.getLogger(FileRabbitEventSubscriber.class);

    @RabbitListener(queues = "${rabbit.events.queues.course-deleted.name}", ackMode = "MANUAL")
    public void onCourseDeleted(
            @Payload EventEnvelope<CourseDeletedEvent> envelope,
            Message message,
            Channel channel
    ) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            log.info("consume only: courseId={}", envelope.getPayload().courseId());
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("Review Service handler error: {}", e.getMessage(), e);
            channel.basicNack(deliveryTag, false, false);
        }
        // TODO: 후속 이슈에서 cleanup usecase 연결
        log.info("Received CourseDeletedEvent: {}", event);
    }
}
