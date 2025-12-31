package com.sansung_gorogoro.file_server.config;

import com.sansung_gorogoro.file_server.infrastructure.config.UploadPolicyProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
@EnableConfigurationProperties(UploadPolicyProperties.class)
public class UploadPropertiesConfig {
}
