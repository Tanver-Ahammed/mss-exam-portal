package com.mss.exam.portal.entity.course;

import com.mss.exam.portal.entity.BaseEntity;
import com.mss.exam.portal.entity.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
 * Represents an academic / training course.
 * A course contains one or more {@link Batch}es.
 *
 * <p>Table: {@code COURSES}
 */
@Entity
@Table(
        name = "COURSES",
        indexes = {
                @Index(name = "IDX_COURSES_CODE", columnList = "CODE")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class Course extends BaseEntity {

    @NotBlank
    @Size(max = 200)
    @Column(name = "TITLE", nullable = false, length = 200)
    private String title;

    /**
     * Short unique code, e.g. {@code JAVA-SPRING-01}.
     */
    @NotBlank
    @Size(max = 50)
    @Column(name = "CODE", nullable = false, unique = true, length = 50)
    private String code;

    @Column(name = "DESCRIPTION", columnDefinition = "TEXT")
    private String description;

    @Column(name = "THUMBNAIL_URL")
    private String thumbnailUrl;

    @DecimalMin("0.0")
    @Column(name = "COURSE_FEE", precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal courseFee = BigDecimal.ZERO;

    @Column(name = "IS_ACTIVE", nullable = false)
    @Builder.Default
    private boolean active = true;

    @ManyToMany
    @JoinTable(
            name = "COURSE_CATEGORY_MAPPINGS",
            joinColumns = @JoinColumn(name = "COURSE_ID"),
            inverseJoinColumns = @JoinColumn(name = "CATEGORY_ID")
    )
    @Builder.Default
    private List<CourseCategory> categories = new ArrayList<>();

    // ── Relationships ─────────────────────────────────────────────────────────

    @ManyToMany
    @JoinTable(
            name = "COURSE_INSTRUCTORS",
            joinColumns = @JoinColumn(name = "COURSE_ID"),
            inverseJoinColumns = @JoinColumn(name = "USER_ID")
    )
    @Builder.Default
    private List<User> instructedBy = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Batch> batches = new ArrayList<>();
}
