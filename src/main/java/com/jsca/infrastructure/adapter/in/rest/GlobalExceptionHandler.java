package com.jsca.infrastructure.adapter.in.rest;

import com.jsca.infrastructure.adapter.in.rest.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Manejador global de excepciones según RFC 7807 (Problem Details).
 * Captura todas las excepciones y las convierte a formato application/problem+json.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja excepciones de tipo ResponseStatusException (404, etc.).
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(
            ResponseStatusException ex,
            HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse();
        error.setType("https://api.bank.example.com/problems/resource-not-found");
        error.setTitle(ex.getReason() != null ? ex.getReason() : "Not Found");
        error.setStatus(ex.getStatusCode().value());
        error.setDetail(ex.getMessage());
        error.setInstance(request.getRequestURI());

        return ResponseEntity
                .status(ex.getStatusCode())
                .header("Content-Type", "application/problem+json")
                .body(error);
    }

    /**
     * Maneja excepciones de validación de negocio (IllegalArgumentException).
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex,
            HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse();
        error.setType("https://api.bank.example.com/problems/validation-error");
        error.setTitle("Validation Failed");
        error.setStatus(400);
        error.setDetail(ex.getMessage());
        error.setInstance(request.getRequestURI());

        return ResponseEntity
                .badRequest()
                .header("Content-Type", "application/problem+json")
                .body(error);
    }

    /**
     * Maneja excepciones de validación de Bean Validation (@Valid).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        String detail = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .reduce((a, b) -> a + ", " + b)
                .orElse("Validation failed");

        ErrorResponse error = new ErrorResponse();
        error.setType("https://api.bank.example.com/problems/validation-error");
        error.setTitle("Validation Failed");
        error.setStatus(400);
        error.setDetail(detail);
        error.setInstance(request.getRequestURI());

        return ResponseEntity
                .badRequest()
                .header("Content-Type", "application/problem+json")
                .body(error);
    }

    /**
     * Maneja todas las demás excepciones no capturadas (fallback).
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex,
            HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse();
        error.setType("https://api.bank.example.com/problems/internal-error");
        error.setTitle("Internal Server Error");
        error.setStatus(500);
        error.setDetail("Error inesperado en el servidor");
        error.setInstance(request.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("Content-Type", "application/problem+json")
                .body(error);
    }
}

