package com.sansung_gorogoro.file_server.application.usecase;

import com.sansung_gorogoro.file_server.application.result.ChunkUploadResult;
import com.sansung_gorogoro.file_server.common.error.code.FileErrorCode;
import com.sansung_gorogoro.file_server.common.exception.BusinessException;
import com.sansung_gorogoro.file_server.domain.UploadSession;
import com.sansung_gorogoro.file_server.domain.UploadSessionRepository;
import com.sansung_gorogoro.file_server.infrastructure.config.UploadPolicyProperties;
import com.sansung_gorogoro.file_server.infrastructure.storage.ChunkFileWriter;
import com.sansung_gorogoro.file_server.infrastructure.storage.TempUploadFileProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChunkUploadUseCase {

    private final UploadSessionRepository repository;
    private final UploadPolicyProperties props;
    private final ChunkFileWriter writer;
    private final TempUploadFileProvider tempUploadFileProvider;

    @Transactional
    public ChunkUploadResult chunk(Long sessionId, Long ownerUserId, Long uploadOffset, Long contentLength, InputStream body) throws IOException {
        UploadSession session = repository.findById(sessionId)
                .orElseThrow(()-> BusinessException.builder(FileErrorCode.UPLOAD_SESSION_NOT_FOUND).build());

        session.assertOwner(ownerUserId);
        validateChunkSize(contentLength);
        validateChunkWithinRemainingBytes(contentLength, session.getDeclaredTotalSize(), uploadOffset);
        session.assertExpectedOffset(uploadOffset);
        String tempFilePath = tempUploadFileProvider.resolveTempFilePath(session.getId()).toString();

        long written = writer.writeAtOffset(tempFilePath, uploadOffset, body);

        session.applyChunkWritten(written);

        return ChunkUploadResult.from(session);
    }

    private void validateChunkSize(Long contentLength) {
        if (contentLength != -1 && contentLength > props.chunkMaxSizeBytes()) {
            throw BusinessException.builder(FileErrorCode.UPLOAD_CHUNK_SIZE_EXCEEDS_LIMIT).build();
        }

        if (contentLength == 0) {
            throw BusinessException.builder(FileErrorCode.UPLOAD_CHUNK_EMPTY).build();
        }
    }

    private void validateChunkWithinRemainingBytes(Long contentLength, Long declaredTotalSize, Long uploadOffset) {
        Long remaining = declaredTotalSize - uploadOffset;

        /**
         * (디버그용) 수동 HTTP/CLI 테스트에서 마지막 청크 크기/offset을 맞추기 어려워서,
         * 서버가 계산한 remaining(= declaredTotalSize - uploadOffset)과 요청 contentLength를 로그로 남긴다.
         * → 어떤 값이 초과했는지(현재 offset, 청크 크기, 총 크기, 남은 용량)를 즉시 확인해
         * 마지막 청크를 remaining만큼으로 조정(dd/split 등)할 때 참고하기 위한 로그.
         */
        if (contentLength > remaining) {
            log.warn("FS-011 debug: offset={}, contentLength={}, total={}, remaining={}",
                    uploadOffset, contentLength, declaredTotalSize, declaredTotalSize - uploadOffset);

            throw BusinessException.builder(FileErrorCode.UPLOAD_CHUNK_EXCEEDS_REMAINING).build();
        }
    }
}