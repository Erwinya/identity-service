package com.halukkilincer.identity.service;

import com.halukkilincer.identity.dto.RegisterRequest;
import com.halukkilincer.identity.dto.UserResponse;
import com.halukkilincer.identity.entity.Role;
import com.halukkilincer.identity.entity.User;
import com.halukkilincer.identity.exception.ConflictException;
import com.halukkilincer.identity.exception.NotFoundException;
import com.halukkilincer.identity.repository.UserRepository;
import com.halukkilincer.identity.security.JwtService;
import com.halukkilincer.identity.service.impl.AuthServiceImpl;
import com.halukkilincer.identity.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthAndUserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void register_persistsUserAndReturnsToken() {
        RegisterRequest request = new RegisterRequest(
                "haluk",
                "haluk@example.com",
                "Secret123!",
                "Haluk",
                "Kilincer"
        );

        when(userRepository.existsByUsername("haluk")).thenReturn(false);
        when(userRepository.existsByEmail("haluk@example.com")).thenReturn(false);
        when(passwordEncoder.encode("Secret123!")).thenReturn("encoded");
        when(jwtService.getExpirationMs()).thenReturn(3600000L);
        when(jwtService.generateToken(any())).thenReturn("jwt-token");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId("generated-id");
            return user;
        });

        var response = authService.register(request);

        assertThat(response.accessToken()).isEqualTo("jwt-token");
        assertThat(response.tokenType()).isEqualTo("Bearer");
        assertThat(response.user().username()).isEqualTo("haluk");
        assertThat(response.user().email()).isEqualTo("haluk@example.com");

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertThat(captor.getValue().getPasswordHash()).isEqualTo("encoded");
        assertThat(captor.getValue().getRole()).isEqualTo(Role.USER);
    }

    @Test
    void register_ThrowsWhenUsernameExists() {
        when(userRepository.existsByUsername("haluk")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(
                new RegisterRequest("haluk", "a@b.com", "Secret123!", "H", "K")
        )).isInstanceOf(ConflictException.class);
    }

    @Test
    void getByUsername_ThrowsWhenMissing() {
        UserServiceImpl userService = new UserServiceImpl(userRepository);
        when(userRepository.findByUsername("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getByUsername("missing"))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void getByUsername_ReturnsMappedUser() {
        UserServiceImpl userService = new UserServiceImpl(userRepository);
        Instant now = Instant.now();
        User user = User.builder()
                .id("1")
                .username("haluk")
                .email("haluk@example.com")
                .passwordHash("x")
                .firstName("Haluk")
                .lastName("Kilincer")
                .role(Role.USER)
                .enabled(true)
                .createdAt(now)
                .updatedAt(now)
                .build();
        when(userRepository.findByUsername("haluk")).thenReturn(Optional.of(user));

        UserResponse response = userService.getByUsername("haluk");

        assertThat(response.username()).isEqualTo("haluk");
        assertThat(response.firstName()).isEqualTo("Haluk");
    }
}
