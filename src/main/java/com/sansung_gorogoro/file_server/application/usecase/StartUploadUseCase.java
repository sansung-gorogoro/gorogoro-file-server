package com.sansung_gorogoro.file_server.application.usecase;

import com.sansung_gorogoro.file_server.application.policy.UploadSessionExpiryProvider;
import com.sansung_gorogoro.file_server.application.result.StartUploadResult;
import com.sansung_gorogoro.file_server.domain.UploadSession;
import com.sansung_gorogoro.file_server.domain.UploadSessionRepository;
import com.sansung_gorogoro.file_server.infrastructure.config.UploadPolicyProperties;
import com.sansung_gorogoro.file_server.infrastructure.storage.TempUploadFileProvider;
import com.sansung_gorogoro.file_server.presentation.request.StartUploadRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StartUploadUseCase {

    private final UploadSessionRepository sessionRepository;
    private final UploadSessionExpiryProvider expiryProvider;
    private final TempUploadFileProvider tempUploadFileProvider;
    private final UploadPolicyProperties props;

    public StartUploadResult create(StartUploadRequest req, Long ownerUserId) {
        if (ownerUserId == null) {
            throw new IllegalArgumentException("ownerUserId는 필수입니다. ");
        }

        LocalDateTime expiresAt = expiryProvider.calculateExpiresAt();

        UploadSession session = UploadSession.start(ownerUserId, req.declaredTotalSize(), req.originalFileName(), expiresAt);
        sessionRepository.save(session);

        tempUploadFileProvider.resolveTempFilePath(session.getId());
        return StartUploadResult.from(session, props.chunkMaxSizeBytes());
    }
}
