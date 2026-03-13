package com.mss.exam.portal.entity.enrollment;

import com.mss.exam.portal.entity.BaseEntity;
import com.mss.exam.portal.entity.enums.FileType;
import com.mss.exam.portal.entity.exam.Answer;
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
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents one file (PDF or scanned image) uploaded by a student as part
 * of a written-exam {@link Answer}.
 *
 * <p>A student may upload multiple pages per answer (e.g. a multi-page
 * handwritten solution scanned as individual images). The {@code PAGE_NUMBER}
 * field orders them for the examiner's review queue.
 *
 * <p>Applicable only when {@link com.mss.exam.portal.entity.enums.ExamType}
 * is {@code WRITTEN} or {@code MIXED} and the question type is
 * {@code LONG_ANSWER}.
 *
 * <p>Table: {@code SUBMISSION_FILES}
 */
@Entity
@Table(
    name = "SUBMISSION_FILES",
    indexes = {
        @Index(name = "IDX_SUB_FILES_ANSWER_ID",  columnList = "ANSWER_ID"),
        @Index(name = "IDX_SUB_FILES_ATTEMPT_ID", columnList = "ATTEMPT_ID"),
        @Index(name = "IDX_SUB_FILES_FILE_TYPE",  columnList = "FILE_TYPE")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class SubmissionFile extends BaseEntity {

    /**
     * S3 / MinIO object key, e.g.
     * {@code submissions/{attempt-uuid}/q3/page_1.pdf}
     */
    @NotBlank
    @Column(name = "S3_OBJECT_KEY", nullable = false)
    private String s3ObjectKey;

    /** Accessible download URL (CDN or pre-signed S3 URL). */
    @NotBlank
    @Column(name = "FILE_URL", nullable = false)
    private String fileUrl;

    @NotBlank
    @Column(name = "ORIGINAL_FILENAME", nullable = false, length = 255)
    private String originalFilename;

    /**
     * MIME type. Accepted values:
     * {@code application/pdf}, {@code image/jpeg},
     * {@code image/png}, {@code image/webp}, {@code image/tiff}.
     */
    @Column(name = "CONTENT_TYPE", nullable = false, length = 100)
    private String contentType;

    /** File size in bytes — service layer rejects files larger than 20 MB. */
    @Column(name = "FILE_SIZE_BYTES")
    private Long fileSizeBytes;

    /** {@code SUBMISSION_PDF} or {@code SUBMISSION_IMAGE}. */
    @Enumerated(EnumType.STRING)
    @Column(name = "FILE_TYPE", nullable = false, length = 30)
    private FileType fileType;

    /**
     * 1-based sequence number within this answer's upload set.
     * Examiner views files ordered ascending by this value.
     */
    @Min(1)
    @Column(name = "PAGE_NUMBER", nullable = false)
    @Builder.Default
    private Integer pageNumber = 1;

    /**
     * Set to {@code true} once the examiner has opened this file.
     * Drives the "unreviewed uploads" counter in the grading dashboard.
     */
    @Column(name = "IS_REVIEWED", nullable = false)
    @Builder.Default
    private boolean reviewed = false;

    /** Thumbnail URL generated server-side after upload (image types only). */
    @Column(name = "THUMBNAIL_URL")
    private String thumbnailUrl;

    // ── Relationships ─────────────────────────────────────────────────────────

    /** The specific answer this file belongs to. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "ANSWER_ID",
        nullable = false,
        foreignKey = @ForeignKey(name = "FK_SUB_FILES_ANSWER")
    )
    private Answer answer;

    /**
     * Redundant FK to {@link Attempt} — enables a single
     * "give me all files for this sitting" query without joining through answers.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "ATTEMPT_ID",
        nullable = false,
        foreignKey = @ForeignKey(name = "FK_SUB_FILES_ATTEMPT")
    )
    private Attempt attempt;
}
