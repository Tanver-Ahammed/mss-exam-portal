package com.mss.exam.portal.dto.user;

import com.mss.exam.portal.entity.enums.Role;
import com.mss.exam.portal.entity.user.User;

import java.time.LocalDateTime;

public record UserDto(
        Long id,
        String name,
        String nameLocal,
        String username,
        String email,
        String phone,
        Role role,
        boolean active,
        LocalDateTime createdAt
) {
    public static UserDto from(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getNameLocal(),
                user.getUsername(),
                user.getEmail(),
                user.getPhone(),
                user.getRole(),
                user.isActive(),
                user.getCreatedAt()
        );
    }
}