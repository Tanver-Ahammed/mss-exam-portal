package com.mss.exam.portal.entity.enums;

/**
 * System-wide user roles enforced by Spring Security {@code @PreAuthorize}.
 */
public enum Role {
    ROLE_ADMIN,
    ROLE_INSTRUCTOR,
    ROLE_EXAMINER,
    ROLE_STUDENT
}
