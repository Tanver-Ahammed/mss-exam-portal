package com.mss.exam.portal.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an academic / training course.
 * A course contains one or more {@link Batch}es.
 *
 * <p>Table: {@code COURSES}
 */
@Entity
@Table(
    name = "COURSES",
    indexes = {
        @Index(name = "IDX_COURSES_CODE",       columnList = "CODE"),
        @Index(name = "IDX_COURSES_CREATED_BY", columnList = "CREATED_BY_ID")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course extends BaseEntity {

    @NotBlank
    @Size(max = 200)
    @Column(name = "TITLE", nullable = false, length = 200)
    private String title;

    /** Short unique code, e.g. {@code JAVA-SPRING-01}. */
    @NotBlank
    @Size(max = 50)
    @Column(name = "CODE", nullable = false, unique = true, length = 50)
    private String code;

    @Column(name = "DESCRIPTION", columnDefinition = "TEXT")
    private String description;

    @Column(name = "THUMBNAIL_URL")
    private String thumbnailUrl;

    @Column(name = "IS_ACTIVE", nullable = false)
    @Builder.Default
    private boolean active = true;

    // ── Relationships ─────────────────────────────────────────────────────────

    /** Instructor who owns the course. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "CREATED_BY_ID",
        nullable = false,
        foreignKey = @ForeignKey(name = "FK_COURSES_CREATED_BY")
    )
    private User createdBy;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Batch> batches = new ArrayList<>();
}
