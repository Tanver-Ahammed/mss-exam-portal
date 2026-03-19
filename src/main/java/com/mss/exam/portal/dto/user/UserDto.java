package com.mss.exam.portal.dto.user;

import com.mss.exam.portal.entity.enums.Role;
import com.mss.exam.portal.entity.enums.UserStatus;
import com.mss.exam.portal.entity.user.User;

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
        LocalDateTime createdAt
) {
    public static UserDto from(User user) {
        return new UserDto(
                user.getUserId(),
                user.getName(),
                user.getNameLocal(),
                user.getUsername(),
                user.getEmail(),
                user.getPhone(),
                user.getRole(),
                user.getStatus(),
                user.getCreatedAt()
        );
    }
}