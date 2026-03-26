package com.mss.exam.portal.dto.user;

import com.mss.exam.portal.entity.enums.UserStatus;

public record UserFilter(
        Long userId,
        String omniSearch,
        UserStatus status,
        Long divisionId,
        Long districtId,
        Long upazilaId
) {
}
