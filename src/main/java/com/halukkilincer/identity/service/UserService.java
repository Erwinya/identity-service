package com.halukkilincer.identity.service;

import com.halukkilincer.identity.dto.UserResponse;

public interface UserService {

    UserResponse getByUsername(String username);

    UserResponse getCurrentUser(String username);
}
