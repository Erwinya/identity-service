package com.halukkilincer.identity.mapper;

import com.halukkilincer.identity.dto.UserResponse;
import com.halukkilincer.identity.entity.Role;
import com.halukkilincer.identity.entity.User;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    @Test
    void toResponse_mapsAllFields() {
        Instant createdAt = Instant.parse("2026-01-01T00:00:00Z");
        User user = User.builder()
                .id("id-1")
                .username("haluk")
                .email("haluk@example.com")
                .passwordHash("hash")
                .firstName("Haluk")
                .lastName("Kilincer")
                .role(Role.USER)
                .enabled(true)
                .createdAt(createdAt)
                .updatedAt(createdAt)
                .build();

        UserResponse response = UserMapper.toResponse(user);

        assertThat(response.id()).isEqualTo("id-1");
        assertThat(response.username()).isEqualTo("haluk");
        assertThat(response.email()).isEqualTo("haluk@example.com");
        assertThat(response.firstName()).isEqualTo("Haluk");
        assertThat(response.lastName()).isEqualTo("Kilincer");
        assertThat(response.role()).isEqualTo(Role.USER);
        assertThat(response.enabled()).isTrue();
        assertThat(response.createdAt()).isEqualTo(createdAt);
    }
}
