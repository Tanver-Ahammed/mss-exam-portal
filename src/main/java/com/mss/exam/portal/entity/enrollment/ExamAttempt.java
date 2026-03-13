package com.mss.exam.portal.entity.enrollment;

import com.mss.exam.portal.entity.BaseEntity;
import com.mss.exam.portal.entity.exam.Answer;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents one student sitting of an {@link com.mss.exam.portal.entity.exam.Exam}.
 *
 * <p>Uses JPA optimistic locking ({@code @Version}) to prevent concurrent
 * double-submission when a student submits from two browser tabs simultaneously.
 *
 * <p>Table: {@code EXAM_ATTEMPTS}
 */
@Entity
@Table(
        name = "EXAM_ATTEMPTS",
        indexes = {
                @Index(name = "IDX_EXAM_ATTEMPTS_ENROLLMENT_ID", columnList = "ENROLLMENT_ID"),
                @Index(name = "IDX_EXAM_ATTEMPTS_STARTED_AT", columnList = "STARTED_AT")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class ExamAttempt extends BaseEntity {

    @Min(1)
    @Column(name = "ATTEMPT_NUMBER", nullable = false)
    private Integer attemptNumber;

    @Column(name = "STARTED_AT", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "SUBMITTED_AT")
    private LocalDateTime submittedAt;

    @Column(name = "SCORE")
    private Integer score;

    @Column(name = "SCORE_PERCENT")
    private Double scorePercent;

    @Column(name = "PASSED")
    private Boolean passed;

    @Column(name = "IS_SUBMITTED", nullable = false)
    @Builder.Default
    private boolean submitted = false;

    @Column(name = "IS_GRADED", nullable = false)
    @Builder.Default
    private boolean graded = false;

    @Column(name = "IP_ADDRESS", length = 45)
    private String ipAddress;

    @Column(name = "CERTIFICATE_URL")
    private String certificateUrl;

    @Version
    @Column(name = "VERSION", nullable = false)
    @Builder.Default
    private Long version = 0L;

    // ── Relationships ─────────────────────────────────────────────────────────

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "ENROLLMENT_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_EXAM_ATTEMPTS_ENROLLMENT")
    )
    private Enrollment enrollment;

    @OneToMany(mappedBy = "attempt", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Answer> answers = new ArrayList<>();

    @OneToMany(mappedBy = "attempt", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<SubmissionFile> submissionFiles = new ArrayList<>();
}
