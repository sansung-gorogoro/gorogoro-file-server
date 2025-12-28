package com.sansung_gorogoro.file_server.application.result;

import com.sansung_gorogoro.file_server.domain.UploadSession;
import lombok.Builder;

@Builder
public record StartUploadResult(
        String uploadId,
        Long declaredTotalSize,
        Long nextOffset,
        Long maxChunkSize
) {
    public static StartUploadResult from (UploadSession session, Long maxChunkSize
    ) {
        return StartUploadResult.builder()
                .uploadId(session.getUploadId())
                .declaredTotalSize(session.getDeclaredTotalSize())
                .nextOffset(session.getNextOffset())
                .maxChunkSize(maxChunkSize)
                .build();
    }
}
