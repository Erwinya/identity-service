package com.halukkilincer.identity.controller;

import com.halukkilincer.identity.dto.UserResponse;
import com.halukkilincer.identity.response.ApiResponse;
import com.halukkilincer.identity.security.UserPrincipal;
import com.halukkilincer.identity.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User profile endpoints")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get the authenticated user's profile")
    public ApiResponse<UserResponse> getCurrentUser(@AuthenticationPrincipal UserPrincipal principal) {
        return ApiResponse.success(
                userService.getCurrentUser(principal.getUsername()),
                HttpStatus.OK.value()
        );
    }

    @GetMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a user profile by username")
    public ApiResponse<UserResponse> getByUsername(@PathVariable String username) {
        return ApiResponse.success(userService.getByUsername(username), HttpStatus.OK.value());
    }
}
