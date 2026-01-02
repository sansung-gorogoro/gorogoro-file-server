package com.sansung_gorogoro.file_server.application.result;

import com.sansung_gorogoro.file_server.domain.UploadSession;
import lombok.Builder;

@Builder
public record StartUploadResult(
        Long sessionId,
        Long declaredTotalSize,
        Long nextOffset,
        Long maxChunkSize
) {
    public static StartUploadResult from (UploadSession session, Long maxChunkSize
    ) {
        return StartUploadResult.builder()
                .sessionId(session.getId())
                .declaredTotalSize(session.getDeclaredTotalSize())
                .nextOffset(session.getNextOffset())
                .maxChunkSize(maxChunkSize)
                .build();
    }
}
