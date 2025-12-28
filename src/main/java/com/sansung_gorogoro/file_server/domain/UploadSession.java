package com.sansung_gorogoro.file_server.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "upload_session")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class UploadSession {
    @Id
    @Column(name = "upload_id", length = 36, nullable = false, updatable = false)
    private String uploadId; // UUID string

    @Column(name = "declared_total_size", nullable = false)
    private Long declaredTotalSize;

    @Column(name = "original_file_name", nullable = false, length = 255)
    private String originalFileName;

    @Column(name = "next_offset", nullable = false)
    private Long nextOffset;

    @Column(name = "temp_file_path", nullable = false, length = 500)
    private String tempFilePath;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UploadSessionStatus status;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "owner_user_id", nullable = false, length = 100)
    private String ownerUserId; // from X-User-Id (gateway)

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected UploadSession (String uploadId, String ownerUserId, Long declaredTotalSize, String originalFileName, String tempFilePath, LocalDateTime expiresAt){
        this.uploadId = uploadId;
        this.ownerUserId = ownerUserId;
        this.declaredTotalSize = declaredTotalSize;
        this.originalFileName = originalFileName;
        this.status = UploadSessionStatus.UPLOADING;
        this.tempFilePath = tempFilePath;
        this.nextOffset = 0L;
        this.expiresAt = expiresAt;
    }

    public static UploadSession start(String uploadId,
                                      String ownerUserId,
                                      Long declaredTotalSize,
                                      String originalFileName,
                                      String tempFilePath,
                                      LocalDateTime expiresAt) {
        return new UploadSession(uploadId, ownerUserId, declaredTotalSize, originalFileName, tempFilePath, expiresAt);
    }
}
