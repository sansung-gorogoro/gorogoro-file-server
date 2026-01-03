package com.sansung_gorogoro.file_server.presentation.response;

import com.sansung_gorogoro.file_server.application.result.StartUploadResult;
import lombok.Builder;

@Builder
public record StartUploadResponse(
        Long sessionId,
        Long declaredTotalSize,
        Long nextOffset,
        Long maxChunkSize
) {
    public static StartUploadResponse from(StartUploadResult result) {
        return StartUploadResponse.builder()
                .sessionId(result.sessionId())
                .declaredTotalSize(result.declaredTotalSize())
                .nextOffset(result.nextOffset())
                .maxChunkSize(result.maxChunkSize())
                .build();
    }
}