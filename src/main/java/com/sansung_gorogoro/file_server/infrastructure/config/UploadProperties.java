package com.sansung_gorogoro.file_server.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "upload")
public record UploadProperties(
        Temp temp,
        Session session,
        Chunk chunk
) {
    public record Temp(String baseDir) { }
    public record Session(long ttlHours) { }
    public record Chunk(long maxSizeBytes) { }
}
