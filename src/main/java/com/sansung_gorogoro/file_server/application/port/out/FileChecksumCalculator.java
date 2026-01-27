package com.sansung_gorogoro.file_server.application.port.out;

import java.nio.file.Path;

public interface FileChecksumCalculator {
    String sha256Hex(Path filePath);
}
