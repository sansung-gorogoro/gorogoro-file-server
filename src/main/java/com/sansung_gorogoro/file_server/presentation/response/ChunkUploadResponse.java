package com.sansung_gorogoro.file_server.presentation.response;

import com.sansung_gorogoro.file_server.application.result.ChunkUploadResult;

public record ChunkUploadResponse (
        Long nextOffset
){
    public static ChunkUploadResponse from (ChunkUploadResult result) {
        return new ChunkUploadResponse(
                result.nextOffset());
    }
}
