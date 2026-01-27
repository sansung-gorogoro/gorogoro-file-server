package com.sansung_gorogoro.file_server.domain;

import com.sansung_gorogoro.file_server.common.error.code.FileErrorCode;
import com.sansung_gorogoro.file_server.common.exception.BusinessException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum ExtensionType {
    MP4("mp4", ResourceType.VIDEO);
    
    private final String extension;
    private final ResourceType resourceType;

    public static ExtensionType fromOriginalFileName(String originalFileName) {
        String ext = extractExtension(originalFileName);

        return Arrays.stream(values())
                .filter(e -> e.extension.equalsIgnoreCase(ext))
                .findFirst()
                .orElseThrow(()-> BusinessException.builder(FileErrorCode.ORIGINAL_FILE_NAME_EXTENSION_NOT_SUPPORTED).build());
    }

    private static String extractExtension(String originalFileName) {
        int idx = originalFileName.lastIndexOf('.');
        if (idx == -1 || idx == originalFileName.length() -1) {
            throw BusinessException.builder(FileErrorCode.ORIGINAL_FILE_NAME_EXTENSION_NOT_FOUND).build();
        }
        return originalFileName.substring(idx+1).trim().toLowerCase();
    }

    public static ResourceType toResourceType(String originalFileName) {
        return fromOriginalFileName(originalFileName).getResourceType();
    }
}