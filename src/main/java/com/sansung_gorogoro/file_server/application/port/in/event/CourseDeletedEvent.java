package com.sansung_gorogoro.file_server.application.port.in.event;

import java.util.List;

public record CourseDeletedEvent(
        Long courseId,
        List<Long> videoResourceIds
) {}