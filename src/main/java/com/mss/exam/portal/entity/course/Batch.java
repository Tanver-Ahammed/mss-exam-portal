package com.mss.exam.portal.entity.course;

import com.mss.exam.portal.entity.BaseEntity;
import com.mss.exam.portal.entity.enrollment.Enrollment;
import com.mss.exam.portal.entity.enums.BatchStatus;
import com.mss.exam.portal.entity.exam.Exam;
import com.mss.exam.portal.entity.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a cohort of students enrolled under a {@link Course}.
 *
 * <p>One batch can be linked to multiple {@link Exam}s so that a single
 * CSV upload enrolls the whole cohort into every exam in that batch.
 * One {@link Enrollment} row is created per (student × exam) pair.
 *
 * <p>Table:      {@code BATCHES}
 * <p>Join table: {@code BATCH_EXAMS}
 */
@Entity
@Table(
        name = "BATCHES",
        indexes = {
                @Index(name = "IDX_BATCHES_COURSE_ID", columnList = "COURSE_ID"),
                @Index(name = "IDX_BATCHES_STATUS", columnList = "STATUS")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class Batch extends BaseEntity {

    @NotBlank
    @Column(name = "NAME", nullable = false, length = 150)
    private String name;

    @NotBlank
    @Column(name = "NAME_LOCAL", nullable = false, length = 150)
    private String nameLocal;

    @Column(name = "CSV_FILENAME", nullable = false)
    private String csvFilename;

    @Column(name = "S3_OBJECT_KEY")
    private String s3ObjectKey;

    @Column(name = "TOTAL_ROWS", nullable = false)
    @Builder.Default
    private Integer totalRows = 0;

    @Column(name = "ENROLLED_COUNT", nullable = false)
    @Builder.Default
    private Integer enrolledCount = 0;

    @Column(name = "FAILED_COUNT", nullable = false)
    @Builder.Default
    private Integer failedCount = 0;

    @Column(name = "SKIPPED_COUNT", nullable = false)
    @Builder.Default
    private Integer skippedCount = 0;

    @DecimalMin("0.0")
    @Max(100)
    @Column(name = "DISCOUNT", precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal discount = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 20)
    @Builder.Default
    private BatchStatus status = BatchStatus.UPLOADED;

    /**
     * JSON array of per-row error messages for failed rows.
     */
    @Column(name = "ERROR_LOG", columnDefinition = "TEXT")
    private String errorLog;

    // ── Relationships ─────────────────────────────────────────────────────────

    /**
     * The course this batch belongs to.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "COURSE_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_BATCHES_COURSE")
    )
    private Course course;

    @ManyToMany
    @JoinTable(
            name = "BATCH_INSTRUCTORS",
            joinColumns = @JoinColumn(name = "BATCH_ID"),
            inverseJoinColumns = @JoinColumn(name = "USER_ID")
    )
    @Builder.Default
    private List<User> instructedBy = new ArrayList<>();

    /**
     * The exams students in this batch are enrolled into.
     * Join table: {@code BATCH_EXAMS (BATCH_ID, EXAM_ID)}.
     */
    @ManyToMany
    @JoinTable(
            name = "BATCH_EXAMS",
            joinColumns = @JoinColumn(name = "BATCH_ID",
                    foreignKey = @ForeignKey(name = "FK_BATCH_EXAMS_BATCH")),
            inverseJoinColumns = @JoinColumn(name = "EXAM_ID",
                    foreignKey = @ForeignKey(name = "FK_BATCH_EXAMS_EXAM"))
    )
    @Builder.Default
    private List<Exam> exams = new ArrayList<>();

    @OneToMany(mappedBy = "batch", cascade = CascadeType.ALL, orphanRemoval = false)
    @Builder.Default
    private List<Enrollment> enrollments = new ArrayList<>();
}
