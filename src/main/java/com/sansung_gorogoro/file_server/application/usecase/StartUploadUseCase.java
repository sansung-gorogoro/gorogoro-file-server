package com.sansung_gorogoro.file_server.application.usecase;

import com.sansung_gorogoro.file_server.application.result.StartUploadResult;
import com.sansung_gorogoro.file_server.domain.UploadSession;
import com.sansung_gorogoro.file_server.domain.UploadSessionRepository;
import com.sansung_gorogoro.file_server.infrastructure.config.UploadPolicyProperties;
import com.sansung_gorogoro.file_server.presentation.request.StartUploadRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StartUploadUseCase {

    private final UploadSessionRepository sessionRepository;
    private final UploadPolicyProperties props;

    private static final String TEMP_PREFIX = "tmp-";
    private static final String TEMP_EXT = ".part";

    public StartUploadResult create(StartUploadRequest req, String ownerUserId) {
        if (ownerUserId == null || ownerUserId.isBlank()) {
            throw new IllegalArgumentException("ownerUserId는 필수입니다. ");
        }

        Long declaredTotalSize = req.declaredTotalSize();

        String originalFileName = req.originalFileName();

        String uploadId = UUID.randomUUID().toString();

        Path baseDir = Path.of(props.tempBaseDir());
        try {
            Files.createDirectories(baseDir);
        } catch (IOException e) {
            throw new IllegalStateException("임시 업로드 디렉터리를 생성할 수 없습니다: " + baseDir, e);
        }

        Path tempFilePath = baseDir.resolve(TEMP_PREFIX + uploadId + TEMP_EXT);

        LocalDateTime expiresAt = LocalDateTime.now().plusHours(props.sessionTtlHours());

        UploadSession session = UploadSession.start(uploadId, ownerUserId, declaredTotalSize, originalFileName, tempFilePath.toString(), expiresAt);

        sessionRepository.save(session);

        return StartUploadResult.from(session, props.chunkMaxSizeBytes());
    }
}
