package com.mss.exam.portal.entity.exam;

import com.mss.exam.portal.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
 * A single answer option for an MCQ / True-False {@link Question}.
 *
 * <p>Table: {@code OPTIONS}
 */
@Entity
@Table(
        name = "OPTIONS",
        indexes = {
                @Index(name = "IDX_OPTIONS_QUESTION_ID", columnList = "QUESTION_ID")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class Option extends BaseEntity {

    @NotBlank
    @Column(name = "OPTION_TEXT", columnDefinition = "TEXT", nullable = false)
    private String optionText;

    @Column(name = "IS_CORRECT", nullable = false)
    @Builder.Default
    private boolean correct = false;

    /**
     * Optional image associated with this answer option.
     */
    @Column(name = "IMAGE_URL")
    private String imageUrl;

    @Column(name = "DISPLAY_ORDER", nullable = false)
    @Builder.Default
    private Integer displayOrder = 0;

    // ── Relationship ──────────────────────────────────────────────────────────

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "QUESTION_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_OPTIONS_QUESTION")
    )
    private Question question;
}
