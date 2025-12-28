package com.sansung_gorogoro.file_server.presentation.response;

import com.sansung_gorogoro.file_server.application.result.StartUploadResult;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record StartUploadResponse(
        String uploadId,
        Long declaredTotalSize,
        Long nextOffset,
        Long maxChunkSize
) {
    public static StartUploadResponse from(StartUploadResult result) {
        return StartUploadResponse.builder()
                .uploadId(result.uploadId())
                .declaredTotalSize(result.declaredTotalSize())
                .nextOffset(result.nextOffset())
                .maxChunkSize(result.maxChunkSize())
                .build();
    }
}