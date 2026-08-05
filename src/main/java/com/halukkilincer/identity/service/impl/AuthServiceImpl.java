package com.halukkilincer.identity.service.impl;

import com.halukkilincer.identity.dto.AuthResponse;
import com.halukkilincer.identity.dto.LoginRequest;
import com.halukkilincer.identity.dto.RegisterRequest;
import com.halukkilincer.identity.entity.Role;
import com.halukkilincer.identity.entity.User;
import com.halukkilincer.identity.exception.ConflictException;
import com.halukkilincer.identity.exception.UnauthorizedException;
import com.halukkilincer.identity.mapper.UserMapper;
import com.halukkilincer.identity.repository.UserRepository;
import com.halukkilincer.identity.security.JwtService;
import com.halukkilincer.identity.security.UserPrincipal;
import com.halukkilincer.identity.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new ConflictException("Username is already taken");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new ConflictException("Email is already registered");
        }

        Instant now = Instant.now();
        User user = User.builder()
                .username(request.username().trim())
                .email(request.email().trim().toLowerCase())
                .passwordHash(passwordEncoder.encode(request.password()))
                .firstName(request.firstName().trim())
                .lastName(request.lastName().trim())
                .role(Role.USER)
                .enabled(true)
                .createdAt(now)
                .updatedAt(now)
                .build();

        User saved = userRepository.save(user);
        UserPrincipal principal = new UserPrincipal(saved);
        String token = jwtService.generateToken(principal);

        return new AuthResponse(
                token,
                "Bearer",
                jwtService.getExpirationMs(),
                UserMapper.toResponse(saved)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password())
            );
        } catch (Exception ex) {
            throw new UnauthorizedException("Invalid username or password");
        }

        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new UnauthorizedException("Invalid username or password"));

        UserPrincipal principal = new UserPrincipal(user);
        String token = jwtService.generateToken(principal);

        return new AuthResponse(
                token,
                "Bearer",
                jwtService.getExpirationMs(),
                UserMapper.toResponse(user)
        );
    }
}
