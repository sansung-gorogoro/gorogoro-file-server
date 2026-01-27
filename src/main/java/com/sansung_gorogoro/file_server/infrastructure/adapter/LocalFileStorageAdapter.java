package com.sansung_gorogoro.file_server.infrastructure.adapter;

import com.sansung_gorogoro.file_server.application.port.out.FileStoragePort;
import com.sansung_gorogoro.file_server.common.error.code.FileErrorCode;
import com.sansung_gorogoro.file_server.common.exception.BusinessException;
import com.sansung_gorogoro.file_server.infrastructure.config.UploadPolicyProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Component
@RequiredArgsConstructor
public class LocalFileStorageAdapter implements FileStoragePort {
    private final UploadPolicyProperties props;

    @Override
    public Path confirm(Path tempFilePath, String fileKey) throws IOException {
        Path filePath = resolveValidate(Path.of(props.fileBaseDir()), fileKey);

        try {
            Files.createDirectories(filePath.getParent());

            return Files.move(
                    tempFilePath,
                    filePath,
                    StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.ATOMIC_MOVE
            );
        } catch (IOException e) {
            throw BusinessException.builder(FileErrorCode.FILE_CONFIRM_FAILED).build();
        }
    }

    @Override
    public void assertExists(Path tempFilePath) {
        if (!Files.exists(tempFilePath)) {
            throw BusinessException.builder(FileErrorCode.UPLOAD_TEMP_FILE_NOT_FOUND).build();
        }
    }

    @Override
    public long size(Path tempFilePath) throws IOException {
        return Files.size(tempFilePath);
    }

    @Override
    public Path resolveValidate(Path baseDir, String fileKey) {
        Path keyPath = Path.of(fileKey);
        if (keyPath.isAbsolute()) {
            throw BusinessException.builder(FileErrorCode.INVALID_FILE_KEY).build();
        }

        Path normalizedBase = baseDir.toAbsolutePath().normalize(); // 반드시 절대경로
        Path filePath = normalizedBase.resolve(keyPath).normalize();

        if (!filePath.startsWith(normalizedBase)) {
            throw BusinessException.builder(FileErrorCode.INVALID_FILE_KEY).build();
        }
        return filePath;
    }
}
