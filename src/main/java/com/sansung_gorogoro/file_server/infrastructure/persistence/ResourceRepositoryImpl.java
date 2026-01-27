package com.sansung_gorogoro.file_server.infrastructure.persistence;

import com.sansung_gorogoro.file_server.domain.Resource;
import com.sansung_gorogoro.file_server.domain.ResourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ResourceRepositoryImpl implements ResourceRepository {
    private final ResourceJpaRepository jpaRepository;


    @Override
    public Optional<Resource> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Resource save(Resource resource) {
        return jpaRepository.save(resource);
    }
}
