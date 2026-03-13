package com.mss.exam.portal.entity.user;

import com.mss.exam.portal.entity.BaseEntity;
import com.mss.exam.portal.entity.course.Course;
import com.mss.exam.portal.entity.enrollment.Enrollment;
import com.mss.exam.portal.entity.enums.Role;
import com.mss.exam.portal.entity.exam.Exam;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a system user (admin, instructor, examiner, or student).
 * Loaded by Spring Security via {@code UserDetailsService}.
 *
 * <p>Table: {@code USERS}
 */
@Entity
@Table(
        name = "USERS",
        uniqueConstraints = {
                @UniqueConstraint(name = "UQ_USERS_EMAIL", columnNames = "EMAIL"),
                @UniqueConstraint(name = "UQ_USERS_USERNAME", columnNames = "USERNAME")
        },
        indexes = {
                @Index(name = "IDX_USERS_EMAIL", columnList = "EMAIL"),
                @Index(name = "IDX_USERS_ROLE", columnList = "ROLE")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {

    @NotBlank
    @Size(min = 3, max = 50)
    @Column(name = "USERNAME", nullable = false, length = 50)
    private String username;

    @NotBlank
    @Email
    @Column(name = "EMAIL", nullable = false, length = 120)
    private String email;

    @NotBlank
    @Column(name = "PASSWORD_HASH", nullable = false)
    private String passwordHash;

    @Column(name = "FIRST_NAME", length = 80)
    private String firstName;

    @Column(name = "LAST_NAME", length = 80)
    private String lastName;

    @Column(name = "PHONE", length = 20)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE", nullable = false, length = 30)
    private Role role;

    @Column(name = "IS_ACTIVE", nullable = false)
    @Builder.Default
    private boolean active = true;

    // ── Relationships ─────────────────────────────────────────────────────────

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<UserFile> userFiles = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Enrollment> enrollments = new ArrayList<>();

    @ManyToMany(mappedBy = "instructedBy")
    @Builder.Default
    private List<Course> instructedCourses = new ArrayList<>();

    @ManyToMany(mappedBy = "conductedBy")
    @Builder.Default
    private List<Exam> conductedExams = new ArrayList<>();
}
