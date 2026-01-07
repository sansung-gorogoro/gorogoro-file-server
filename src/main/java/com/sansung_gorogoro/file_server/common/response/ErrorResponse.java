package com.sansung_gorogoro.file_server.common.response;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
        LocalDateTime timestamp,
        String errorCode,
        String message,
        List<FieldError> errors
) {
    public static ErrorResponse of(
            String errorCode,
            String message,
            List<FieldError> errors
    ) {
        return new ErrorResponse(
                LocalDateTime.now(),
                errorCode,
                message,
                errors
        );
    }
}
