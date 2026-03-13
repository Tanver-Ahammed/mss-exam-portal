package com.mss.exam.portal.entity;

import com.mss.exam.portal.entity.enums.FileType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Stores metadata for every file belonging to a {@link User}.
 * Binary content lives in S3/MinIO; only the object key is persisted here.
 *
 * <p>One user may have many files (e.g. multiple historical profile pictures)
 * but only one {@code PROFILE_PICTURE} should have {@code IS_ACTIVE = true}
 * at any time — enforced in {@code UserFileService}.
 *
 * <p>Table: {@code USER_FILES}
 */
@Entity
@Table(
    name = "USER_FILES",
    indexes = {
        @Index(name = "IDX_USER_FILES_USER_ID",   columnList = "USER_ID"),
        @Index(name = "IDX_USER_FILES_FILE_TYPE",  columnList = "FILE_TYPE"),
        @Index(name = "IDX_USER_FILES_IS_ACTIVE",  columnList = "IS_ACTIVE")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class UserFile extends BaseEntity {

    /** S3 / MinIO object key, e.g. {@code users/{uuid}/profile/avatar.jpg}. */
    @NotBlank
    @Column(name = "S3_OBJECT_KEY", nullable = false)
    private String s3ObjectKey;

    /** Public CDN URL or pre-signed URL cached at upload time. */
    @NotBlank
    @Column(name = "FILE_URL", nullable = false)
    private String fileUrl;

    @NotBlank
    @Column(name = "ORIGINAL_FILENAME", nullable = false, length = 255)
    private String originalFilename;

    /** MIME type — e.g. {@code image/jpeg}, {@code image/png}. */
    @Column(name = "CONTENT_TYPE", nullable = false, length = 100)
    private String contentType;

    /** File size in bytes. */
    @Column(name = "FILE_SIZE_BYTES")
    private Long fileSizeBytes;

    @Enumerated(EnumType.STRING)
    @Column(name = "FILE_TYPE", nullable = false, length = 30)
    private FileType fileType;

    @Column(name = "IS_ACTIVE", nullable = false)
    @Builder.Default
    private boolean active = true;

    // ── Relationship ──────────────────────────────────────────────────────────

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "USER_ID",
        nullable = false,
        foreignKey = @ForeignKey(name = "FK_USER_FILES_USER")
    )
    private User user;
}
