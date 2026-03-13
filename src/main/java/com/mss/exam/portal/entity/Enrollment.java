package com.mss.exam.portal.entity;

import com.mss.exam.portal.entity.enums.EnrollmentStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
 * Junction between a {@link User} (student) and an {@link Exam}.
 * Controls access, caps attempt count, and gates the {@link Payment}.
 *
 * <p>Table: {@code ENROLLMENTS}
 */
@Entity
@Table(
    name = "ENROLLMENTS",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "UQ_ENROLLMENT_STUDENT_EXAM",
            columnNames = {"STUDENT_ID", "EXAM_ID"}
        )
    },
    indexes = {
        @Index(name = "IDX_ENROLLMENTS_STUDENT_ID", columnList = "STUDENT_ID"),
        @Index(name = "IDX_ENROLLMENTS_EXAM_ID",    columnList = "EXAM_ID"),
        @Index(name = "IDX_ENROLLMENTS_BATCH_ID",   columnList = "BATCH_ID"),
        @Index(name = "IDX_ENROLLMENTS_STATUS",     columnList = "STATUS")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class Enrollment extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 25)
    @Builder.Default
    private EnrollmentStatus status = EnrollmentStatus.PENDING_PAYMENT;

    @Column(name = "ATTEMPTS_USED", nullable = false)
    @Builder.Default
    private Integer attemptsUsed = 0;

    @Column(name = "ENROLLED_AT")
    private LocalDateTime enrolledAt;

    @Column(name = "EXPIRES_AT")
    private LocalDateTime expiresAt;

    /** {@code true} when fee is waived (scholarship / admin override). */
    @Column(name = "IS_FEE_WAIVED", nullable = false)
    @Builder.Default
    private boolean feeWaived = false;

    // ── Relationships ─────────────────────────────────────────────────────────

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "STUDENT_ID",
        nullable = false,
        foreignKey = @ForeignKey(name = "FK_ENROLLMENTS_STUDENT")
    )
    private User student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "EXAM_ID",
        nullable = false,
        foreignKey = @ForeignKey(name = "FK_ENROLLMENTS_EXAM")
    )
    private Exam exam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "BATCH_ID",
        foreignKey = @ForeignKey(name = "FK_ENROLLMENTS_BATCH")
    )
    private Batch batch;

    @OneToMany(mappedBy = "enrollment", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Attempt> attempts = new ArrayList<>();

    /** One enrollment has one payment record (which can have many transactions). */
    @OneToOne(mappedBy = "enrollment", cascade = CascadeType.ALL,
              orphanRemoval = true, fetch = FetchType.LAZY)
    private Payment payment;
}
