package com.sansung_gorogoro.file_server.infrastructure.persistence;

import com.sansung_gorogoro.file_server.domain.UploadSession;
import com.sansung_gorogoro.file_server.domain.UploadSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UploadSessionRepositoryImpl implements UploadSessionRepository {
    private final UploadSessionJpaRepository jpaRepository;

    @Override
    public Optional<UploadSession> findById(String uploadId) {
        return jpaRepository.findById(uploadId);
    }

    @Override
    public UploadSession save(UploadSession session) {
        return jpaRepository.save(session);
    }
}
