package com.sansung_gorogoro.file_server.application.port.out;

import java.io.IOException;
import java.nio.file.Path;

public interface FileStoragePort {
    Path confirm (Path tempFilePath, String fileKey) throws IOException;
    Path resolveValidate (Path baseDir, String fileKey);
    void assertExists (Path tempFilePath);
    long size (Path tempFilePath) throws IOException;
}