package com.mss.exam.portal.entity.exam;

import com.mss.exam.portal.entity.BaseEntity;
import com.mss.exam.portal.entity.enums.QuestionType;
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
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * A single question belonging to an {@link Exam}.
 *
 * <p>Table: {@code QUESTIONS}
 */
@Entity
@Table(
    name = "QUESTIONS",
    indexes = {
        @Index(name = "IDX_QUESTIONS_EXAM_ID",  columnList = "EXAM_ID"),
        @Index(name = "IDX_QUESTIONS_ORDER_NO", columnList = "ORDER_NO")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class Question extends BaseEntity {

    @NotBlank
    @Column(name = "QUESTION_TEXT", columnDefinition = "TEXT", nullable = false)
    private String questionText;

    @Enumerated(EnumType.STRING)
    @Column(name = "QUESTION_TYPE", nullable = false, length = 20)
    private QuestionType questionType;

    @Min(1)
    @Column(name = "MARKS", nullable = false)
    @Builder.Default
    private Integer marks = 1;

    @Column(name = "ORDER_NO", nullable = false)
    @Builder.Default
    private Integer orderNo = 0;

    @Column(name = "EXPLANATION", columnDefinition = "TEXT")
    private String explanation;

    /** Comma-separated accepted keywords used for auto-grading SHORT_ANSWER questions. */
    @Column(name = "ANSWER_KEYWORDS", columnDefinition = "TEXT")
    private String answerKeywords;

    @Column(name = "IMAGE_URL")
    private String imageUrl;

    @Column(name = "IS_MANDATORY", nullable = false)
    @Builder.Default
    private boolean mandatory = true;

    // ── Relationships ─────────────────────────────────────────────────────────

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "EXAM_ID",
        nullable = false,
        foreignKey = @ForeignKey(name = "FK_QUESTIONS_EXAM")
    )
    private Exam exam;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("displayOrder ASC")
    @Builder.Default
    private List<Option> options = new ArrayList<>();

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Answer> answers = new ArrayList<>();
}
