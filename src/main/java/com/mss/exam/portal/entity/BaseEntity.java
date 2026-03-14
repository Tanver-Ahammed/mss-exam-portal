package com.mss.exam.portal.entity;

import com.mss.exam.portal.entity.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Abstract base entity — Long primary key + full Spring Data audit columns.
 * All domain entities must extend this class.
 *
 * <p>{@code CREATED_BY_USER_ID} and {@code UPDATED_BY_USER_ID} hold a direct
 * {@link User} FK so every row carries a traceable author reference
 * instead of a plain username string.
 *
 * <p>Wire this up with a custom {@code AuditorAware<User>} bean that
 * returns the currently authenticated {@link User} from the
 * Spring Security context:
 * <pre>{@code
 * @Bean
 * public AuditorAware<User> auditorAware() {
 *     return () -> Optional.ofNullable(SecurityContextHolder.getContext()
 *             .getAuthentication())
 *             .filter(Authentication::isAuthenticated)
 *             .map(Authentication::getPrincipal)
 *             .filter(p -> p instanceof User)
 *             .map(User.class::cast);
 * }
 * }</pre>
 *
 * <p>Enable auditing in your main class with {@code @EnableJpaAuditing}.
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", updatable = false, nullable = false)
    private Long id;

    @CreatedDate
    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    /**
     * The {@link User} who created this record.
     * Populated automatically by Spring Data auditing via {@code AuditorAware<User>}.
     * Not updatable after first insert.
     */
    @CreatedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "CREATED_BY_USER_ID",
        updatable = false,
        foreignKey = @ForeignKey(name = "FK_AUDIT_CREATED_BY_USER")
    )
    private User createdBy;

    /**
     * The {@link User} who last modified this record.
     * Updated on every save by Spring Data auditing.
     */
    @LastModifiedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "UPDATED_BY_USER_ID",
        foreignKey = @ForeignKey(name = "FK_AUDIT_UPDATED_BY_USER")
    )
    private User updatedBy;

    @Column(name = "IS_DELETED", nullable = false)
    private boolean deleted = false;
}
