package com.sansung_gorogoro.file_server.application.policy;

import com.sansung_gorogoro.file_server.domain.ExtensionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UploadPolicyProvider {

    public void allowedExtensions(String originalFileName) {
        ExtensionType.fromOriginalFileName(originalFileName);
    }
}
