package com.sansung_gorogoro.file_server.domain;

import com.sansung_gorogoro.file_server.common.error.code.FileErrorCode;
import com.sansung_gorogoro.file_server.common.validate.Validators;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

import static com.sansung_gorogoro.file_server.domain.constants.UploadConstraints.FILE_KEY_MAX_LEN;

@Getter
@Entity
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_key", nullable = false, length = FILE_KEY_MAX_LEN)
    private String fileKey;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ResourceStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ResourceType resourceType;

    @Column(name = "attached_lesson_id", nullable = false)
    private Long attachedLessonId;

    protected Resource (String fileKey, ResourceType resourceType, Long lessonId) {
        this.fileKey = fileKey;
        this.resourceType = resourceType;
        this.attachedLessonId = lessonId;
        this.status = ResourceStatus.READY;
    }

    public static Resource start(String fileKey, ResourceType resourceType, Long lessonId) {
        return new Resource(
                Validators.validateString(
                        fileKey,
                        FILE_KEY_MAX_LEN,
                        FileErrorCode.FILE_KEY_REQUIRED,
                        FileErrorCode.FILE_KEY_TOO_LONG),
                Objects.requireNonNull(
                        resourceType,
                        FileErrorCode.RESOURCE_TYPE_RESOLVE_FAILED.getMessage()),
                Validators.requirePositive(lessonId,
                        FileErrorCode.LESSON_ID_REQUIRED,
                        FileErrorCode.LESSON_ID_MUST_MORE_THAN_ZERO));
    }
}