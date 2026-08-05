package com.halukkilincer.identity.service;

import com.halukkilincer.identity.dto.AuthResponse;
import com.halukkilincer.identity.dto.LoginRequest;
import com.halukkilincer.identity.dto.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
