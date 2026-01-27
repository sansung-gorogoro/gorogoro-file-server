package com.sansung_gorogoro.file_server.infrastructure.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "upload")
public record UploadPolicyProperties(
        @NotBlank
        String tempBaseDir,
        @NotBlank
        String fileBaseDir,
        @Positive
        long sessionTtlHours,
        @Positive
        long chunkMaxSizeBytes
) {
}
