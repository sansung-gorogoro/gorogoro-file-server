package com.sansung_gorogoro.file_server.domain;

import java.util.Optional;

public interface UploadSessionRepository {
    Optional<UploadSession> findById(String uploadId);

    Object save(UploadSession session);
}
