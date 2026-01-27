package com.sansung_gorogoro.file_server.application.result;

import com.sansung_gorogoro.file_server.domain.Resource;

public record CompleteUploadResult(
        Long resourceId,
        String computedSha256,
        String computedSha256Fingerprint,
        Boolean verified
) {
    public static CompleteUploadResult of (
            Resource resource,
            String computedSha256,
            String expectedSha256
    ) {
        String computedFp = fingerprint(computedSha256);
        Boolean verified = (expectedSha256 == null)
                ? null
                : computedSha256 != null && computedSha256.equalsIgnoreCase(expectedSha256);
        return new CompleteUploadResult(
                resource.getId(),
                computedSha256,
                computedFp,
                verified);
    }
    private static String fingerprint(String sha256Hex) {
        if (sha256Hex == null) return null;
        if (sha256Hex.length() <= 16) return sha256Hex;
        String head = sha256Hex.substring(0, 8);
        String tail = sha256Hex.substring(sha256Hex.length() - 8);
        return head + "..." + tail;
    }
}
