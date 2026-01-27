package com.sansung_gorogoro.file_server.presentation.controller;

import com.sansung_gorogoro.file_server.application.result.ChunkUploadResult;
import com.sansung_gorogoro.file_server.application.result.CompleteUploadResult;
import com.sansung_gorogoro.file_server.application.result.StartUploadResult;
import com.sansung_gorogoro.file_server.application.usecase.ChunkUploadUseCase;
import com.sansung_gorogoro.file_server.application.usecase.CompleteUploadUseCase;
import com.sansung_gorogoro.file_server.application.usecase.StartUploadUseCase;
import com.sansung_gorogoro.file_server.presentation.request.CompleteUploadRequest;
import com.sansung_gorogoro.file_server.presentation.request.StartUploadRequest;
import com.sansung_gorogoro.file_server.presentation.response.ChunkUploadResponse;
import com.sansung_gorogoro.file_server.presentation.response.CompleteUploadResponse;
import com.sansung_gorogoro.file_server.presentation.response.StartUploadResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UploadController {

    private final StartUploadUseCase startUploadUseCase;
    private final ChunkUploadUseCase chunkUploadUseCase;
    private final CompleteUploadUseCase completeUploadUseCase;

    @PostMapping("/uploads/sessions")
    public StartUploadResponse create(@RequestHeader("X-User-Id") Long ownerUserId,
                                     @RequestBody @Valid StartUploadRequest req) {

        StartUploadResult result = startUploadUseCase.create(req, ownerUserId);
        return StartUploadResponse.from(result);
    }

    @PutMapping("/uploads/sessions/{sessionId}/chunks")
    public ChunkUploadResponse uploadChunk(@PathVariable Long sessionId,
                                           @RequestHeader("X-User-Id") Long ownerUserId,
                                           @RequestHeader("Upload-Offset") Long uploadOffset,
                                           @RequestBody byte[] body
                                           ) throws IOException {
        long contentLength = body.length;
        ChunkUploadResult result = chunkUploadUseCase.chunk(sessionId, ownerUserId, uploadOffset, contentLength, new ByteArrayInputStream(body));
        return ChunkUploadResponse.from(result);
    }

    @PostMapping("/uploads/sessions/{sessionId}/complete")
    public CompleteUploadResponse uploadComplete(@PathVariable Long sessionId,
                                                 @RequestHeader("X-User-Id") Long ownerUserId,
                                                 @RequestBody @Valid CompleteUploadRequest req) throws IOException {

        CompleteUploadResult result = completeUploadUseCase.complete(sessionId, ownerUserId, req.lessonId(), req.expectedSha256());
        return CompleteUploadResponse.from(result);
    }
}