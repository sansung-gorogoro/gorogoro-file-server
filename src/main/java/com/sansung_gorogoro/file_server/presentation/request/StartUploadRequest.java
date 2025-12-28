package com.sansung_gorogoro.file_server.presentation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record StartUploadRequest(
        @NotBlank(message = "originalFileName은 필수입니다.")
        @Size(max = 255)
        String originalFileName,

        @NotNull(message = "declaredTotalSize는 필수입니다.")
        @Positive(message = "declaredTotalSize는 1 이상이어야 합니다.")
        Long declaredTotalSize
) {

}
