package com.sansung_gorogoro.file_server.application.policy;

import com.sansung_gorogoro.file_server.infrastructure.config.UploadPolicyProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class UploadSessionExpiryProvider {

    private final UploadPolicyProperties props;

    public LocalDateTime calculateExpiresAt() {
        return LocalDateTime.now().plusHours(props.sessionTtlHours());
    }
}