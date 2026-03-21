package com.mss.exam.portal.dto.user;

import com.mss.exam.portal.entity.enums.Role;
import com.mss.exam.portal.entity.enums.UserStatus;

import java.time.LocalDateTime;

public record UserDto(
        Long userId,
        String name,
        String nameLocal,
        String username,
        String email,
        String phone,
        Role role,
        UserStatus status,
        String divisionName,
        String divisionNameLocal,
        String districtName,
        String districtNameLocal,
        String upazilaName,
        String upazilaNameLocal,
        LocalDateTime createdAt
) {
}