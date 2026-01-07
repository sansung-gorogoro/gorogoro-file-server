package com.sansung_gorogoro.file_server.presentation.controller;

import com.sansung_gorogoro.file_server.application.result.StartUploadResult;
import com.sansung_gorogoro.file_server.application.usecase.StartUploadUseCase;
import com.sansung_gorogoro.file_server.presentation.request.StartUploadRequest;
import com.sansung_gorogoro.file_server.presentation.response.StartUploadResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UploadController {

    private final StartUploadUseCase startUploadUseCase;

    @PostMapping("/uploads")
    public StartUploadResponse create(@RequestHeader("X-User-Id") Long ownerUserId,
                                     @RequestBody @Valid StartUploadRequest req) {

        StartUploadResult result = startUploadUseCase.create(req, ownerUserId);
        return StartUploadResponse.from(result);
    }
}
