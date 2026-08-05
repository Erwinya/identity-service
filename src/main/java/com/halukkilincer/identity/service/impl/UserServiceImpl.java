package com.halukkilincer.identity.service.impl;

import com.halukkilincer.identity.dto.UserResponse;
import com.halukkilincer.identity.exception.BadRequestException;
import com.halukkilincer.identity.exception.NotFoundException;
import com.halukkilincer.identity.mapper.UserMapper;
import com.halukkilincer.identity.repository.UserRepository;
import com.halukkilincer.identity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserResponse getByUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new BadRequestException("Username cannot be blank");
        }
        return userRepository.findByUsername(username.trim())
                .map(UserMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("User not found: " + username));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getCurrentUser(String username) {
        return getByUsername(username);
    }
}
