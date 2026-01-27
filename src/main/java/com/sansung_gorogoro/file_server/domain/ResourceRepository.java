package com.sansung_gorogoro.file_server.domain;

import java.util.Optional;

public interface ResourceRepository {
    Optional<Resource> findById(Long id);

    Resource save(Resource resource);
}
