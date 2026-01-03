package com.sansung_gorogoro.file_server.presentation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record StartUploadRequest(
        @NotBlank(message = "originalFileName은 필수입니다.")
        @Size(max = 255, message = "파일명은 최대 255자까지 입력할 수 있습니다.")
        String originalFileName,

        @NotNull(message = "declaredTotalSize는 필수입니다.")
        @Positive(message = "declaredTotalSize는 1 이상이어야 합니다.")
        Long declaredTotalSize
) {
}