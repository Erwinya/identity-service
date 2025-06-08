package com.example.demo.exception;

import com.example.demo.response.CustomResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<CustomResponse<String>> handleUserNotFound(NotFoundException ex) {
        return buildCustomErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<CustomResponse<String>> handleBadRequest(BadRequestException ex) {
        return buildCustomErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<CustomResponse<String>> buildCustomErrorResponse(String message, HttpStatus status) {
        CustomResponse<String> response = new CustomResponse<>(
                message,
                status.value(),
                status.getReasonPhrase(),
                Instant.now().toString()
        );
        return new ResponseEntity<>(response, status);
    }
}
