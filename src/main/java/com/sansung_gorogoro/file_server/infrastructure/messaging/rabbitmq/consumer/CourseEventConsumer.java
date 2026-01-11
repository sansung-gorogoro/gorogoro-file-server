package com.sansung_gorogoro.file_server.infrastructure.messaging.rabbitmq.consumer;

import com.sansung_gorogoro.file_server.application.port.in.event.CourseDeletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CourseEventConsumer {

    @RabbitListener(queues = "${rabbit.events.queues.course-deleted.name}")
    public void onCourseDeleted(CourseDeletedEvent event) {
        // TODO: 후속 이슈에서 cleanup usecase 연결
        log.info("Received CourseDeletedEvent: {}", event);
    }
}
