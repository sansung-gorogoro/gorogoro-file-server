package com.sansung_gorogoro.file_server.presentation.request;

import jakarta.validation.constraints.NotNull;

public record CompleteUploadRequest(
        @NotNull(message = "강의 아이디는 필수입니다.")
        Long lessonId,
        String expectedSha256
) {
}
