package com.sansung_gorogoro.file_server.presentation.response;

import com.sansung_gorogoro.file_server.application.result.CompleteUploadResult;

public record CompleteUploadResponse(
        Long resourceId,
        String computedSha256,
        String sha256Fingerprint,
        Boolean verified
) {
    public static CompleteUploadResponse from (CompleteUploadResult result) {
        return new CompleteUploadResponse(
                result.resourceId(),
                result.computedSha256(),
                fingerprint(result.computedSha256()),
                result.verified()
                );
    }

    private static String fingerprint(String sha256Hex) {
        if (sha256Hex == null) return null;
        // SHA-256 hex는 보통 64자. 혹시 짧으면 그대로 반환
        if (sha256Hex.length() <= 16) return sha256Hex;

        String head = sha256Hex.substring(0, 6);
        String tail = sha256Hex.substring(sha256Hex.length() - 6);
        return head + "…" + tail;  // 560086…9c72b8
    }
}
