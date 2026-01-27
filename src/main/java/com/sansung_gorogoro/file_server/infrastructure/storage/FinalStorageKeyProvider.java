package com.sansung_gorogoro.file_server.infrastructure.storage;

import com.sansung_gorogoro.file_server.domain.ExtensionType;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FinalStorageKeyProvider {

    public String generateFor(Long lessonId, String originalFileName) {
        String ext = ExtensionType.fromOriginalFileName(originalFileName).getExtension();
        return String.format("lessons/%d/video.%s", lessonId, ext);
    }
}