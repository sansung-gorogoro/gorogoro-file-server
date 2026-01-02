package com.sansung_gorogoro.file_server.domain;

import java.util.Optional;

public interface UploadSessionRepository {
    Optional<UploadSession> findById(Long id);

    UploadSession save(UploadSession session);
}