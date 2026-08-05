package com.halukkilincer.identity.dto;

import com.halukkilincer.identity.entity.Role;

import java.time.Instant;

public record UserResponse(
        String id,
        String username,
        String email,
        String firstName,
        String lastName,
        Role role,
        boolean enabled,
        Instant createdAt
) {
}
