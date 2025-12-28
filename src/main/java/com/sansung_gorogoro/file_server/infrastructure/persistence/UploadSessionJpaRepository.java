package com.sansung_gorogoro.file_server.infrastructure.persistence;

import com.sansung_gorogoro.file_server.domain.UploadSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadSessionJpaRepository extends JpaRepository<UploadSession, String> {
}
