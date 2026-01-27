package com.sansung_gorogoro.file_server.application.usecase;

import com.sansung_gorogoro.file_server.application.port.out.FileChecksumCalculator;
import com.sansung_gorogoro.file_server.application.port.out.FileStoragePort;
import com.sansung_gorogoro.file_server.application.result.CompleteUploadResult;
import com.sansung_gorogoro.file_server.common.error.code.FileErrorCode;
import com.sansung_gorogoro.file_server.common.exception.BusinessException;
import com.sansung_gorogoro.file_server.domain.ExtensionType;
import com.sansung_gorogoro.file_server.domain.Resource;
import com.sansung_gorogoro.file_server.domain.ResourceRepository;
import com.sansung_gorogoro.file_server.domain.ResourceType;
import com.sansung_gorogoro.file_server.domain.UploadSession;
import com.sansung_gorogoro.file_server.domain.UploadSessionRepository;
import com.sansung_gorogoro.file_server.infrastructure.storage.FinalStorageKeyProvider;
import com.sansung_gorogoro.file_server.infrastructure.storage.TempUploadFileProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class CompleteUploadUseCase {
    private final UploadSessionRepository sessionRepository;
    private final ResourceRepository resourceRepository;
    private final TempUploadFileProvider tempUploadFileProvider;
    private final FinalStorageKeyProvider finalStorageKeyProvider;
    private final FileStoragePort fileStoragePort;
    private final FileChecksumCalculator checksumCalculator;

    @Transactional
    public CompleteUploadResult complete (Long sessionId, Long ownerUserId, Long lessonId, String expectedSha256) throws IOException {
        UploadSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> BusinessException.builder(FileErrorCode.UPLOAD_SESSION_NOT_FOUND).build());

        session.assertOwner(ownerUserId);
        session.assertNotExpired();
        session.assertUploading();
        session.assertFullyUploaded();

        Path tempFilePath = tempUploadFileProvider.resolveTempFilePath(sessionId);

        fileStoragePort.assertExists(tempFilePath);

        long actualSize = fileStoragePort.size(tempFilePath);
        session.assertSizeMatches(actualSize);

        String fileKey = finalStorageKeyProvider.generateFor(lessonId, session.getOriginalFileName());


        Path filePath = fileStoragePort.confirm(tempFilePath, fileKey);
        String computedSha256 = checksumCalculator.sha256Hex(filePath);

        ResourceType resourceType = ExtensionType.toResourceType(session.getOriginalFileName());
        Resource resource = Resource.start(
                fileKey,
                resourceType,
                lessonId);

        resourceRepository.save(resource);
        session.markCompleted();
        return CompleteUploadResult.of(resource, computedSha256, expectedSha256);
    }
}
