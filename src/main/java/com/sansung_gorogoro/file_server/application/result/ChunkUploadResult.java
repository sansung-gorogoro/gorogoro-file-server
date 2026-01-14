package com.sansung_gorogoro.file_server.application.result;

import com.sansung_gorogoro.file_server.domain.UploadSession;

public record ChunkUploadResult(
        Long nextOffset
) {
    public static ChunkUploadResult from(UploadSession session) {
        return new ChunkUploadResult(
                session.getNextOffset()
        );
    }
}