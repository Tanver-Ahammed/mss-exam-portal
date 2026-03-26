package com.mss.exam.portal.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserStatus {
    PENDING("common.pending", "badge-warning"),
    ACTIVE("common.active", "badge-success"),
    INACTIVE("common.inactive", "badge-secondary"),
    TERMINATED("common.terminated", "badge-danger");

    private final String messageKey;
    private final String displayCSS;

}