package com.sansung_gorogoro.file_server.presentation.controller;

import com.sansung_gorogoro.file_server.application.result.StartUploadResult;
import com.sansung_gorogoro.file_server.application.usecase.StartUploadUseCase;
import com.sansung_gorogoro.file_server.presentation.request.StartUploadRequest;
import com.sansung_gorogoro.file_server.presentation.response.StartUploadResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UploadController {

    private final StartUploadUseCase startUploadUseCase;

    @PostMapping("/uploads")
    public ResponseEntity<?> create (@RequestHeader("X-User-Id") Long ownerUserId,
                                     @RequestBody @Valid StartUploadRequest req) {

        StartUploadResult result = startUploadUseCase.create(req, ownerUserId);
        StartUploadResponse response = StartUploadResponse.from(result);

        return ResponseEntity.ok(response);
    }
}
