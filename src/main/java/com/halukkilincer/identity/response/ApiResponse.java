package com.halukkilincer.identity.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        T data,
        int statusCode,
        String statusMessage,
        Instant timestamp
) {
    public static <T> ApiResponse<T> success(T data, int statusCode) {
        return new ApiResponse<>(data, statusCode, "SUCCESS", Instant.now());
    }

    public static <T> ApiResponse<T> error(T data, int statusCode, String statusMessage) {
        return new ApiResponse<>(data, statusCode, statusMessage, Instant.now());
    }
}
