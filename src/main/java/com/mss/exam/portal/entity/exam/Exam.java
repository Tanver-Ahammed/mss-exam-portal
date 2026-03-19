package com.mss.exam.portal.entity.exam;

import com.mss.exam.portal.entity.BaseEntity;
import com.mss.exam.portal.entity.enrollment.Enrollment;
import com.mss.exam.portal.entity.enrollment.ExamAttempt;
import com.mss.exam.portal.entity.enums.ExamType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a schedulable exam with a question bank.
 *
 * <h3>Time window semantics</h3>
 * <ul>
 *   <li>{@code EXAM_START_TIME} / {@code EXAM_END_TIME} — the daily clock window
 *       during which students may begin the exam (e.g. 18:00 – 22:00).</li>
 *   <li>{@code DURATION_MINUTES} — the maximum sitting time once a student
 *       starts (e.g. 60 min).  The attempt auto-submits when elapsed.</li>
 * </ul>
 *
 * <p>Table: {@code EXAMS}
 */
@Entity
@Table(
        name = "EXAMS",
        indexes = {
                @Index(name = "IDX_EXAMS_EXAM_TYPE", columnList = "EXAM_TYPE"),
                @Index(name = "IDX_EXAMS_START_TIME", columnList = "EXAM_START_TIME")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class Exam extends BaseEntity {

    @NotBlank
    @Size(max = 200)
    @Column(name = "TITLE", nullable = false, length = 200)
    private String title;

    @NotBlank
    @Size(max = 200)
    @Column(name = "TITLE_LOCAL", nullable = false, length = 200)
    private String titleLocal;

    @Column(name = "DESCRIPTION", columnDefinition = "TEXT")
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "EXAM_TYPE", nullable = false, length = 20)
    private ExamType examType;

    // ── Time window ───────────────────────────────────────────────────────────

    /**
     * Clock time when the exam window opens each day, e.g. {@code 18:00}.
     * Combined with the batch/schedule date to form the full start timestamp.
     */
    @NotNull
    @Column(name = "EXAM_START_TIME", nullable = false)
    private LocalTime examStartTime;

    /**
     * Clock time when the exam window closes, e.g. {@code 22:00}.
     * Students cannot begin a new attempt after this time.
     */
    @NotNull
    @Column(name = "EXAM_END_TIME", nullable = false)
    private LocalTime examEndTime;

    /**
     * Maximum sitting duration in minutes once an attempt is started.
     * Must be ≤ the window length (endTime − startTime).
     * The attempt auto-submits when this duration is exhausted.
     */
    @Min(10)
    @Max(600)
    @Column(name = "DURATION_MINUTES", nullable = false)
    private Integer durationMinutes;

    // ── Grading ───────────────────────────────────────────────────────────────

    @Min(0)
    @Max(100)
    @Column(name = "PASS_MARK_PERCENT", nullable = false)
    private Integer passMarkPercent;

    @Column(name = "TOTAL_MARKS", nullable = false)
    @Builder.Default
    private Integer totalMarks = 0;

    // ── Attempt policy ────────────────────────────────────────────────────────

    @Min(1)
    @Column(name = "MAX_ATTEMPTS", nullable = false)
    @Builder.Default
    private Integer maxAttempts = 1;

    @Column(name = "IS_PUBLISHED", nullable = false)
    @Builder.Default
    private boolean published = false;

    @Column(name = "IS_RANDOMIZE_QUESTIONS", nullable = false)
    @Builder.Default
    private boolean randomizeQuestions = false;

    @Column(name = "IS_SHOW_RESULT_IMMEDIATELY", nullable = false)
    @Builder.Default
    private boolean showResultImmediately = true;

    // ── Fees & certificates ───────────────────────────────────────────────────

    @DecimalMin("0.0")
    @Column(name = "ENROLLMENT_FEE", precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal enrollmentFee = BigDecimal.ZERO;

    @Column(name = "CERTIFICATE_TEMPLATE_URL")
    private String certificateTemplateUrl;

    // ── Relationships ─────────────────────────────────────────────────────────

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "EXAM_CONDUCTORS",
            joinColumns = @JoinColumn(
                    name = "EXAM_ID",
                    foreignKey = @ForeignKey(name = "FK_EXAM_CONDUCTORS_EXAM")
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "USER_ID",
                    foreignKey = @ForeignKey(name = "FK_EXAM_CONDUCTORS_USER")
            )
    )
    @Builder.Default
    private List<User> conductedBy = new ArrayList<>();

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("orderNo ASC")
    @Builder.Default
    private List<Question> questions = new ArrayList<>();

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Enrollment> enrollments = new ArrayList<>();

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ExamAttempt> examAttempts = new ArrayList<>();
}
