package com.sansung_gorogoro.file_server.common.response;

public record FieldError(
        String field,
        Object rejectedValue,
        String reason
) {
}