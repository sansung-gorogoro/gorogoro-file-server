package com.sansung_gorogoro.file_server.domain;

import com.sansung_gorogoro.file_server.common.error.code.FileErrorCode;
import com.sansung_gorogoro.file_server.common.exception.BusinessException;
import com.sansung_gorogoro.file_server.common.validate.Validators;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.sansung_gorogoro.file_server.domain.constants.UploadConstraints.ORIGINAL_FILE_NAME_MAX_LEN;

@Getter
@Entity
@Table(name = "upload_session")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class UploadSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "declared_total_size", nullable = false)
    private Long declaredTotalSize;

    @Column(name = "original_file_name", nullable = false, length = ORIGINAL_FILE_NAME_MAX_LEN)
    private String originalFileName;

    @Column(name = "next_offset", nullable = false)
    private Long nextOffset;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UploadSessionStatus status;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "owner_user_id", nullable = false)
    private Long ownerUserId; // from X-User-Id (gateway)

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected UploadSession (Long ownerUserId, Long declaredTotalSize, String originalFileName, LocalDateTime expiresAt){
        this.ownerUserId = Validators.requirePositive(
                ownerUserId,
                FileErrorCode.OWNER_USER_ID_REQUIRED,
                FileErrorCode.OWNER_USER_ID_MUST_MORE_THAN_ZERO);
        this.declaredTotalSize = Validators.requirePositive(
                declaredTotalSize,
                FileErrorCode.DECLARED_TOTAL_SIZE_REQUIRED,
                FileErrorCode.DECLARED_TOTAL_SIZE_MUST_MORE_THAN_ZERO);

        this.originalFileName = Validators.validateString(
                originalFileName,
                ORIGINAL_FILE_NAME_MAX_LEN,
                FileErrorCode.ORIGINAL_FILE_NAME_REQUIRED,
                FileErrorCode.ORIGINAL_FILE_NAME_TOO_LONG
        );

        this.status = UploadSessionStatus.UPLOADING;
        this.nextOffset = 0L;
        this.expiresAt = Objects.requireNonNull(expiresAt, FileErrorCode.EXPIRES_AT_REQUIRED.getMessage());
    }

    public static UploadSession start(Long ownerUserId,
                                      Long declaredTotalSize,
                                      String originalFileName,
                                      LocalDateTime expiresAt) {
        return new UploadSession(ownerUserId, declaredTotalSize, originalFileName, expiresAt);
    }

    public void assertExpectedOffset(Long uploadOffset) {
        if (!uploadOffset.equals(this.nextOffset)) {
            throw BusinessException.builder(FileErrorCode.UPLOAD_SESSION_OFFSET_MISMATCH).build();
        }
    }

    public void assertOwner(Long requesterUserId) {
        if (requesterUserId == null) {
            throw BusinessException.builder(FileErrorCode.OWNER_USER_ID_REQUIRED).build();
        }

        if (!Objects.equals(this.ownerUserId, requesterUserId)) {
            throw BusinessException.builder(FileErrorCode.UPLOAD_SESSION_ACCESS_FORBIDDEN).build();
        }
    }

    public void applyChunkWritten(long written) {
        if (!status.equals(UploadSessionStatus.UPLOADING)) {
            throw BusinessException.builder(FileErrorCode.UPLOAD_SESSION_STATUS_NOT_ACTIVE).build();
        }

        if (written <= 0) {
            throw BusinessException.builder(FileErrorCode.UPLOAD_CHUNK_WRITE_FAILED).build();
        }

        Long newNextOffset = this.nextOffset + written;
        if (newNextOffset > this.declaredTotalSize) {
            throw BusinessException.builder(FileErrorCode.UPLOAD_OFFSET_EXCEEDS_DECLARED_TOTAL_SIZE).build();
        }

        advanceTo(newNextOffset);
    }

    private void advanceTo(Long newNextOffset) {
        this.nextOffset = newNextOffset;
    }
}
