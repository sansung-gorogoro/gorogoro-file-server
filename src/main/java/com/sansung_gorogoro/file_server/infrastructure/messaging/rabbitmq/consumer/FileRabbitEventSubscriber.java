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
            if (envelope == null) {
                log.warn("File Service consume: envelope is null. rk={} - nacking(requeue=false)",
                        message.getMessageProperties().getReceivedRoutingKey());
                channel.basicNack(deliveryTag, false, false);
                return;
            }

            CourseDeletedEvent payload = envelope.getPayload();

            if (payload == null || payload.lessonId() == null) {
                log.warn("File Service consume: invalid message (payload or lessonId is null). rk={} messageId={} traceId={} - nacking(requeue=false)",
                        message.getMessageProperties().getReceivedRoutingKey(),
                        envelope.getMetadata() != null ? envelope.getMetadata().getMessageId() : "n/a",
                        envelope.getMetadata() != null ? envelope.getMetadata().getTraceId() : "n/a"
                );
                channel.basicNack(deliveryTag, false, false);
                return;
            }

            log.info("File Service consume: rk={} messageId={} traceId={} lessonId={}",
                    message.getMessageProperties().getReceivedRoutingKey(),
                    envelope.getMetadata() != null ? envelope.getMetadata().getMessageId() : "n/a",
                    envelope.getMetadata() != null ? envelope.getMetadata().getTraceId() : "n/a",
                    payload.lessonId()
            );

            log.debug("Received CourseDeletedEvent payload={}", payload);
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("File Service handler error: {}", e.getMessage(), e);
            channel.basicNack(deliveryTag, false, false);
        }
    }
}
