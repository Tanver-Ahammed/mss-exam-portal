package com.mss.exam.portal.entity.exam;

import com.mss.exam.portal.entity.BaseEntity;
import com.mss.exam.portal.entity.enrollment.Attempt;
import com.mss.exam.portal.entity.enrollment.SubmissionFile;
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
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Records a student's response to one {@link Question} within an {@link Attempt}.
 *
 * <ul>
 *   <li><b>MCQ</b>           — {@code selectedOption} is populated; text fields are null.</li>
 *   <li><b>SHORT_ANSWER</b>  — {@code textAnswer} is populated; auto-graded by keyword match.</li>
 *   <li><b>LONG_ANSWER /
 *       WRITTEN</b>          — {@code submissionFiles} contains the uploaded PDFs/images;
 *                              {@code marksAwarded} is set manually by an examiner.</li>
 * </ul>
 *
 * <p>Table: {@code ANSWERS}
 */
@Entity
@Table(
    name = "ANSWERS",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "UQ_ANSWERS_ATTEMPT_QUESTION",
            columnNames = {"ATTEMPT_ID", "QUESTION_ID"}
        )
    },
    indexes = {
        @Index(name = "IDX_ANSWERS_ATTEMPT_ID",  columnList = "ATTEMPT_ID"),
        @Index(name = "IDX_ANSWERS_QUESTION_ID", columnList = "QUESTION_ID")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class Answer extends BaseEntity {

    /** Free-text response for SHORT_ANSWER questions. */
    @Column(name = "TEXT_ANSWER", columnDefinition = "TEXT")
    private String textAnswer;

    /** Marks awarded by auto-grader (MCQ) or examiner (WRITTEN). */
    @Column(name = "MARKS_AWARDED")
    private Integer marksAwarded;

    /** {@code true} = correct, {@code false} = wrong, {@code null} = not yet graded. */
    @Column(name = "IS_CORRECT")
    private Boolean correct;

    /** Examiner feedback shown to student after grading is released. */
    @Column(name = "EXAMINER_FEEDBACK", columnDefinition = "TEXT")
    private String examinerFeedback;

    /** Flagged by student for review before final submission. */
    @Column(name = "IS_FLAGGED", nullable = false)
    @Builder.Default
    private boolean flagged = false;

    // ── Relationships ─────────────────────────────────────────────────────────

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "ATTEMPT_ID",
        nullable = false,
        foreignKey = @ForeignKey(name = "FK_ANSWERS_ATTEMPT")
    )
    private Attempt attempt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "QUESTION_ID",
        nullable = false,
        foreignKey = @ForeignKey(name = "FK_ANSWERS_QUESTION")
    )
    private Question question;

    /** Selected MCQ option — {@code null} for text-based and written answers. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "SELECTED_OPTION_ID",
        foreignKey = @ForeignKey(name = "FK_ANSWERS_OPTION")
    )
    private Option selectedOption;

    /**
     * PDF / image files uploaded for this written-exam answer.
     * Empty list for MCQ and short-answer questions.
     * Ordered by {@code pageNumber ASC} in the service layer.
     */
    @OneToMany(mappedBy = "answer", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<SubmissionFile> submissionFiles = new ArrayList<>();
}
