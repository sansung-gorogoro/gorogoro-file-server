package com.sansung_gorogoro.file_server.infrastructure.storage;

import com.sansung_gorogoro.file_server.application.port.out.FileChecksumCalculator;
import com.sansung_gorogoro.file_server.common.error.code.FileErrorCode;
import com.sansung_gorogoro.file_server.common.exception.BusinessException;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class LocalFileChecksumCalculator implements FileChecksumCalculator {

    /**
     * 최종 파일(Path)의 SHA-256 해시(hex 문자열)를 계산합니다.
     * - 대용량 파일도 안전하도록 스트리밍 방식으로 읽습니다.
     */
    public String sha256Hex(Path filePath) {
        MessageDigest digest = sha256Digest();

        byte[] buffer = new byte[1024 * 1024];

        try (InputStream in = new BufferedInputStream(Files.newInputStream(filePath))) {
            int read;
            while ((read = in.read(buffer)) != -1) {
                digest.update(buffer, 0, read);
            }
        } catch (IOException e) {
            throw BusinessException.builder(FileErrorCode.FILE_CHECKSUM_CALCULATION_FAILED)
                    .withCause(e)
                    .build();
        }

        return toHex(digest.digest());
    }

    private static MessageDigest sha256Digest() {
        try {
            return MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 algorithm not available", e);
        }
    }

    private static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(Character.forDigit((b >>> 4) & 0xF, 16));
            sb.append(Character.forDigit(b & 0xF, 16));
        }
        return sb.toString();
    }
}