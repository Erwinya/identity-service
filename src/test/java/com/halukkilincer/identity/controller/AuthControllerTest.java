package com.halukkilincer.identity.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.halukkilincer.identity.dto.AuthResponse;
import com.halukkilincer.identity.dto.LoginRequest;
import com.halukkilincer.identity.dto.RegisterRequest;
import com.halukkilincer.identity.dto.UserResponse;
import com.halukkilincer.identity.entity.Role;
import com.halukkilincer.identity.security.JwtAuthenticationFilter;
import com.halukkilincer.identity.security.JwtService;
import com.halukkilincer.identity.service.AuthService;
import com.halukkilincer.identity.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = AuthController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthenticationFilter.class
        )
)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtService jwtService;

    @Test
    void register_ReturnsCreated() throws Exception {
        RegisterRequest request = new RegisterRequest(
                "haluk",
                "haluk@example.com",
                "Secret123!",
                "Haluk",
                "Kilincer"
        );
        UserResponse user = new UserResponse(
                "1", "haluk", "haluk@example.com", "Haluk", "Kilincer",
                Role.USER, true, Instant.now()
        );
        when(authService.register(any(RegisterRequest.class)))
                .thenReturn(new AuthResponse("token", "Bearer", 3600000L, user));

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statusCode").value(201))
                .andExpect(jsonPath("$.data.accessToken").value("token"))
                .andExpect(jsonPath("$.data.user.username").value("haluk"));
    }

    @Test
    void login_ReturnsOk() throws Exception {
        LoginRequest request = new LoginRequest("haluk", "Secret123!");
        UserResponse user = new UserResponse(
                "1", "haluk", "haluk@example.com", "Haluk", "Kilincer",
                Role.USER, true, Instant.now()
        );
        when(authService.login(any(LoginRequest.class)))
                .thenReturn(new AuthResponse("token", "Bearer", 3600000L, user));

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").value("token"));
    }
}
