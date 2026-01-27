package com.sansung_gorogoro.file_server.infrastructure.persistence;

import com.sansung_gorogoro.file_server.domain.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceJpaRepository extends JpaRepository<Resource, Long> {
}
