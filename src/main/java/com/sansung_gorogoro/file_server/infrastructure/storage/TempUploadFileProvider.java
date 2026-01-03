package com.sansung_gorogoro.file_server.infrastructure.storage;

import com.sansung_gorogoro.file_server.infrastructure.config.UploadPolicyProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
@RequiredArgsConstructor
public class TempUploadFileProvider {

    private static final String TEMP_PREFIX = "tmp-";
    private static final String TEMP_FILE_EXT = ".part";

    private final UploadPolicyProperties props;

    public Path resolveTempFilePath(Long sessionId) {
        Path baseDir = Path.of(props.tempBaseDir());
        ensureDirectoryExists(baseDir);

        return baseDir.resolve(TEMP_PREFIX + sessionId + TEMP_FILE_EXT);
    }

    private void ensureDirectoryExists(Path baseDir) {
        try {
            Files.createDirectories(baseDir);
        } catch (IOException e) {
            throw new IllegalStateException("임시 업로드 디렉터리를 생성할 수 없습니다: " + baseDir, e);
        }
    }
}
