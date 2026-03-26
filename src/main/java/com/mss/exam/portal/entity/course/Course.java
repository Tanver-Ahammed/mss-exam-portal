package com.mss.exam.portal.entity.course;

import com.mss.exam.portal.entity.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
@EqualsAndHashCode
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COURSE_ID", updatable = false, nullable = false)
    private Long courseId;

    @NotBlank
    @Size(max = 200)
    @Column(name = "TITLE", nullable = false, length = 200)
    private String title;

    @NotBlank
    @Size(max = 200)
    @Column(name = "TITLE_LOCAL", nullable = false, length = 200)
    private String titleLocal;

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

    @CreatedDate
    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @CreatedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "CREATED_BY_USER_ID",
            updatable = false,
            foreignKey = @ForeignKey(name = "FK_AUDIT_CREATED_BY_USER")
    )
    private User createdBy;

    @LastModifiedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "UPDATED_BY_USER_ID",
            foreignKey = @ForeignKey(name = "FK_AUDIT_UPDATED_BY_USER")
    )
    private User updatedBy;

    @Column(name = "IS_DELETED", nullable = false)
    private boolean deleted = false;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "COURSE_CATEGORY_MAPPINGS",
            joinColumns = @JoinColumn(
                    name = "COURSE_ID",
                    foreignKey = @ForeignKey(name = "FK_COURSE_CATEGORY_MAPPINGS_COURSE")
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "CATEGORY_ID",
                    foreignKey = @ForeignKey(name = "FK_COURSE_CATEGORY_MAPPINGS_CATEGORY")
            )
    )
    @Builder.Default
    private List<Category> categories = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Batch> batches = new ArrayList<>();
}
